package com.benzaied.gestiondestock.services.impl;

import com.benzaied.gestiondestock.Repository.ArticleRepository;
import com.benzaied.gestiondestock.Repository.CommandeFournisseurRepository;
import com.benzaied.gestiondestock.Repository.FournisseurRepository;
import com.benzaied.gestiondestock.Repository.LigneCommandeFournisseurRepository;
import com.benzaied.gestiondestock.dto.*;
import com.benzaied.gestiondestock.exception.EntityNotFoundException;
import com.benzaied.gestiondestock.exception.ErrorCodes;
import com.benzaied.gestiondestock.exception.InvalidEntityException;
import com.benzaied.gestiondestock.exception.InvalidOperationException;
import com.benzaied.gestiondestock.model.*;
import com.benzaied.gestiondestock.services.CommandeFournisseurService;
import com.benzaied.gestiondestock.services.MvmntStckService;
import com.benzaied.gestiondestock.validator.ArticleValidator;
import com.benzaied.gestiondestock.validator.CommandeFournisseurValidator;
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
public class CommandeFournisseurServiceImpl implements CommandeFournisseurService {

    private CommandeFournisseurRepository commandeFournisseurRepository;
    private LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository;
    private FournisseurRepository fournisseurRepository;
    private ArticleRepository articleRepository;
    private MvmntStckService mvmntStckService;

    @Autowired
    public CommandeFournisseurServiceImpl(CommandeFournisseurRepository commandeFournisseurRepository, LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository, FournisseurRepository fournisseurRepository, ArticleRepository articleRepository, MvmntStckService mvmntStckService) {
        this.commandeFournisseurRepository = commandeFournisseurRepository;
        this.ligneCommandeFournisseurRepository = ligneCommandeFournisseurRepository;
        this.fournisseurRepository = fournisseurRepository;
        this.articleRepository = articleRepository;
        this.mvmntStckService = mvmntStckService;
    }

    @Override
    public CommandeFournisseurDto save(CommandeFournisseurDto commandeFournisseurDto) {
        List<String> errors = CommandeFournisseurValidator.validate(commandeFournisseurDto);
        if (!errors.isEmpty()) {
            log.error("La commande fournisseur n'est pas valide {}", commandeFournisseurDto);
            throw new InvalidEntityException("La commande fournisseur n'est pas valide", ErrorCodes.CAMMANDE_FOURNISSEUR_NOT_VALID, errors);
        }

        if (commandeFournisseurDto.getId() != null && commandeFournisseurDto.isCommandeLivree()){
            throw new InvalidOperationException("Cette commande est déja livree",ErrorCodes.CAMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }

        Optional<Fournisseur> fournisseur = fournisseurRepository.findById(commandeFournisseurDto.getFournisseur().getId());
        if (fournisseur.isEmpty()) {
            log.warn("Le fournisseur avec l'ID {} n'existe pas dans la BD", commandeFournisseurDto.getFournisseur().getId());
            throw new EntityNotFoundException("Aucun fournisseur avec l'ID " + commandeFournisseurDto.getFournisseur().getId() + " n'a été trouvé dans la BD", ErrorCodes.FOURNISSEUR_NOT_FOUND);
        }
        List<String> articleErrors = new ArrayList<>();
        if (commandeFournisseurDto.getLigneCommandeFournisseurs() != null) {
            commandeFournisseurDto.getLigneCommandeFournisseurs().forEach(lCF -> {
                if (lCF.getArticle() != null) {
                    Optional<Article> article = articleRepository.findById(lCF.getArticle().getId());
                    if (article.isEmpty()) {
                        articleErrors.add("L'article avec l'ID " + lCF.getArticle().getId() + " n'existe pas");
                    }
                } else {
                    articleErrors.add("Impossible d'enregistrer les commandes avec un article null");
                }
            });
        }
        if (!articleErrors.isEmpty()) {
            log.warn("Article(s) not found in the database: {}", articleErrors);
            throw new InvalidEntityException("Article(s) not found in the database", ErrorCodes.ARTICLE_NOT_FOUND, articleErrors);
        }
        CommandeFournisseur commandeFournisseur = CommandeFournisseurDto.toEntity(commandeFournisseurDto);
        CommandeFournisseur savedCommandeFournisseur = commandeFournisseurRepository.save(commandeFournisseur);
        if (commandeFournisseurDto.getLigneCommandeFournisseurs() != null) {
            commandeFournisseurDto.getLigneCommandeFournisseurs().forEach(lCF -> {
                LigneCommandeFournisseur ligneCommandeFournisseur = LigneCommandeFournisseurDto.toEntity(lCF);
                ligneCommandeFournisseur.setCommandeFournisseur(savedCommandeFournisseur);
                ligneCommandeFournisseurRepository.save(ligneCommandeFournisseur);
                updateMvmntStck(ligneCommandeFournisseur.getId());
            });
        }

        return CommandeFournisseurDto.fromEntity(savedCommandeFournisseur);
    }

    @Override
    public CommandeFournisseurDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande){
        if (idCommande == null){
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec ID null",ErrorCodes.CAMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
        if (!StringUtils.hasLength(String.valueOf(etatCommande))){
            log.error("L etat de commande est null");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec etatCommande null",ErrorCodes.CAMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }

        CommandeFournisseurDto commandeFournisseurDto = findById(idCommande);

        if (commandeFournisseurDto.isCommandeLivree()){
            throw new InvalidOperationException("Cette commande est déja livree",ErrorCodes.CAMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }

        commandeFournisseurDto.setEtatCommande(etatCommande);

        CommandeFournisseur savedCommande = commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(commandeFournisseurDto));

        if (commandeFournisseurDto.isCommandeLivree()){
            updateMvmntStck(idCommande);
        }

        return CommandeFournisseurDto.fromEntity(savedCommande);
    }

    @Override
    public CommandeFournisseurDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
        if (idCommande == null){
            throw new InvalidOperationException("Impossible de modifier une commande avec ID null",ErrorCodes.CAMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
        if (idLigneCommande == null){
            log.error("Ligne Commande Client is null");
            throw new InvalidOperationException("Impossible de modifier une ligne de commande avec id null",ErrorCodes.CAMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
        if (quantite == null || quantite.compareTo(BigDecimal.ZERO)==0){
            log.error("La quantite doit etre valide");
            throw new InvalidOperationException("Impossible de modifier une ligne de commande avec une quantite non valid",ErrorCodes.CAMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }

        CommandeFournisseurDto commandeFournisseurDto = findById(idCommande);

        if (commandeFournisseurDto.isCommandeLivree()){
            throw new InvalidOperationException("Cette commande est déja livree",ErrorCodes.CAMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }

        Optional<LigneCommandeFournisseur> ligneCommandeFournisseurOptional = ligneCommandeFournisseurRepository.findById(idLigneCommande);

        if (ligneCommandeFournisseurOptional.isEmpty()){
            throw new EntityNotFoundException("Cette ligne de commande avec l'ID "+idLigneCommande+" n'existe pas",ErrorCodes.LIGNE_COMMANDE_FOURNISSEUR_NOT_FOUND);
        }

        LigneCommandeFournisseur ligneCommandeFournisseur = ligneCommandeFournisseurOptional.get();

        ligneCommandeFournisseur.setQuantite(quantite);

        ligneCommandeFournisseurRepository.save(ligneCommandeFournisseur);

        return commandeFournisseurDto;
    }

    @Override
    public CommandeFournisseurDto updateFournisseur(Integer idCommande, Integer idFournisseur) {
        if (idCommande == null){
            throw new InvalidOperationException("Impossible de modifier une commande avec ID null",ErrorCodes.CAMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
        if (idFournisseur == null){
            log.error("ID Fournisseur is null");
            throw new InvalidOperationException("Impossible de modifier l etat d'une commande avec id Fournisseur null",ErrorCodes.CAMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }

        CommandeFournisseurDto commandeFournisseurDto = findById(idCommande);

        if (commandeFournisseurDto.isCommandeLivree()){
            throw new InvalidOperationException("Cette commande est déja livree",ErrorCodes.CAMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }

        Optional<Fournisseur> fournisseurOptional = fournisseurRepository.findById(idFournisseur);

        if (fournisseurOptional.isEmpty()){
            throw new EntityNotFoundException("Ce Fournisseur avec l'ID "+idFournisseur+" n'existe pas",ErrorCodes.FOURNISSEUR_NOT_FOUND);
        }

        commandeFournisseurDto.setFournisseur(FournisseurDto.fromEntity(fournisseurOptional.get()));

        return CommandeFournisseurDto.fromEntity(
                commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(commandeFournisseurDto))
        );
    }

    @Override
    public CommandeFournisseurDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer newIdArticle) {
        if (idCommande == null){
            throw new InvalidOperationException("Impossible de modifier une commande avec ID null",ErrorCodes.CAMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
        if (idLigneCommande == null){
            log.error("Ligne Commande Client is null");
            throw new InvalidOperationException("Impossible de modifier une ligne de commande avec id null",ErrorCodes.CAMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
        if (newIdArticle == null){
            log.error("Le nouveau article a un ID null");
            throw new InvalidOperationException("Impossible de modifier un article non valid",ErrorCodes.ARTICLE_NOT_FOUND);
        }

        CommandeFournisseurDto commandeFournisseurDto = findById(idCommande);

        if (commandeFournisseurDto.isCommandeLivree()){
            throw new InvalidOperationException("Cette commande est déja livree",ErrorCodes.CAMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }

        Optional<LigneCommandeFournisseur> ligneCommandeFournisseurOptional = ligneCommandeFournisseurRepository.findById(idLigneCommande);

        if (ligneCommandeFournisseurOptional.isEmpty()){
            throw new EntityNotFoundException("Cette ligne de commande avec l'ID "+idLigneCommande+" n'existe pas",ErrorCodes.LIGNE_COMMANDE_FOURNISSEUR_NOT_FOUND);
        }

        Optional<Article> articleOptional=articleRepository.findById(newIdArticle);
        if(articleOptional.isEmpty()){
            throw new EntityNotFoundException("Cet article ID "+newIdArticle+" n'existe pas",ErrorCodes.ARTICLE_NOT_FOUND);
        }
        List<String> errors = ArticleValidator.validate(ArticleDto.fromEntity(articleOptional.get()));

        if (!errors.isEmpty()){
            throw new InvalidEntityException("Article invalid",ErrorCodes.ARTICLE_NOT_VALID,errors);
        }

        LigneCommandeFournisseur ligneCommandeFournisseur = ligneCommandeFournisseurOptional.get();
        ligneCommandeFournisseur.setArticle(articleOptional.get());
        ligneCommandeFournisseurRepository.save(ligneCommandeFournisseur);

        return commandeFournisseurDto;
    }

    @Override
    public CommandeFournisseurDto deleteArticle(Integer idCommande, Integer idLigneCommande) {
        if (idCommande == null){
            throw new InvalidOperationException("Impossible de modifier une commande avec ID null",ErrorCodes.CAMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
        if (idLigneCommande == null){
            log.error("Ligne Commande Fournisseur is null");
            throw new InvalidOperationException("Impossible de modifier une ligne de commande avec id null",ErrorCodes.CAMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
        CommandeFournisseurDto commandeFournisseurDto = findById(idCommande);

        if (commandeFournisseurDto.isCommandeLivree()){
            throw new InvalidOperationException("Cette commande est déja livree",ErrorCodes.CAMMANDE_FOURNISSEUR_NON_MODIFIABLE);
        }
        Optional<LigneCommandeFournisseur> ligneCommandeFournisseurOptional = ligneCommandeFournisseurRepository.findById(idLigneCommande);

        if (ligneCommandeFournisseurOptional.isEmpty()){
            throw new EntityNotFoundException("Cette ligne de commande avec l'ID "+idLigneCommande+" n'existe pas",ErrorCodes.LIGNE_COMMANDE_FOURNISSEUR_NOT_FOUND);
        }
        ligneCommandeFournisseurRepository.deleteById(idLigneCommande);

        return commandeFournisseurDto;
    }

    @Override
    public CommandeFournisseurDto findById(Integer id) {
        if (id == null) {
            log.error("L'ID de commande fournisseur est non valide");
            return null;
        }
        Optional<CommandeFournisseur> commandeFournisseurOptional = commandeFournisseurRepository.findById(id);
        if (commandeFournisseurOptional.isEmpty()) {
            throw new EntityNotFoundException("CommandeFournisseur with id " + id + " not found", ErrorCodes.CAMMANDE_FOURNISSEUR_NOT_FOUND);
        }
        return CommandeFournisseurDto.fromEntity(commandeFournisseurOptional.get());
    }

    @Override
    public CommandeFournisseurDto findByCodeCF(String codeCF) {
        if (!StringUtils.hasLength(codeCF)) {
            log.error("Le code de commande fournisseur est invalide");
            return null;
        }
        Optional<CommandeFournisseur> commandeFournisseur = commandeFournisseurRepository.findByCodeCF(codeCF);
        if (commandeFournisseur.isEmpty()) {
            throw new EntityNotFoundException("CommandeFournisseur with code " + codeCF + " not found", ErrorCodes.CAMMANDE_FOURNISSEUR_NOT_FOUND);
        }
        return CommandeFournisseurDto.fromEntity(commandeFournisseur.get());
    }

    @Override
    public List<CommandeFournisseurDto> findAll() {
        List<CommandeFournisseur> commandesFournisseurs = commandeFournisseurRepository.findAll();
        return commandesFournisseurs.stream()
                .map(CommandeFournisseurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LigneCommandeFournisseurDto> findAllLigneCommandeFournisseursByCommandeFournisseurId(Integer idCommande){
        return ligneCommandeFournisseurRepository.findAllByCommandeFournisseurId(idCommande).stream()
                .map(LigneCommandeFournisseurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(Integer id) {
        if (!commandeFournisseurRepository.existsById(id) || id == null) {
            log.error("CommandeFournisseur with id " + id + " not found");
            return false;
        }
        CommandeFournisseur commandeFournisseur = commandeFournisseurRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("CommandeFournisseur not found !!")
        );
        List<LigneCommandeFournisseur> ligneCommandeFournisseurs = ligneCommandeFournisseurRepository.findAllByCommandeFournisseurId(id);
        if (!ligneCommandeFournisseurs.isEmpty()){
            throw new InvalidOperationException("Impossible de supprimer une commande fournisseur déja utiliser",ErrorCodes.CAMMANDE_FOURNISSEUR_ALREADY_IN_USE);
        }


        commandeFournisseurRepository.deleteById(id);
        return true;
    }

    public void updateMvmntStck(Integer idCommande){
        List<LigneCommandeFournisseur> ligneCommandeFournisseurs = ligneCommandeFournisseurRepository.findAllByCommandeFournisseurId(idCommande);
        ligneCommandeFournisseurs.forEach(lig -> {
            MvmntStckDto mvmntStckDto = MvmntStckDto.builder()
                    .article(ArticleDto.fromEntity(lig.getArticle()))
                    .dateMvmnt(Instant.now())
                    .typeMvmntStck(TypedeMvmntStck.ENTREE)
                    .sourceMvmntStck(SourceMvmntStck.COMMANDE_FOURNISSEUR)
                    .quantite(lig.getQuantite())
                    .idEntreprise(lig.getIdEntreprise())
                    .build();
            mvmntStckService.entreeStock(mvmntStckDto);
        });
    }

}
