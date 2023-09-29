package com.benzaied.gestiondestock.services;

import com.benzaied.gestiondestock.dto.LigneCommandeFournisseurDto;

import java.util.List;

public interface LigneCommandeFournisseurService {

    LigneCommandeFournisseurDto save(LigneCommandeFournisseurDto ligneCommandeFournisseurDto);
    LigneCommandeFournisseurDto findById(Integer id);
    List<LigneCommandeFournisseurDto> findAll();
    boolean delete(Integer id);
}

