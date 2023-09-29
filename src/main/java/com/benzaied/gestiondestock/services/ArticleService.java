package com.benzaied.gestiondestock.services;

import com.benzaied.gestiondestock.dto.*;
import com.benzaied.gestiondestock.model.Article;
import com.benzaied.gestiondestock.model.TypeClient;

import java.util.List;

public interface ArticleService {

    ArticleDto save(ArticleDto articleDto);
    ArticleDto findById(Integer id);
    ArticleDto findByNomArticle(String nomArticle);
    List<ArticleDto> findByTypeClient(ClientDto clientDto);
    List<ArticleDto> findByTypeClientForPassager();
    ArticleDto findByCodeArticle(String codeArticle);
    List<ArticleDto> findAll();
    List<LigneVenteDto> findHistoriqueVentes(Integer idArticle);
    List<LigneCommandeClientDto> findHistoriqueCommandeClient(Integer idArticle);
    List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(Integer idArticle);
    List<ArticleDto> findAllArticleByIdCategory(Integer idCategory);
    List<ArticleDto> getArticlesForClientByTypeAndCategory(ClientDto clientDto, Integer idCategorie);
    List<ArticleDto> getArticlesForPassagerClientByTypeAndCategory(TypeClient typeClient, Integer idCategorie);
    boolean delete(Integer id);
    boolean deleteByNomArticleIgnoreCase(String nomArticle);
    //List<ArticleDto> findArticlesForPassengerClients();

}
