package com.benzaied.gestiondestock.services.impl;

import com.benzaied.gestiondestock.Repository.ArticleRepository;
import com.benzaied.gestiondestock.Repository.CommandeFournisseurRepository;
import com.benzaied.gestiondestock.Repository.FournisseurRepository;
import com.benzaied.gestiondestock.Repository.LigneCommandeFournisseurRepository;
import com.benzaied.gestiondestock.dto.ArticleDto;
import com.benzaied.gestiondestock.dto.FournisseurDto;
import com.benzaied.gestiondestock.exception.EntityNotFoundException;
import com.benzaied.gestiondestock.exception.ErrorCodes;
import com.benzaied.gestiondestock.exception.InvalidEntityException;
import com.benzaied.gestiondestock.exception.InvalidOperationException;
import com.benzaied.gestiondestock.model.*;
import com.benzaied.gestiondestock.services.FournisseurService;
import com.benzaied.gestiondestock.validator.FournisseurValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FournisseurServiceImpl implements FournisseurService {

    private FournisseurRepository fournisseurRepository;
    private ArticleRepository articleRepository;
    private CommandeFournisseurRepository commandeFournisseurRepository;
    private LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository;

    @Autowired
    public FournisseurServiceImpl(FournisseurRepository fournisseurRepository, ArticleRepository articleRepository, CommandeFournisseurRepository commandeFournisseurRepository, LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository){
        this.fournisseurRepository = fournisseurRepository;
        this.articleRepository = articleRepository;
        this.commandeFournisseurRepository = commandeFournisseurRepository;
        this.ligneCommandeFournisseurRepository = ligneCommandeFournisseurRepository;
    }

    @Override
    public FournisseurDto save(FournisseurDto fournisseurDto) {
        List<String> errors = FournisseurValidator.validate(fournisseurDto);
        if (!errors.isEmpty()) {
            log.error("Le fournisseur n'est pas valide {}", fournisseurDto);
            throw new InvalidEntityException("Le fournisseur n'est pas valide", ErrorCodes.FOURNISSEUR_NOT_VALID, errors);
        }
        return FournisseurDto.fromEntity(fournisseurRepository.save(FournisseurDto.toEntity(fournisseurDto)));
    }

    @Override
    public BigDecimal getQuantiteTotaleFournieArticle(Integer fournisseurId, Integer idEntreprise, Integer articleId) {
        Fournisseur fournisseur = fournisseurRepository.findById(fournisseurId).orElse(null);
        Article article = articleRepository.findById(articleId).orElse(null);
        if (fournisseur != null && article != null) {
            return ArticleDto.getQuantiteTotaleFournieArticle(idEntreprise, fournisseur, article);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public Map<ArticleDto, BigDecimal> getQuantiteFournieParArticle(Fournisseur fournisseur) {
        List<LigneCommandeFournisseur> lignesCommandeFournisseur = ligneCommandeFournisseurRepository.findByCommandeFournisseur_Fournisseur(fournisseur);

        Map<ArticleDto, BigDecimal> quantitesParArticle = new HashMap<>();

        for (LigneCommandeFournisseur ligne : lignesCommandeFournisseur) {
            Article article = ligne.getArticle();
            BigDecimal quantite = ligne.getQuantite();

            quantitesParArticle.put(ArticleDto.fromEntity(article), quantitesParArticle.getOrDefault(article, BigDecimal.ZERO).add(quantite));
        }

        return quantitesParArticle;
    }

    @Override
    public FournisseurDto findById(Integer id) {
        if (id == null) {
            log.error("L'ID de fournisseur est non valide");
            return null;
        }
        Optional<Fournisseur> fournisseur = fournisseurRepository.findById(id);
        FournisseurDto fournisseurDto = FournisseurDto.fromEntity(fournisseur.get());
        return Optional.of(fournisseurDto).orElseThrow(() -> new EntityNotFoundException("Aucun fournisseur avec l'ID = " + id + " n'est trouvé dans la BD", ErrorCodes.FOURNISSEUR_NOT_FOUND));
    }

    @Override
    public FournisseurDto findByNomAndPrenomIgnoreCase(String nom, String prenom) {
        if (!StringUtils.hasLength(nom) || !StringUtils.hasLength(prenom)) {
            log.error("Le nom ou le prénom de fournisseur est invalide");
            return null;
        }
        Optional<Fournisseur> fournisseur = fournisseurRepository.findByNomAndPrenomIgnoreCase(nom, prenom);
        FournisseurDto fournisseurDto = FournisseurDto.fromEntity(fournisseur.get());
        return Optional.of(fournisseurDto).orElseThrow(() -> new EntityNotFoundException("Aucun fournisseur avec le nom = " + nom + " et le prénom = " + prenom + " n'est trouvé dans la BD", ErrorCodes.FOURNISSEUR_NOT_FOUND));
    }

    @Override
    public FournisseurDto findByNomIgnoreCase(String nom) {
        if (!StringUtils.hasLength(nom)) {
            log.error("Le nom de fournisseur est invalide");
            return null;
        }
        Optional<Fournisseur> fournisseur = fournisseurRepository.findByNomIgnoreCase(nom);
        FournisseurDto fournisseurDto = FournisseurDto.fromEntity(fournisseur.get());
        return Optional.of(fournisseurDto).orElseThrow(() -> new EntityNotFoundException("Aucun fournisseur avec le nom = " + nom + " n'est trouvé dans la BD", ErrorCodes.FOURNISSEUR_NOT_FOUND));
    }

    @Override
    public FournisseurDto findByPrenomIgnoreCase(String prenom) {
        if (!StringUtils.hasLength(prenom)) {
            log.error("Le prenom de fournisseur est invalide");
            return null;
        }
        Optional<Fournisseur> fournisseur = fournisseurRepository.findByPrenomIgnoreCase(prenom);
        FournisseurDto fournisseurDto = FournisseurDto.fromEntity(fournisseur.get());
        return Optional.of(fournisseurDto).orElseThrow(() -> new EntityNotFoundException("Aucun fournisseur avec le prenom = " + prenom + " n'est trouvé dans la BD", ErrorCodes.FOURNISSEUR_NOT_FOUND));
    }

    @Override
    public List<FournisseurDto> findAll() {
        return fournisseurRepository.findAll().stream()
                .map(FournisseurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(Integer id) {
        if (id == null) {
            log.error("L'ID de fournisseur est non valide");
            return false;
        }
        Fournisseur fournisseur = fournisseurRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Fournisseur not found !!")
        );
        List<CommandeFournisseur> commandeFournisseurs = commandeFournisseurRepository.findAllByFournisseurId(id);
        if (!commandeFournisseurs.isEmpty()){
            throw new InvalidOperationException("Impossible de supprimer ce fournisseur car il à déja passer des commandes",ErrorCodes.FOURNISSEUR_ALREADY_IN_USE);
        }


        fournisseurRepository.deleteById(id);
        return true;
    }

}
