package com.benzaied.gestiondestock.services;

import com.benzaied.gestiondestock.dto.CommandeClientDto;
import com.benzaied.gestiondestock.dto.LigneCommandeClientDto;
import com.benzaied.gestiondestock.model.EtatCommande;

import java.math.BigDecimal;
import java.util.List;

public interface CommandeClientService {

    CommandeClientDto save(CommandeClientDto commandeClientDto);
    CommandeClientDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande);
    CommandeClientDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite);
    CommandeClientDto updateClient(Integer idCommande, Integer idClient);
    CommandeClientDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer newIdArticle);
    CommandeClientDto deleteArticle(Integer idCommande, Integer idLigneCommande);
    CommandeClientDto findById(Integer id);
    CommandeClientDto findByCodeCC(String codeCC);
    List<CommandeClientDto> findAll();
    List<LigneCommandeClientDto> findAllLigneCommandeClientsByCommandeClientId(Integer idCommande);
    boolean delete(Integer id);
}

