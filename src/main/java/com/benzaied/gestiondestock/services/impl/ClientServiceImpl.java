package com.benzaied.gestiondestock.services.impl;

import com.benzaied.gestiondestock.Repository.ArticleRepository;
import com.benzaied.gestiondestock.Repository.ClientRepository;
import com.benzaied.gestiondestock.Repository.CommandeClientRepository;
import com.benzaied.gestiondestock.dto.ArticleDto;
import com.benzaied.gestiondestock.dto.ChangerMotDePasseUtilisateurDto;
import com.benzaied.gestiondestock.dto.ClientDto;
import com.benzaied.gestiondestock.dto.UtilisateurDto;
import com.benzaied.gestiondestock.exception.EntityNotFoundException;
import com.benzaied.gestiondestock.exception.ErrorCodes;
import com.benzaied.gestiondestock.exception.InvalidEntityException;
import com.benzaied.gestiondestock.exception.InvalidOperationException;
import com.benzaied.gestiondestock.model.Client;
import com.benzaied.gestiondestock.model.CommandeClient;
import com.benzaied.gestiondestock.model.Utilisateur;
import com.benzaied.gestiondestock.services.ArticleService;
import com.benzaied.gestiondestock.services.ClientService;
import com.benzaied.gestiondestock.validator.ClientValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ArticleRepository articleRepository;
    private final PasswordEncoder encoder;
    private final ArticleService articleService;
    private CommandeClientRepository commandeClientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, ArticleRepository articleRepository, PasswordEncoder encoder, ArticleService articleService, CommandeClientRepository commandeClientRepository) {
        this.clientRepository = clientRepository;
        this.articleRepository = articleRepository;
        this.encoder = encoder;
        this.articleService = articleService;
        this.commandeClientRepository = commandeClientRepository;
    }

    @Override
    public ClientDto save(ClientDto clientDto) {
        List<String> errors = ClientValidator.validate(clientDto);
        if (!errors.isEmpty()) {
            log.error("Le client n'est pas valide {}", clientDto);
            throw new InvalidEntityException("Le client n'est pas valide", ErrorCodes.CLIENT_NOT_VALID, errors);
        }

        if(clientAlreadyExists(clientDto.getEmail())) {
            throw new InvalidEntityException("Un autre utilisateur avec le meme email existe deja", ErrorCodes.USER_ALREADY_IN_EXIST,
                    Collections.singletonList("Un autre utilisateur avec le meme email existe deja dans la BDD"));
        }

        clientDto.setMotDePasse(encoder.encode(clientDto.getMotDePasse()));
        return ClientDto.fromEntity(clientRepository.save(ClientDto.toEntity(clientDto)));
    }

    private boolean clientAlreadyExists(String email) {
        Optional<Client> client = clientRepository.findByEmail(email);
        return client.isPresent();
    }

    @Override
    public ClientDto findById(Integer id) {
        if (id == null) {
            log.error("L'ID du client est non valide");
            return null;
        }
        Optional<Client> client = clientRepository.findById(id);
        ClientDto clientDto = ClientDto.fromEntity(client.get());
        return Optional.of(clientDto).orElseThrow(() -> new EntityNotFoundException("Aucun client avec l'ID = " + id + " n'est trouvé dans la BD", ErrorCodes.CLIENT_NOT_FOUND));
    }

    @Override
    public ClientDto findByNomAndPrenomIgnoreCase(String nom, String prenom) {
        if (!StringUtils.hasLength(nom) || !StringUtils.hasLength(prenom)) {
            log.error("Le nom ou le prénom du client est invalide");
            return null;
        }
        Optional<Client> client = clientRepository.findByNomAndPrenomIgnoreCase(nom, prenom);
        ClientDto clientDto = ClientDto.fromEntity(client.get());
        return Optional.of(clientDto).orElseThrow(() -> new EntityNotFoundException("Aucun client avec le nom = " + nom + " et le prénom = " + prenom + " n'est trouvé dans la BD", ErrorCodes.CLIENT_NOT_FOUND));
    }

    @Override
    public ClientDto findByNomIgnoreCase(String nom) {
        if (!StringUtils.hasLength(nom)) {
            log.error("Le nom du client est invalide");
            return null;
        }
        Optional<Client> client = clientRepository.findByNomIgnoreCase(nom);
        ClientDto clientDto = ClientDto.fromEntity(client.get());
        return Optional.of(clientDto).orElseThrow(() -> new EntityNotFoundException("Aucun client avec le nom = " + nom + " n'est trouvé dans la BD", ErrorCodes.CLIENT_NOT_FOUND));
    }

    @Override
    public ClientDto findByPrenomIgnoreCase(String prenom) {
        if (!StringUtils.hasLength(prenom)) {
            log.error("Le prénom du client est invalide");
            return null;
        }
        Optional<Client> client = clientRepository.findByPrenomIgnoreCase(prenom);
        ClientDto clientDto = ClientDto.fromEntity(client.get());
        return Optional.of(clientDto).orElseThrow(() -> new EntityNotFoundException("Aucun client avec le prénom = " + prenom + " n'est trouvé dans la BD", ErrorCodes.CLIENT_NOT_FOUND));
    }

    @Override
    public List<ClientDto> findAll() {
        return clientRepository.findAll().stream()
                .map(ClientDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDto> findAllArticlesByTypeClient(ClientDto clientDto){
        return articleService.findByTypeClient(clientDto);
    }

    @Override
    public List<ArticleDto> getArticlesForClientByTypeAndCategory(ClientDto clientDto, Integer idCategorie) {
        return articleService.getArticlesForClientByTypeAndCategory(clientDto, idCategorie);
    }

    @Override
    public boolean delete(Integer id) {
        if (id == null) {
            log.error("L'ID du client est non valide");
            return false;
        }
        Client client = clientRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Client not found !!")
        );
        List<CommandeClient> commandeClients = commandeClientRepository.findAllByClientId(id);
        if (!commandeClients.isEmpty()){
            throw new InvalidOperationException("Impossible de supprimer ce client car il à déja passer des commandes",ErrorCodes.CLIENT_ALREADY_IN_USE);
        }


        clientRepository.deleteById(id);
        return true;
    }

    @Override
    public ClientDto findByEmail(String email) {
        return ClientDto.fromEntity((clientRepository.findByEmail(email)).get());
    }

    @Override
    public ClientDto changerMotDePasse(ChangerMotDePasseUtilisateurDto dto) {
        if (dto == null){
            log.warn("Impossible de modifier le mot de passe avec un objet null");
            throw new InvalidOperationException("Aucune information n'a ete fourni pour pouvoir changer le mot de passe",ErrorCodes.CLIENT_CHANGE_PASSWORD_OBJECT_NON_VALID);
        }

        if (dto.getId() == null){
            log.warn("Impossible de modifier le mot de passe avec un ID null");
            throw new InvalidOperationException("CLIENT ID IS NULL",ErrorCodes.CLIENT_CHANGE_PASSWORD_OBJECT_NON_VALID);
        }

        if (!StringUtils.hasLength(dto.getMotDePasse()) || !StringUtils.hasLength(dto.getConfirmMotDePasse())){
            log.warn("Impossible de modifier le mot de passe avec un mot de passe null");
            throw new InvalidOperationException("PASSWORD IS NULL",ErrorCodes.CLIENT_CHANGE_PASSWORD_OBJECT_NON_VALID);
        }

        if (!dto.getMotDePasse().equals(dto.getConfirmMotDePasse())){
            log.warn("Impossible de confirmer de mot de passe !!");
            throw new InvalidOperationException("PASSWORD CONFIRMATION IS INCORRECT",ErrorCodes.CLIENT_CHANGE_PASSWORD_OBJECT_NON_VALID);
        }

        Optional<Client> clientOptional = clientRepository.findById(dto.getId());

        if (clientOptional.isEmpty()){
            log.warn("Aucun client n'a ete trouve avec l'ID "+dto.getId());
            throw new EntityNotFoundException("Aucun client n'a ete trouve avec l'ID "+dto.getId(),ErrorCodes.CLIENT_NOT_FOUND);
        }

        Client client = clientOptional.get();

        client.setMotDePasse(encoder.encode(dto.getMotDePasse()));


        return ClientDto.fromEntity(clientRepository.save(client));
    }

}
