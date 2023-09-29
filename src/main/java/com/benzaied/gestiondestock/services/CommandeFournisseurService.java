package com.benzaied.gestiondestock.services;

import com.benzaied.gestiondestock.dto.CommandeClientDto;
import com.benzaied.gestiondestock.dto.CommandeFournisseurDto;
import com.benzaied.gestiondestock.dto.LigneCommandeFournisseurDto;
import com.benzaied.gestiondestock.model.EtatCommande;

import java.math.BigDecimal;
import java.util.List;

public interface CommandeFournisseurService {

    CommandeFournisseurDto save(CommandeFournisseurDto commandeFournisseurDto);
    CommandeFournisseurDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande);
    CommandeFournisseurDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite);
    CommandeFournisseurDto updateFournisseur(Integer idCommande, Integer idFournisseur);
    CommandeFournisseurDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer newIdArticle);
    CommandeFournisseurDto deleteArticle(Integer idCommande, Integer idLigneCommande);
    CommandeFournisseurDto findById(Integer id);
    CommandeFournisseurDto findByCodeCF(String codeCF);
    List<CommandeFournisseurDto> findAll();
    List<LigneCommandeFournisseurDto> findAllLigneCommandeFournisseursByCommandeFournisseurId(Integer idCommande);
    boolean delete(Integer id);
}