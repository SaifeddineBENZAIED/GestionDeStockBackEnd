package com.benzaied.gestiondestock.services.impl;

import com.benzaied.gestiondestock.Repository.UtilisateurRepository;
import com.benzaied.gestiondestock.dto.ChangerMotDePasseUtilisateurDto;
import com.benzaied.gestiondestock.dto.UtilisateurDto;
import com.benzaied.gestiondestock.exception.EntityNotFoundException;
import com.benzaied.gestiondestock.exception.ErrorCodes;
import com.benzaied.gestiondestock.exception.InvalidEntityException;
import com.benzaied.gestiondestock.exception.InvalidOperationException;
import com.benzaied.gestiondestock.model.Utilisateur;
import com.benzaied.gestiondestock.services.UtilisateurService;
import com.benzaied.gestiondestock.validator.UtilisateurValidator;
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
public class UtilisateurServiceImpl implements UtilisateurService {

    private UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository, PasswordEncoder encoder){
        this.utilisateurRepository = utilisateurRepository;
        this.encoder = encoder;
    }

    @Override
    public UtilisateurDto save(UtilisateurDto utilisateurDto) {
        List<String> errors = UtilisateurValidator.validate(utilisateurDto);
        if(!errors.isEmpty()){
            log.error("L'utilisateur n'est pas valide {}", utilisateurDto);
            throw new InvalidEntityException("L'utilisateur n'est pas valide", ErrorCodes.USER_NOT_VALID, errors);
        }
        if(userAlreadyExists(utilisateurDto.getEmail())) {
            throw new InvalidEntityException("Un autre utilisateur avec le meme email existe deja", ErrorCodes.USER_ALREADY_IN_EXIST,
                    Collections.singletonList("Un autre utilisateur avec le meme email existe deja dans la BDD"));
        }
        utilisateurDto.setMotDePasse(encoder.encode(utilisateurDto.getMotDePasse()));
        return UtilisateurDto.fromEntity(utilisateurRepository.save(UtilisateurDto.toEntity(utilisateurDto)));
    }

    private boolean userAlreadyExists(String email) {
        Optional<Utilisateur> user = utilisateurRepository.findByEmail(email);
        return user.isPresent();
    }

    @Override
    public UtilisateurDto findById(Integer id) {
        if (id == null){
            log.error("L'ID d'utilisateur est non valide");
            return null;
        }
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(id);
        UtilisateurDto utilisateurDto = UtilisateurDto.fromEntity(utilisateur.get());
        return Optional.of(utilisateurDto).orElseThrow(() -> new EntityNotFoundException("Aucun utilisateur avec l'ID = "+ id +" n'est trouvé dans la BD",ErrorCodes.USER_NOT_FOUND));
    }

    @Override
    public UtilisateurDto findByNomAndPrenomIgnoreCase(String nom, String prenom) {
        if (!StringUtils.hasLength(nom) || !StringUtils.hasLength(prenom)) {
            log.error("Le nom ou le prénom d'utilisateur est invalide");
            return null;
        }
        Optional<Utilisateur> utilisateur = utilisateurRepository.findByNomAndPrenomIgnoreCase(nom, prenom);
        UtilisateurDto utilisateurDto = UtilisateurDto.fromEntity(utilisateur.get());
        return Optional.of(utilisateurDto).orElseThrow(() -> new EntityNotFoundException("Aucun utilisateur avec le nom = " + nom + " et le prénom = " + prenom + " n'est trouvé dans la BD", ErrorCodes.USER_NOT_FOUND));
    }

    @Override
    public UtilisateurDto findByNomIgnoreCase(String nom) {
        if (!StringUtils.hasLength(nom)) {
            log.error("Le nom d'utilisateur est invalide");
            return null;
        }
        Optional<Utilisateur> utilisateur = utilisateurRepository.findByNomIgnoreCase(nom);
        UtilisateurDto utilisateurDto = UtilisateurDto.fromEntity(utilisateur.get());
        return Optional.of(utilisateurDto).orElseThrow(() -> new EntityNotFoundException("Aucun utilisateur avec le nom = " + nom + " n'est trouvé dans la BD", ErrorCodes.USER_NOT_FOUND));
    }

    @Override
    public UtilisateurDto findByPrenomIgnoreCase(String prenom) {
        if (!StringUtils.hasLength(prenom)) {
            log.error("Le prénom d'utilisateur est invalide");
            return null;
        }
        Optional<Utilisateur> utilisateur = utilisateurRepository.findByPrenomIgnoreCase(prenom);
        UtilisateurDto utilisateurDto = UtilisateurDto.fromEntity(utilisateur.get());
        return Optional.of(utilisateurDto).orElseThrow(() -> new EntityNotFoundException("Aucun utilisateur avec le prénom = " + prenom + " n'est trouvé dans la BD", ErrorCodes.USER_NOT_FOUND));
    }

    @Override
    public List<UtilisateurDto> findAll() {
        return utilisateurRepository.findAll().stream()
                .map(UtilisateurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(Integer id) {
        if (id == null){
            log.error("L'ID d'utilisateur est non valide");
            return false;
        }
        Utilisateur utilisateur = utilisateurRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not found !!")
        );
        utilisateurRepository.deleteById(id);
        return true;
    }

    @Override
    public UtilisateurDto findByEmail(String email) {
        return UtilisateurDto.fromEntity((utilisateurRepository.findByEmail(email)).get());
    }

    @Override
    public UtilisateurDto changerMotDePasse(ChangerMotDePasseUtilisateurDto dto) {
        if (dto == null){
            log.warn("Impossible de modifier le mot de passe avec un objet null");
            throw new InvalidOperationException("Aucune information n'a ete fourni pour pouvoir changer le mot de passe",ErrorCodes.USER_CHANGE_PASSWORD_OBJECT_NON_VALID);
        }

        if (dto.getId() == null){
            log.warn("Impossible de modifier le mot de passe avec un ID null");
            throw new InvalidOperationException("USER ID IS NULL",ErrorCodes.USER_CHANGE_PASSWORD_OBJECT_NON_VALID);
        }

        if (!StringUtils.hasLength(dto.getMotDePasse()) || !StringUtils.hasLength(dto.getConfirmMotDePasse())){
            log.warn("Impossible de modifier le mot de passe avec un mot de passe null");
            throw new InvalidOperationException("PASSWORD IS NULL",ErrorCodes.USER_CHANGE_PASSWORD_OBJECT_NON_VALID);
        }

        if (!dto.getMotDePasse().equals(dto.getConfirmMotDePasse())){
            log.warn("Impossible de confirmer de mot de passe !!");
            throw new InvalidOperationException("PASSWORD CONFIRMATION IS INCORRECT",ErrorCodes.USER_CHANGE_PASSWORD_OBJECT_NON_VALID);
        }

        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(dto.getId());

        if (utilisateurOptional.isEmpty()){
            log.warn("Aucun utilisateur n'a ete trouve avec l'ID "+dto.getId());
            throw new EntityNotFoundException("Aucun utilisateur n'a ete trouve avec l'ID "+dto.getId(),ErrorCodes.USER_NOT_FOUND);
        }

        Utilisateur utilisateur = utilisateurOptional.get();

        utilisateur.setMotDePasse(encoder.encode(dto.getMotDePasse()));


        return UtilisateurDto.fromEntity(utilisateurRepository.save(utilisateur));
    }

}
