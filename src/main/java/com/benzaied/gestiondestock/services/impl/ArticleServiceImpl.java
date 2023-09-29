package com.benzaied.gestiondestock.services.impl;

import com.benzaied.gestiondestock.Repository.ArticleRepository;
import com.benzaied.gestiondestock.Repository.LigneCommandeClientRepository;
import com.benzaied.gestiondestock.Repository.LigneCommandeFournisseurRepository;
import com.benzaied.gestiondestock.Repository.LigneVenteRepository;
import com.benzaied.gestiondestock.dto.*;
import com.benzaied.gestiondestock.exception.EntityNotFoundException;
import com.benzaied.gestiondestock.exception.ErrorCodes;
import com.benzaied.gestiondestock.exception.InvalidEntityException;
import com.benzaied.gestiondestock.exception.InvalidOperationException;
import com.benzaied.gestiondestock.model.*;
import com.benzaied.gestiondestock.services.ArticleService;
import com.benzaied.gestiondestock.services.CategorieService;
import com.benzaied.gestiondestock.validator.ArticleValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    private ArticleRepository articleRepository;
    private LigneVenteRepository ligneVenteRepository;
    private LigneCommandeClientRepository ligneCommandeClientRepository;
    private LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository;
    private CategorieService categorieService;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, LigneVenteRepository ligneVenteRepository, LigneCommandeClientRepository ligneCommandeClientRepository, LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository, CategorieService categorieService){
        this.articleRepository=articleRepository;
        this.ligneVenteRepository = ligneVenteRepository;
        this.ligneCommandeClientRepository = ligneCommandeClientRepository;
        this.ligneCommandeFournisseurRepository = ligneCommandeFournisseurRepository;
        this.categorieService = categorieService;
    }

    @Override
    public ArticleDto save(ArticleDto articleDto) {
        List<String> errors = ArticleValidator.validate(articleDto);
        if(!errors.isEmpty()){
            log.error("L'article n'est pas valide {}",articleDto);
            throw new InvalidEntityException("L'article n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }
        return ArticleDto.fromEntity(articleRepository.save(ArticleDto.toEntity(articleDto)));
    }

    @Override
    public ArticleDto findById(Integer id) {
        if (id == null){
            log.error("L'ID d'article est non valide");
            return null;
        }
        Optional<Article> article=articleRepository.findById(id);
        ArticleDto articleDto=ArticleDto.fromEntity(article.get());
        return Optional.of(articleDto).orElseThrow(() -> new EntityNotFoundException("Aucun article avec l'ID = "+ id +" n'est trouve dans la BD",ErrorCodes.ARTICLE_NOT_FOUND));
    }

    @Override
    public ArticleDto findByNomArticle(String nomArticle) {
        if(!StringUtils.hasLength(nomArticle)){
            log.error("Le nom d'article est invalid");
            return null;
        }
        Optional<Article> article=articleRepository.findByNomArticleIgnoreCase(nomArticle);
        ArticleDto articleDto=ArticleDto.fromEntity(article.get());
        return Optional.of(articleDto).orElseThrow(() -> new EntityNotFoundException("Aucun article avec le nom = "+ nomArticle +" n'est trouve dans la BD",ErrorCodes.ARTICLE_NOT_FOUND));
    }

    @Override
    public List<ArticleDto> findByTypeClient(ClientDto clientDto) {
        if (clientDto.getTypeClient() == null) {
            log.error("Le type de client est invalide");
            throw new IllegalArgumentException("Le type de client est invalide");
        }

        List<Article> articles = articleRepository.findAll();

        List<Article> filteredArticles = articles.stream()
                .filter(article -> {
                    List<TypeClient> typeClientsAutorise = article.getTypeClientsAutorise();
                    return typeClientsAutorise != null && typeClientsAutorise.contains(clientDto.getTypeClient());
                })
                .collect(Collectors.toList());

        if (filteredArticles.isEmpty()) {
            throw new EntityNotFoundException("Aucun article avec le type de client = " + clientDto.getTypeClient() + " n'est trouvé dans la BD", ErrorCodes.ARTICLE_NOT_FOUND);
        }

        List<ArticleDto> articleDtos = filteredArticles.stream()
                .map(ArticleDto::fromEntity) // Assuming ArticleDto.fromEntity() is a static method of ArticleDto
                .collect(Collectors.toList());

        return articleDtos;
    }

    @Override
    public List<ArticleDto> findByTypeClientForPassager() {

        List<Article> articles = articleRepository.findAll();

        List<Article> filteredArticles = articles.stream()
                .filter(article -> {
                    List<TypeClient> typeClientsAutorise = article.getTypeClientsAutorise();
                    return typeClientsAutorise != null && typeClientsAutorise.contains(TypeClient.PASSAGER);
                })
                .collect(Collectors.toList());

        if (filteredArticles.isEmpty()) {
            throw new EntityNotFoundException("Aucun article avec le type de client = Passager n'est trouvé dans la BD", ErrorCodes.ARTICLE_NOT_FOUND);
        }

        List<ArticleDto> articleDtos = filteredArticles.stream()
                .map(ArticleDto::fromEntity) // Assuming ArticleDto.fromEntity() is a static method of ArticleDto
                .collect(Collectors.toList());

        return articleDtos;
    }

    @Override
    public ArticleDto findByCodeArticle(String codeArticle) {
        if(!StringUtils.hasLength(codeArticle)){
            log.error("Le code d'article est invalid");
            return null;
        }
        Optional<Article> article=articleRepository.findByCodeArticle(codeArticle);
        ArticleDto articleDto=ArticleDto.fromEntity(article.get());
        return Optional.of(articleDto).orElseThrow(() -> new EntityNotFoundException("Aucun article avec le code = "+ codeArticle +" n'est trouve dans la BD",ErrorCodes.ARTICLE_NOT_FOUND));
    }

    @Override
    public List<ArticleDto> findAll() {
        return articleRepository.findAll().stream()
                .map(ArticleDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LigneVenteDto> findHistoriqueVentes(Integer idArticle){
        return ligneVenteRepository.findAllByArticleId(idArticle).stream()
                .map(LigneVenteDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LigneCommandeClientDto> findHistoriqueCommandeClient(Integer idArticle){
        return ligneCommandeClientRepository.findAllByArticleId(idArticle).stream()
                .map(LigneCommandeClientDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(Integer idArticle){
        return ligneCommandeFournisseurRepository.findAllByArticleId(idArticle).stream()
                .map(LigneCommandeFournisseurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDto> findAllArticleByIdCategory(Integer idCategory){
        return articleRepository.findAllByCategorieId(idCategory).stream()
                .map(ArticleDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDto> getArticlesForClientByTypeAndCategory(ClientDto clientDto, Integer idCategorie) {
        Client client = ClientDto.toEntity(clientDto);
        CategorieDto categorieDto = categorieService.findById(idCategorie);
        if (categorieDto == null){
            throw new EntityNotFoundException("Aucune categorie avec ce nom",ErrorCodes.CATEGORY_NOT_FOUND);
        }

        List<Article> articles = articleRepository.findAllByCategorieId(idCategorie);

        List<Article> filteredArticles = articles.stream()
                .filter(article -> {
                    List<TypeClient> typeClientsAutorise = article.getTypeClientsAutorise();
                    return typeClientsAutorise != null && typeClientsAutorise.contains(clientDto.getTypeClient());
                })
                .collect(Collectors.toList());

        return filteredArticles.stream()
                .map(ArticleDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDto> getArticlesForPassagerClientByTypeAndCategory(TypeClient typeClient, Integer idCategorie) {
        CategorieDto categorieDto = categorieService.findById(idCategorie);
        if (categorieDto == null){
            throw new EntityNotFoundException("Aucune categorie avec ce nom",ErrorCodes.CATEGORY_NOT_FOUND);
        }

        List<Article> articles = articleRepository.findAllByCategorieId(idCategorie);

        List<Article> filteredArticles = articles.stream()
                .filter(article -> {
                    List<TypeClient> typeClientsAutorise = article.getTypeClientsAutorise();
                    return typeClientsAutorise != null && typeClientsAutorise.contains(typeClient);
                })
                .collect(Collectors.toList());

        return filteredArticles.stream()
                .map(ArticleDto::fromEntity)
                .collect(Collectors.toList());

    }

    @Override
    public boolean delete(Integer id) {
        if (id == null){
            log.error("L'ID d'article est non valide");
            return false;
        }
        Article article = articleRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Article not found !!")
        );
        List<LigneCommandeClient> ligneCommandeClients=ligneCommandeClientRepository.findAllByArticleId(id);
        if (!ligneCommandeClients.isEmpty()){
            throw new InvalidOperationException("Impossible de supprimer un article déja utilise dans une commande client",ErrorCodes.ARTICLE_ALREADY_IN_USE);
        }
        List<LigneCommandeFournisseur> ligneCommandeFournisseurs=ligneCommandeFournisseurRepository.findAllByArticleId(id);
        if (!ligneCommandeFournisseurs.isEmpty()){
            throw new InvalidOperationException("Impossible de supprimer un article déja utilise dans une commande fournisseur",ErrorCodes.ARTICLE_ALREADY_IN_USE);
        }
        List<LigneVente> ligneVentes=ligneVenteRepository.findAllByArticleId(id);
        if (!ligneVentes.isEmpty()){
            throw new InvalidOperationException("Impossible de supprimer un article déja utilise dans une vente",ErrorCodes.ARTICLE_ALREADY_IN_USE);
        }


        articleRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean deleteByNomArticleIgnoreCase(String nomArticle) {
        if(!StringUtils.hasLength(nomArticle)){
            log.error("Le nom d'article est invalid");
            return false;
        }
        Article article = articleRepository.findByNomArticleIgnoreCase(nomArticle).orElseThrow(
                () -> new EntityNotFoundException("Article not found !!")
        );
        List<LigneCommandeClient> ligneCommandeClients=ligneCommandeClientRepository.findAllByArticleId(article.getId());
        if (!ligneCommandeClients.isEmpty()){
            throw new InvalidOperationException("Impossible de supprimer un article déja utilise dans une commande client",ErrorCodes.ARTICLE_ALREADY_IN_USE);
        }
        List<LigneCommandeFournisseur> ligneCommandeFournisseurs=ligneCommandeFournisseurRepository.findAllByArticleId(article.getId());
        if (!ligneCommandeFournisseurs.isEmpty()){
            throw new InvalidOperationException("Impossible de supprimer un article déja utilise dans une commande fournisseur",ErrorCodes.ARTICLE_ALREADY_IN_USE);
        }
        List<LigneVente> ligneVentes=ligneVenteRepository.findAllByArticleId(article.getId());
        if (!ligneVentes.isEmpty()){
            throw new InvalidOperationException("Impossible de supprimer un article déja utilise dans une vente",ErrorCodes.ARTICLE_ALREADY_IN_USE);
        }


        articleRepository.deleteById(article.getId());
        return true;
    }

    /*@Override
    public List<ArticleDto> findArticlesForPassengerClients() {
        List<Article> articles = articleRepository.findByTypeClientsAutoriseContaining(TypeClient.PASSAGER);

        if (articles.isEmpty()) {
            throw new EntityNotFoundException("Aucun article avec le type de client autorisé (PASSAGER) n'est trouvé", ErrorCodes.ARTICLE_NOT_FOUND);
        }

        List<ArticleDto> articleDtos = articles.stream()
                .map(ArticleDto::fromEntity) // Assuming ArticleDto.fromEntity() is a static method of ArticleDto
                .collect(Collectors.toList());

        return articleDtos;
    }*/


}
