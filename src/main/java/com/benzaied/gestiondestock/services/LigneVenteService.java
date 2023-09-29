package com.benzaied.gestiondestock.services;

import com.benzaied.gestiondestock.dto.LigneVenteDto;

import java.util.List;

public interface LigneVenteService {

    LigneVenteDto save(LigneVenteDto ligneVenteDto);
    LigneVenteDto findById(Integer id);
    List<LigneVenteDto> findAll();
    boolean delete(Integer id);
}

