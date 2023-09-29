package com.benzaied.gestiondestock.services;

import com.benzaied.gestiondestock.dto.EntrepriseDto;

import java.util.List;

public interface EntrepriseService {

    EntrepriseDto save(EntrepriseDto entrepriseDto);
    EntrepriseDto findById(Integer id);
    EntrepriseDto findByNomIgnoreCase(String nom);
    List<EntrepriseDto> findAll();
    boolean delete(Integer id);
    boolean deleteByNom(String nom);

}
