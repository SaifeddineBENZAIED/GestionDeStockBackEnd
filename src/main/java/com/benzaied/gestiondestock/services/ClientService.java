package com.benzaied.gestiondestock.services;

import com.benzaied.gestiondestock.dto.*;
import com.benzaied.gestiondestock.model.TypeClient;

import java.util.List;

public interface ClientService {

    ClientDto save(ClientDto clientDto);
    ClientDto findById(Integer id);
    ClientDto findByNomAndPrenomIgnoreCase(String nom, String prenom);
    ClientDto findByNomIgnoreCase(String nom);
    ClientDto findByPrenomIgnoreCase(String nom);
    List<ClientDto> findAll();
    List<ArticleDto> findAllArticlesByTypeClient(ClientDto clientDto);
    List<ArticleDto> getArticlesForClientByTypeAndCategory(ClientDto clientDto, Integer idCategorie);
    boolean delete(Integer id);
    ClientDto findByEmail(String email);
    ClientDto changerMotDePasse(ChangerMotDePasseUtilisateurDto dto);

}
