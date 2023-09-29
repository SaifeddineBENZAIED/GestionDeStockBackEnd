package com.benzaied.gestiondestock.services;

import com.benzaied.gestiondestock.dto.*;
import com.benzaied.gestiondestock.model.EtatCommande;

import java.math.BigDecimal;
import java.util.List;

public interface VenteService {

    VenteDto save(VenteDto venteDto);
    VenteDto updateEtatCommande(Integer idVente, EtatCommande etatVente);
    VenteDto updateQuantiteCommande(Integer idVente, Integer idLigneVente, BigDecimal quantite);
    VenteDto updateArticle(Integer idVente, Integer idLigneVente, Integer newIdArticle);
    VenteDto deleteArticle(Integer idVente, Integer idLigneVente);
    VenteDto findById(Integer id);
    VenteDto findByCodeVente(String codeVente);
    List<VenteDto> findAll();
    List<LigneVenteDto> findAllLigneVentesByVenteId(Integer idVente);
    List<ArticleDto> getAllArticles();
    List<ArticleDto> getAllArticlesByCategorie(Integer idCategorie);
    boolean delete(Integer id);
}
