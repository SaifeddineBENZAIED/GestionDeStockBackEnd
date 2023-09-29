package com.benzaied.gestiondestock.services.impl;

import com.benzaied.gestiondestock.Repository.LigneCommandeFournisseurRepository;
import com.benzaied.gestiondestock.dto.LigneCommandeFournisseurDto;
import com.benzaied.gestiondestock.exception.EntityNotFoundException;
import com.benzaied.gestiondestock.model.LigneCommandeClient;
import com.benzaied.gestiondestock.model.LigneCommandeFournisseur;
import com.benzaied.gestiondestock.services.LigneCommandeFournisseurService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LigneCommandeFournisseurServiceImpl implements LigneCommandeFournisseurService {

    private final LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository;

    @Autowired
    public LigneCommandeFournisseurServiceImpl(LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository) {
        this.ligneCommandeFournisseurRepository = ligneCommandeFournisseurRepository;
    }

    @Override
    public LigneCommandeFournisseurDto save(LigneCommandeFournisseurDto ligneCommandeFournisseurDto) {
        LigneCommandeFournisseur ligneCommandeFournisseur = LigneCommandeFournisseurDto.toEntity(ligneCommandeFournisseurDto);
        LigneCommandeFournisseur savedLigneCommandeFournisseur = ligneCommandeFournisseurRepository.save(ligneCommandeFournisseur);
        return LigneCommandeFournisseurDto.fromEntity(savedLigneCommandeFournisseur);
    }

    @Override
    public LigneCommandeFournisseurDto findById(Integer id) {
        return ligneCommandeFournisseurRepository.findById(id)
                .map(LigneCommandeFournisseurDto::fromEntity)
                .orElse(null);
    }

    @Override
    public List<LigneCommandeFournisseurDto> findAll() {
        List<LigneCommandeFournisseur> lignesCommandeFournisseur = ligneCommandeFournisseurRepository.findAll();
        return lignesCommandeFournisseur.stream()
                .map(LigneCommandeFournisseurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(Integer id) {
        if (id == null) {
            log.error("L'ID de Ligne CF est non valide");
            return false;
        }
        LigneCommandeFournisseur ligneCommandeFournisseur = ligneCommandeFournisseurRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Ligne CF not found !!")
        );
        ligneCommandeFournisseurRepository.deleteById(id);
        return true;
    }
}
