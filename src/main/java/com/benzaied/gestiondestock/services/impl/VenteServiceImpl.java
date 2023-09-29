package com.benzaied.gestiondestock.services.impl;

import com.benzaied.gestiondestock.Repository.ArticleRepository;
import com.benzaied.gestiondestock.Repository.LigneVenteRepository;
import com.benzaied.gestiondestock.Repository.VenteRepository;
import com.benzaied.gestiondestock.dto.*;
import com.benzaied.gestiondestock.exception.EntityNotFoundException;
import com.benzaied.gestiondestock.exception.ErrorCodes;
import com.benzaied.gestiondestock.exception.InvalidEntityException;
import com.benzaied.gestiondestock.exception.InvalidOperationException;
import com.benzaied.gestiondestock.model.*;
import com.benzaied.gestiondestock.services.ArticleService;
import com.benzaied.gestiondestock.services.CategorieService;
import com.benzaied.gestiondestock.services.MvmntStckService;
import com.benzaied.gestiondestock.services.VenteService;
import com.benzaied.gestiondestock.validator.ArticleValidator;
import com.benzaied.gestiondestock.validator.VenteValidator;
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
public class VenteServiceImpl implements VenteService {

    private VenteRepository venteRepository;
    private LigneVenteRepository ligneVenteRepository;
    private ArticleRepository articleRepository;
    private MvmntStckService mvmntStckService;
    private ArticleService articleService;
    private CategorieService categorieService;

    @Autowired
    public VenteServiceImpl(VenteRepository venteRepository, LigneVenteRepository ligneVenteRepository, ArticleRepository articleRepository, MvmntStckService mvmntStckService, ArticleService articleService, CategorieService categorieService) {
        this.venteRepository = venteRepository;
        this.ligneVenteRepository = ligneVenteRepository;
        this.articleRepository = articleRepository;
        this.mvmntStckService = mvmntStckService;
        this.articleService = articleService;
        this.categorieService = categorieService;
    }

    @Override
    public VenteDto save(VenteDto venteDto) {
        List<String> errors = VenteValidator.validate(venteDto);
        if (!errors.isEmpty()) {
            log.error("La vente n'est pas valide {}", venteDto);
            throw new InvalidEntityException("La vente n'est pas valide", ErrorCodes.VENTE_NOT_VALID, errors);
        }
        List<String> articleErrors = new ArrayList<>();
        if (venteDto.getLigneVentes() != null) {
            venteDto.getLigneVentes().forEach(ligneVenteDto -> {
                if (ligneVenteDto.getArticle() != null) {
                    Optional<Article> article = articleRepository.findById(ligneVenteDto.getArticle().getId());
                    if (article.isEmpty()) {
                        articleErrors.add("L'article avec l'ID " + ligneVenteDto.getArticle().getId() + " n'existe pas");
                    }
                } else {
                    articleErrors.add("Impossible d'enregistrer les lignes de vente avec un article null");
                }
            });
        }
        if (!articleErrors.isEmpty()) {
            log.warn("Quelques articles ne figurent pas dans la BD , {}",articleErrors);
            throw new InvalidEntityException("Article n'existe pas dans la BD", ErrorCodes.ARTICLE_NOT_FOUND, articleErrors);
        }
        Vente vente = VenteDto.toEntity(venteDto);
        Vente savedVente = venteRepository.save(vente);
        if (venteDto.getLigneVentes() != null) {
            venteDto.getLigneVentes().forEach(ligneVenteDto -> {
                LigneVente ligneVente = LigneVenteDto.toEntity(ligneVenteDto);
                ligneVente.setVente(savedVente);
                ligneVenteRepository.save(ligneVente);
                updateMvmntStck(ligneVente.getId());
            });
        }

        return VenteDto.fromEntity(savedVente);
    }

    @Override
    public VenteDto findById(Integer id) {
        if (id == null) {
            log.error("L'ID de vente est non valide");
            return null;
        }
        Optional<Vente> venteOptional = venteRepository.findById(id);
        if (venteOptional.isEmpty()) {
            throw new EntityNotFoundException("Vente with id " + id + " not found", ErrorCodes.VENTE_NOT_FOUND);
        }
        return VenteDto.fromEntity(venteOptional.get());
    }

    @Override
    public VenteDto findByCodeVente(String codeVente) {
        if (!StringUtils.hasLength(codeVente)) {
            log.error("Le code de vente est invalide");
            return null;
        }
        Optional<Vente> vente = venteRepository.findByCodeVente(codeVente);
        if (vente.isEmpty()) {
            throw new EntityNotFoundException("Vente with code " + codeVente + " not found", ErrorCodes.VENTE_NOT_FOUND);
        }
        return VenteDto.fromEntity(vente.get());
    }

    @Override
    public List<VenteDto> findAll() {
        List<Vente> ventes = venteRepository.findAll();
        return ventes.stream()
                .map(VenteDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public VenteDto updateEtatCommande(Integer idVente, EtatCommande etatVente) {
        if (idVente == null){
            throw new InvalidOperationException("Impossible de modifier l'etat de la vente avec ID null",ErrorCodes.VENTE_NON_MODIFIABLE);
        }
        if (!StringUtils.hasLength(String.valueOf(etatVente))){
            log.error("L etat de vente est invalid");
            throw new InvalidOperationException("Impossible de modifier l'etat de la vente avec etatVente null",ErrorCodes.VENTE_NON_MODIFIABLE);
        }

        VenteDto venteDto = findById(idVente);

        if (venteDto.isCommandeLivree()){
            throw new InvalidOperationException("Cette commande est déja livree",ErrorCodes.VENTE_NON_MODIFIABLE);
        }

        venteDto.setEtatCommande(etatVente);

        Vente savedVente = venteRepository.save(VenteDto.toEntity(venteDto));

        if (venteDto.isCommandeLivree()){
            updateMvmntStck(idVente);
        }

        return VenteDto.fromEntity(savedVente);
    }

    @Override
    public VenteDto updateQuantiteCommande(Integer idVente, Integer idLigneVente, BigDecimal quantite) {
        if (idVente == null){
            throw new InvalidOperationException("Impossible de modifier une vente avec ID null",ErrorCodes.VENTE_NON_MODIFIABLE);
        }
        if (idLigneVente == null){
            log.error("Ligne Vente is null");
            throw new InvalidOperationException("Impossible de modifier une ligne de vente avec id null",ErrorCodes.VENTE_NON_MODIFIABLE);
        }
        if (quantite == null || quantite.compareTo(BigDecimal.ZERO)==0){
            log.error("La quantite doit etre valide");
            throw new InvalidOperationException("Impossible de modifier une ligne de vente avec une quantite non valid",ErrorCodes.VENTE_NON_MODIFIABLE);
        }

        VenteDto venteDto = findById(idVente);

        if (venteDto.isCommandeLivree()){
            throw new InvalidOperationException("Cette vente est déja livree",ErrorCodes.VENTE_NON_MODIFIABLE);
        }

        Optional<LigneVente> ligneVenteOptional = ligneVenteRepository.findById(idLigneVente);

        if (ligneVenteOptional.isEmpty()){
            throw new EntityNotFoundException("Cette ligne de vente avec l'ID "+idLigneVente+" n'existe pas",ErrorCodes.LIGNE_VENTE_NOT_FOUND);
        }

        LigneVente ligneVente = ligneVenteOptional.get();

        ligneVente.setQuantite(quantite);

        ligneVenteRepository.save(ligneVente);

        return venteDto;
    }

    @Override
    public VenteDto updateArticle(Integer idVente, Integer idLigneVente, Integer newIdArticle) {
        if (idVente == null){
            throw new InvalidOperationException("Impossible de modifier une vente avec ID null",ErrorCodes.VENTE_NON_MODIFIABLE);
        }
        if (idLigneVente == null){
            log.error("Ligne Vente is null");
            throw new InvalidOperationException("Impossible de modifier une ligne de vente avec id null",ErrorCodes.VENTE_NON_MODIFIABLE);
        }
        if (newIdArticle == null){
            log.error("Le nouveau article a un ID null");
            throw new InvalidOperationException("Impossible de modifier un article non valid",ErrorCodes.ARTICLE_NOT_VALID);
        }

        VenteDto venteDto = findById(idVente);

        if (venteDto.isCommandeLivree()){
            throw new InvalidOperationException("Cette vente est déja livree",ErrorCodes.VENTE_NON_MODIFIABLE);
        }

        Optional<LigneVente> ligneVenteOptional = ligneVenteRepository.findById(idLigneVente);

        if (ligneVenteOptional.isEmpty()){
            throw new EntityNotFoundException("Cette ligne de vente avec l'ID "+idLigneVente+" n'existe pas",ErrorCodes.LIGNE_VENTE_NOT_FOUND);
        }

        Optional<Article> articleOptional=articleRepository.findById(newIdArticle);
        if(articleOptional.isEmpty()){
            throw new EntityNotFoundException("Cet article ID "+newIdArticle+" n'existe pas",ErrorCodes.ARTICLE_NOT_FOUND);
        }
        List<String> errors = ArticleValidator.validate(ArticleDto.fromEntity(articleOptional.get()));

        if (!errors.isEmpty()){
            throw new InvalidEntityException("Article invalid",ErrorCodes.ARTICLE_NOT_VALID,errors);
        }

        LigneVente ligneVente = ligneVenteOptional.get();
        ligneVente.setArticle(articleOptional.get());
        ligneVenteRepository.save(ligneVente);

        return venteDto;
    }

    @Override
    public VenteDto deleteArticle(Integer idVente, Integer idLigneVente) {
        if (idVente == null){
            throw new InvalidOperationException("Impossible de modifier une vente avec ID null",ErrorCodes.VENTE_NON_MODIFIABLE);
        }
        if (idLigneVente == null){
            log.error("Ligne Vente is null");
            throw new InvalidOperationException("Impossible de modifier une ligne de vente avec id null",ErrorCodes.VENTE_NON_MODIFIABLE);
        }
        VenteDto venteDto = findById(idVente);

        if (venteDto.isCommandeLivree()){
            throw new InvalidOperationException("Cette vente est déja livree",ErrorCodes.VENTE_NON_MODIFIABLE);
        }
        Optional<LigneVente> ligneVenteOptional = ligneVenteRepository.findById(idLigneVente);

        if (ligneVenteOptional.isEmpty()){
            throw new EntityNotFoundException("Cette ligne de vente avec l'ID "+idLigneVente+" n'existe pas",ErrorCodes.LIGNE_VENTE_NOT_FOUND);
        }
        ligneVenteRepository.deleteById(idLigneVente);

        return venteDto;
    }

    @Override
    public List<LigneVenteDto> findAllLigneVentesByVenteId(Integer idVente) {
        return ligneVenteRepository.findAllByVenteId(idVente).stream()
                .map(LigneVenteDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDto> getAllArticles() {
        return articleService.findByTypeClientForPassager();
    }

    @Override
    public List<ArticleDto> getAllArticlesByCategorie(Integer idCategorie) {
        return articleService.getArticlesForPassagerClientByTypeAndCategory(TypeClient.PASSAGER,idCategorie);
    }

    @Override
    public boolean delete(Integer id) {
        if (!venteRepository.existsById(id) || id == null) {
            log.error("Vente with id " + id + " not found");
        }
        Vente vente = venteRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Vente not found !!")
        );
        List<LigneVente> ligneVentes = ligneVenteRepository.findAllByVenteId(id);
        if (!ligneVentes.isEmpty()){
            throw new InvalidOperationException("Impossible de supprimer une vente déja utiliser",ErrorCodes.VENTE_ALREADY_IN_USE);
        }


        venteRepository.deleteById(id);
        return true;
    }

    public void updateMvmntStck(Integer idVente){
        List<LigneVente> ligneVentes = ligneVenteRepository.findAllByVenteId(idVente);
        ligneVentes.forEach(lig -> {
            MvmntStckDto mvmntStckDto = MvmntStckDto.builder()
                    .article(ArticleDto.fromEntity(lig.getArticle()))
                    .dateMvmnt(Instant.now())
                    .typeMvmntStck(TypedeMvmntStck.SORTIE)
                    .sourceMvmntStck(SourceMvmntStck.VENTE)
                    .quantite(lig.getQuantite())
                    .idEntreprise(lig.getIdEntreprise())
                    .build();
            mvmntStckService.sortieStock(mvmntStckDto);
        });
    }

}
