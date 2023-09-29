package com.benzaied.gestiondestock.services.impl;

import com.benzaied.gestiondestock.Repository.LigneVenteRepository;
import com.benzaied.gestiondestock.dto.LigneVenteDto;
import com.benzaied.gestiondestock.exception.EntityNotFoundException;
import com.benzaied.gestiondestock.model.LigneCommandeClient;
import com.benzaied.gestiondestock.model.LigneVente;
import com.benzaied.gestiondestock.services.LigneVenteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LigneVenteServiceImpl implements LigneVenteService {

    private final LigneVenteRepository ligneVenteRepository;

    @Autowired
    public LigneVenteServiceImpl(LigneVenteRepository ligneVenteRepository) {
        this.ligneVenteRepository = ligneVenteRepository;
    }

    @Override
    public LigneVenteDto save(LigneVenteDto ligneVenteDto) {
        LigneVente ligneVente = LigneVenteDto.toEntity(ligneVenteDto);
        LigneVente savedLigneVente = ligneVenteRepository.save(ligneVente);
        return LigneVenteDto.fromEntity(savedLigneVente);
    }

    @Override
    public LigneVenteDto findById(Integer id) {
        return ligneVenteRepository.findById(id)
                .map(LigneVenteDto::fromEntity)
                .orElse(null);
    }

    @Override
    public List<LigneVenteDto> findAll() {
        List<LigneVente> lignesVente = ligneVenteRepository.findAll();
        return lignesVente.stream()
                .map(LigneVenteDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(Integer id) {
        if (id == null) {
            log.error("L'ID de Ligne Vente est non valide");
            return false;
        }
        LigneVente ligneVente = ligneVenteRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Ligne Vente not found !!")
        );
        ligneVenteRepository.deleteById(id);
        return true;
    }
}
