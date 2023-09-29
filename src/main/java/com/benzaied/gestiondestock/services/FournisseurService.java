package com.benzaied.gestiondestock.services;

import com.benzaied.gestiondestock.dto.ArticleDto;
import com.benzaied.gestiondestock.dto.FournisseurDto;
import com.benzaied.gestiondestock.model.Fournisseur;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface FournisseurService {

    FournisseurDto save(FournisseurDto fournisseurDto);
    BigDecimal getQuantiteTotaleFournieArticle(Integer fournisseurId, Integer idEntreprise, Integer articleId);
    Map<ArticleDto, BigDecimal> getQuantiteFournieParArticle(Fournisseur fournisseur);
    FournisseurDto findById(Integer id);
    FournisseurDto findByNomAndPrenomIgnoreCase(String nom, String prenom);
    FournisseurDto findByNomIgnoreCase(String nom);
    FournisseurDto findByPrenomIgnoreCase(String prenom);
    List<FournisseurDto> findAll();
    boolean delete(Integer id);

}
