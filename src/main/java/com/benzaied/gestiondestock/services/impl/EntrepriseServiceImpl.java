package com.benzaied.gestiondestock.services.impl;

import com.benzaied.gestiondestock.Repository.EntrepriseRepository;
import com.benzaied.gestiondestock.auth.AuthenticationService;
import com.benzaied.gestiondestock.dto.EntrepriseDto;
import com.benzaied.gestiondestock.dto.UtilisateurDto;
import com.benzaied.gestiondestock.exception.EntityNotFoundException;
import com.benzaied.gestiondestock.exception.ErrorCodes;
import com.benzaied.gestiondestock.exception.InvalidEntityException;
import com.benzaied.gestiondestock.model.Entreprise;
import com.benzaied.gestiondestock.model.Roles;
import com.benzaied.gestiondestock.model.Utilisateur;
import com.benzaied.gestiondestock.model.auth.ExtendedUser;
import com.benzaied.gestiondestock.services.EntrepriseService;
import com.benzaied.gestiondestock.services.UtilisateurService;
import com.benzaied.gestiondestock.services.auth.ApplicationUserDetailsService;
import com.benzaied.gestiondestock.token.Token;
import com.benzaied.gestiondestock.token.TokenRepository;
import com.benzaied.gestiondestock.utils.JwtUtil;
import com.benzaied.gestiondestock.validator.EntrepriseValidator;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(rollbackOn = Exception.class)
@Service
@Slf4j
public class EntrepriseServiceImpl implements EntrepriseService {

    private EntrepriseRepository entrepriseRepository;

    private UtilisateurService utilisateurService;

    private JwtUtil jwtUtil;

    private TokenRepository tokenRepository;

    private ApplicationUserDetailsService applicationUserDetailsService;

    @Autowired
    public EntrepriseServiceImpl(EntrepriseRepository entrepriseRepository, UtilisateurService utilisateurService, JwtUtil jwtUtil, TokenRepository tokenRepository, ApplicationUserDetailsService applicationUserDetailsService){
        this.entrepriseRepository = entrepriseRepository;
        this.utilisateurService = utilisateurService;
        this.jwtUtil = jwtUtil;
        this.tokenRepository = tokenRepository;
        this.applicationUserDetailsService = applicationUserDetailsService;
    }

    @Override
    public EntrepriseDto save(EntrepriseDto entrepriseDto) {
        List<String> errors = EntrepriseValidator.validate(entrepriseDto);
        if (!errors.isEmpty()) {
            log.error("L'entreprise n'est pas valide {}", entrepriseDto);
            throw new InvalidEntityException("L'entreprise n'est pas valide", ErrorCodes.ENTREPRISE_NOT_VALID, errors);
        }
        EntrepriseDto savedEntreprise = EntrepriseDto.fromEntity(entrepriseRepository.save(EntrepriseDto.toEntity(entrepriseDto)));

        UtilisateurDto utilisateur = fromEntreprise(savedEntreprise);

        UtilisateurDto savedUser = utilisateurService.save(utilisateur);

        generateRandomTokens(savedUser);

        return  savedEntreprise;
    }

    private UtilisateurDto fromEntreprise(EntrepriseDto dto) {
        return UtilisateurDto.builder()
                .adresse(dto.getAdresse())
                .nom(dto.getNom())
                .prenom(dto.getCodeFiscal())
                .email(dto.getEmail())
                .motDePasse(generateRandomPassword())
                .entreprise(dto)
                .dateNaissance(Instant.now())
                .numTelephone(dto.getNumTelephone())
                .image(dto.getImage())
                .roles(List.of(Roles.ADMIN))
                .build();
    }

    private String generateRandomPassword() {
        return "som3R@nd0mP@$$word$2a$10$uV3uYOSHhKAWXj0kmq1kuZrS8mGrY6NOcxS5hkgjQr5AAp1mm0LK";
    }

    private void generateRandomTokens(UtilisateurDto utilisateurDto) {
        var userDetails = applicationUserDetailsService.loadUserByUsername(utilisateurDto.getEmail());
        var jwtToken = jwtUtil.generateToken((ExtendedUser) userDetails);
        //var refreshToken = jwtUtil.generateRefreshToken((ExtendedUser) userDetails);
        saveUserToken(UtilisateurDto.toEntity(utilisateurDto), jwtToken);

    }

    private void saveUserToken(Utilisateur user, String jwtToken) {
        var token = Token.builder()
                .utilisateur(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    @Override
    public EntrepriseDto findById(Integer id) {
        if (id == null) {
            log.error("L'ID d'entreprise est non valide");
            return null;
        }
        Optional<Entreprise> entreprise = entrepriseRepository.findById(id);
        EntrepriseDto entrepriseDto = EntrepriseDto.fromEntity(entreprise.get());
        return Optional.of(entrepriseDto).orElseThrow(() -> new EntityNotFoundException("Aucune entreprise avec l'ID = " + id + " n'est trouvée dans la BD", ErrorCodes.ENTREPRISE_NOT_FOUND));
    }

    @Override
    public EntrepriseDto findByNomIgnoreCase(String nom) {
        if (!StringUtils.hasLength(nom)) {
            log.error("Le nom d'entreprise est invalide");
            return null;
        }
        Optional<Entreprise> entreprise = entrepriseRepository.findByNomIgnoreCase(nom);
        EntrepriseDto entrepriseDto = EntrepriseDto.fromEntity(entreprise.get());
        return Optional.of(entrepriseDto).orElseThrow(() -> new EntityNotFoundException("Aucune entreprise avec le nom = " + nom + " n'est trouvée dans la BD", ErrorCodes.ENTREPRISE_NOT_FOUND));
    }

    @Override
    public List<EntrepriseDto> findAll() {
        return entrepriseRepository.findAll().stream()
                .map(EntrepriseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(Integer id) {
        if (id == null) {
            log.error("L'ID d'entreprise est non valide");
            return false;
        }
        Entreprise entreprise = entrepriseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Entreprise not found !!")
        );
        entrepriseRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean deleteByNom(String nom) {
        if (!StringUtils.hasLength(nom)) {
            log.error("Le nom d'entreprise est invalide");
            return false;
        }
        Entreprise entreprise = entrepriseRepository.findByNomIgnoreCase(nom).orElseThrow(
                () -> new EntityNotFoundException("Entreprise not found !!")
        );
        entrepriseRepository.deleteById(entreprise.getId());
        return true;
    }
}
