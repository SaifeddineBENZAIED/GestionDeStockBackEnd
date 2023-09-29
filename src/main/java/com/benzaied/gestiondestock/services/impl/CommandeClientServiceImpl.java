package com.benzaied.gestiondestock.services.impl;

import com.benzaied.gestiondestock.Repository.ArticleRepository;
import com.benzaied.gestiondestock.Repository.ClientRepository;
import com.benzaied.gestiondestock.Repository.CommandeClientRepository;
import com.benzaied.gestiondestock.Repository.LigneCommandeClientRepository;
import com.benzaied.gestiondestock.dto.*;
import com.benzaied.gestiondestock.exception.EntityNotFoundException;
import com.benzaied.gestiondestock.exception.ErrorCodes;
import com.benzaied.gestiondestock.exception.InvalidEntityException;
import com.benzaied.gestiondestock.exception.InvalidOperationException;
import com.benzaied.gestiondestock.model.*;
import com.benzaied.gestiondestock.services.CommandeClientService;
import com.benzaied.gestiondestock.services.MvmntStckService;
import com.benzaied.gestiondestock.validator.ArticleValidator;
import com.benzaied.gestiondestock.validator.CommandeClientValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommandeClientServiceImpl implements CommandeClientService {

    private CommandeClientRepository commandeClientRepository;
    private LigneCommandeClientRepository ligneCommandeClientRepository;
    private ClientRepository clientRepository;
    private ArticleRepository articleRepository;
    private MvmntStckService mvmntStckService;

    @Autowired
    public CommandeClientServiceImpl(CommandeClientRepository commandeClientRepository,LigneCommandeClientRepository ligneCommandeClientRepository,ClientRepository clientRepository,ArticleRepository articleRepository, MvmntStckService mvmntStckService) {
        this.commandeClientRepository = commandeClientRepository;
        this.ligneCommandeClientRepository = ligneCommandeClientRepository;
        this.clientRepository = clientRepository;
        this.articleRepository = articleRepository;
        this.mvmntStckService = mvmntStckService;
    }

    @Override
    public CommandeClientDto save(CommandeClientDto commandeClientDto) {
        List<String> errors = CommandeClientValidator.validate(commandeClientDto);
        if (!errors.isEmpty()) {
            log.error("La commande de client n'est pas valide {}", commandeClientDto);
            throw new InvalidEntityException("La commande de client n'est pas valide", ErrorCodes.COMMANDE_CLIENT_NOT_VALID, errors);
        }

        if (commandeClientDto.getId() != null && commandeClientDto.isCommandeLivree()){
            throw new InvalidOperationException("Cette commande est déja livree",ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        Optional<Client> client=clientRepository.findById(commandeClientDto.getClient().getId());
        if (client.isEmpty()){
            log.warn("Le client avec l'ID {} n'existe pas dans la BD",commandeClientDto.getClient().getId());
            throw new EntityNotFoundException("Aucun client avec l'ID "+commandeClientDto.getClient().getId()+" n'a ete trouve dans la BD",ErrorCodes.CLIENT_NOT_FOUND);
        }
        List<String> articleErrors = new ArrayList<>();
        if(commandeClientDto.getLigneCommandeClients()!= null){
            commandeClientDto.getLigneCommandeClients().forEach(lCC -> {
                if (lCC.getArticle()!=null){
                    Optional<Article> article=articleRepository.findById(lCC.getArticle().getId());
                    if(article.isEmpty()){
                        articleErrors.add("L'article avec l'ID "+lCC.getArticle().getId()+" n'existe pas");
                    }
                }else {
                    articleErrors.add("Impossible d'enregistrer les commandes avec un article null");
                }
             });
        }
        if(!articleErrors.isEmpty()){
            log.warn("Quelques articles ne figurent pas dans la BD , {}",articleErrors);
            throw new InvalidEntityException("Article n'existe pas dans la BD",ErrorCodes.ARTICLE_NOT_FOUND,articleErrors);
        }
        CommandeClient commandeClient = CommandeClientDto.toEntity(commandeClientDto);
        CommandeClient savedCommandeClient = commandeClientRepository.save(commandeClient);
        if (commandeClientDto.getLigneCommandeClients()!=null){
            commandeClientDto.getLigneCommandeClients().forEach(lCC -> {
                LigneCommandeClient ligneCommandeClient= LigneCommandeClientDto.toEntity(lCC);
                ligneCommandeClient.setCommandeClient(savedCommandeClient);
                ligneCommandeClientRepository.save(ligneCommandeClient);
                updateMvmntStck(ligneCommandeClient.getId());
            });
        }

        return CommandeClientDto.fromEntity(savedCommandeClient);
    }

    @Override
    public CommandeClientDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande){
        if (idCommande == null){
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec ID null",ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
        if (!StringUtils.hasLength(String.valueOf(etatCommande))){
            log.error("L etat de commande est null");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec etatCommande null",ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        CommandeClientDto commandeClientDto = findById(idCommande);

        if (commandeClientDto.isCommandeLivree()){
            throw new InvalidOperationException("Cette commande est déja livree",ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        commandeClientDto.setEtatCommande(etatCommande);

        CommandeClient savedCommandeClient = commandeClientRepository.save(CommandeClientDto.toEntity(commandeClientDto));

        if (commandeClientDto.isCommandeLivree()){
            updateMvmntStck(idCommande);
        }

        return CommandeClientDto.fromEntity(savedCommandeClient);
    }

    @Override
    public CommandeClientDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite){

        if (idCommande == null){
            throw new InvalidOperationException("Impossible de modifier une commande avec ID null",ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
        if (idLigneCommande == null){
            log.error("Ligne Commande Client is null");
            throw new InvalidOperationException("Impossible de modifier une ligne de commande avec id null",ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
        if (quantite == null || quantite.compareTo(BigDecimal.ZERO)==0){
            log.error("La quantite doit etre valide");
            throw new InvalidOperationException("Impossible de modifier une ligne de commande avec une quantite non valid",ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        CommandeClientDto commandeClientDto = findById(idCommande);

        if (commandeClientDto.isCommandeLivree()){
            throw new InvalidOperationException("Cette commande est déja livree",ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        Optional<LigneCommandeClient> ligneCommandeClientOptional = ligneCommandeClientRepository.findById(idLigneCommande);

        if (ligneCommandeClientOptional.isEmpty()){
            throw new EntityNotFoundException("Cette ligne de commande avec l'ID "+idLigneCommande+" n'existe pas",ErrorCodes.LIGNE_COMMANDE_CLIENT_NOT_FOUND);
        }

        LigneCommandeClient ligneCommandeClient = ligneCommandeClientOptional.get();

        ligneCommandeClient.setQuantite(quantite);

        ligneCommandeClientRepository.save(ligneCommandeClient);

        return commandeClientDto;
    }

    @Override
    public CommandeClientDto updateClient(Integer idCommande, Integer idClient){

        if (idCommande == null){
            throw new InvalidOperationException("Impossible de modifier une commande avec ID null",ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
        if (idClient == null){
            log.error("ID Client is null");
            throw new InvalidOperationException("Impossible de modifier l etat d'une commande avec id Client null",ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        CommandeClientDto commandeClientDto = findById(idCommande);

        if (commandeClientDto.isCommandeLivree()){
            throw new InvalidOperationException("Cette commande est déja livree",ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        Optional<Client> clientOptional = clientRepository.findById(idClient);

        if (clientOptional.isEmpty()){
            throw new EntityNotFoundException("Ce client avec l'ID "+idClient+" n'existe pas",ErrorCodes.CLIENT_NOT_FOUND);
        }

        commandeClientDto.setClient(ClientDto.fromEntity(clientOptional.get()));

        return CommandeClientDto.fromEntity(
                commandeClientRepository.save(CommandeClientDto.toEntity(commandeClientDto))
        );
    }

    @Override
    public CommandeClientDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer newIdArticle){
        if (idCommande == null){
            throw new InvalidOperationException("Impossible de modifier une commande avec ID null",ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
        if (idLigneCommande == null){
            log.error("Ligne Commande Client is null");
            throw new InvalidOperationException("Impossible de modifier une ligne de commande avec id null",ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
        if (newIdArticle == null){
            log.error("Le nouveau article a un ID null");
            throw new InvalidOperationException("Impossible de modifier un article non valid",ErrorCodes.ARTICLE_NOT_FOUND);
        }

        CommandeClientDto commandeClientDto = findById(idCommande);

        if (commandeClientDto.isCommandeLivree()){
            throw new InvalidOperationException("Cette commande est déja livree",ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }

        Optional<LigneCommandeClient> ligneCommandeClientOptional = ligneCommandeClientRepository.findById(idLigneCommande);

        if (ligneCommandeClientOptional.isEmpty()){
            throw new EntityNotFoundException("Cette ligne de commande avec l'ID "+idLigneCommande+" n'existe pas",ErrorCodes.LIGNE_COMMANDE_CLIENT_NOT_FOUND);
        }

        Optional<Article> articleOptional=articleRepository.findById(newIdArticle);
        if(articleOptional.isEmpty()){
            throw new EntityNotFoundException("Cet article ID "+newIdArticle+" n'existe pas",ErrorCodes.ARTICLE_NOT_FOUND);
        }
        List<String> errors = ArticleValidator.validate(ArticleDto.fromEntity(articleOptional.get()));

        if (!errors.isEmpty()){
            throw new InvalidEntityException("Article invalid",ErrorCodes.ARTICLE_NOT_VALID,errors);
        }

        LigneCommandeClient ligneCommandeClient = ligneCommandeClientOptional.get();
        ligneCommandeClient.setArticle(articleOptional.get());
        ligneCommandeClientRepository.save(ligneCommandeClient);

        return commandeClientDto;
    }

    @Override
    public CommandeClientDto deleteArticle(Integer idCommande, Integer idLigneCommande){

        if (idCommande == null){
            throw new InvalidOperationException("Impossible de modifier une commande avec ID null",ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
        if (idLigneCommande == null){
            log.error("Ligne Commande Client is null");
            throw new InvalidOperationException("Impossible de modifier une ligne de commande avec id null",ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
        CommandeClientDto commandeClientDto = findById(idCommande);

        if (commandeClientDto.isCommandeLivree()){
            throw new InvalidOperationException("Cette commande est déja livree",ErrorCodes.COMMANDE_CLIENT_NON_MODIFIABLE);
        }
        Optional<LigneCommandeClient> ligneCommandeClientOptional = ligneCommandeClientRepository.findById(idLigneCommande);

        if (ligneCommandeClientOptional.isEmpty()){
            throw new EntityNotFoundException("Cette ligne de commande avec l'ID "+idLigneCommande+" n'existe pas",ErrorCodes.LIGNE_COMMANDE_CLIENT_NOT_FOUND);
        }
        ligneCommandeClientRepository.deleteById(idLigneCommande);

        return commandeClientDto;
    }

    @Override
    public CommandeClientDto findById(Integer id) {
        if (id == null){
            log.error("L'ID de commande client est non valide");
            return null;
        }
        Optional<CommandeClient> commandeClientOptional = commandeClientRepository.findById(id);
        if (commandeClientOptional.isEmpty()) {
            throw new EntityNotFoundException("CommandeClient with id " + id + " not found",ErrorCodes.COMMANDE_CLIENT_NOT_FOUND);
        }
        return CommandeClientDto.fromEntity(commandeClientOptional.get());
    }

    @Override
    public CommandeClientDto findByCodeCC(String codeCC) {
        if (!StringUtils.hasLength(codeCC)) {
            log.error("Le code de commande client est invalide");
            return null;
        }
        Optional<CommandeClient> commandeClient = commandeClientRepository.findByCodeCC(codeCC);
        if (commandeClient.isEmpty()) {
            throw new EntityNotFoundException("CommandeClient with code " + codeCC + " not found",ErrorCodes.COMMANDE_CLIENT_NOT_FOUND);
        }
        return CommandeClientDto.fromEntity(commandeClient.get());
    }

    @Override
    public List<CommandeClientDto> findAll() {
        List<CommandeClient> commandesClients = commandeClientRepository.findAll();
        return commandesClients.stream()
                .map(CommandeClientDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LigneCommandeClientDto> findAllLigneCommandeClientsByCommandeClientId(Integer idCommande){
        return ligneCommandeClientRepository.findAllByCommandeClientId(idCommande).stream()
                .map(LigneCommandeClientDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(Integer id) {
        if (!commandeClientRepository.existsById(id) || id==null) {
            log.error("Commande client n'existe pas ou bien l ID est null");
        }
        CommandeClient commandeClient = commandeClientRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("CommandeClient not found !!")
        );
        List<LigneCommandeClient> ligneCommandeClients = ligneCommandeClientRepository.findAllByCommandeClientId(id);
        if (!ligneCommandeClients.isEmpty()){
            throw new InvalidOperationException("Impossible de supprimer une commande client déja utiliser",ErrorCodes.COMMANDE_CLIENT_ALREADY_IN_USE);
        }


        commandeClientRepository.deleteById(id);
        return true;
    }

    public void updateMvmntStck(Integer idCommande){
        List<LigneCommandeClient> ligneCommandeClients = ligneCommandeClientRepository.findAllByCommandeClientId(idCommande);
        ligneCommandeClients.forEach(lig -> {
            MvmntStckDto mvmntStckDto = MvmntStckDto.builder()
                    .article(ArticleDto.fromEntity(lig.getArticle()))
                    .dateMvmnt(Instant.now())
                    .typeMvmntStck(TypedeMvmntStck.SORTIE)
                    .sourceMvmntStck(SourceMvmntStck.COMMANDE_CLIENT)
                    .quantite(lig.getQuantite())
                    .idEntreprise(lig.getIdEntreprise())
                    .build();
            mvmntStckService.sortieStock(mvmntStckDto);
        });
    }

}
