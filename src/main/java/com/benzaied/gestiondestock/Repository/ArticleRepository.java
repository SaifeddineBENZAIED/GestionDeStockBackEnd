package com.benzaied.gestiondestock.Repository;

import com.benzaied.gestiondestock.model.Article;
import com.benzaied.gestiondestock.model.Categorie;
import com.benzaied.gestiondestock.model.TypeClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

    Optional<Article> findByNomArticleIgnoreCase(String nomArticle);
    //List<Article> findByTypeClientsAutorise(TypeClient typeClient);
    Optional<Article> findByCodeArticle(String codeArticle);
    List<Article> findAllByCategorieId(Integer idCategorie);
    List<Article> findAllByIdEntreprise(Integer idEntreprise);
    void deleteByNomArticleIgnoreCase(String nomArticle);
    //List<Article> findByTypeClientsAutoriseAndCategorie(TypeClient typeClient, Categorie categorie);
    //List<Article> findByTypeClientsAutoriseContaining(TypeClient typeClient);

    List<Article> findByTypeClientsAutoriseIn(List<TypeClient> typeClients);
    List<Article> findByTypeClientsAutoriseInAndCategorie_NomCategorieAndCategorie_NomCategorieIsNotNull(List<TypeClient> typeClients, String nomCategorie);

}
