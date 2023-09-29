package com.benzaied.gestiondestock.services;

import com.benzaied.gestiondestock.dto.LigneCommandeClientDto;

import java.util.List;

public interface LigneCommandeClientService {

    LigneCommandeClientDto save(LigneCommandeClientDto ligneCommandeClientDto);
    LigneCommandeClientDto findById(Integer id);
    List<LigneCommandeClientDto> findAll();
    boolean delete(Integer id);
}

