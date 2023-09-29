package com.benzaied.gestiondestock.services;

import com.benzaied.gestiondestock.dto.CategorieDto;

import java.util.List;

public interface CategorieService {

    CategorieDto save(CategorieDto categorieDto);

    CategorieDto findById(Integer id);

    CategorieDto findByCodeCategorie(String codeCategorie);

    CategorieDto findByNomCategorie(String nomCategorie);

    List<CategorieDto> findAll();

    boolean delete(Integer id);

}
