package com.benzaied.gestiondestock.services;

import com.benzaied.gestiondestock.dto.ChangerMotDePasseUtilisateurDto;
import com.benzaied.gestiondestock.dto.UtilisateurDto;

import java.util.List;

public interface UtilisateurService {

    UtilisateurDto save(UtilisateurDto utilisateurDto);
    UtilisateurDto findById(Integer id);
    UtilisateurDto findByNomAndPrenomIgnoreCase(String nom, String prenom);
    UtilisateurDto findByNomIgnoreCase(String nom);
    UtilisateurDto findByPrenomIgnoreCase(String prenom);
    List<UtilisateurDto> findAll();
    boolean delete(Integer id);
    UtilisateurDto findByEmail(String email);
    UtilisateurDto changerMotDePasse(ChangerMotDePasseUtilisateurDto dto);
}
