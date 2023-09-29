package com.benzaied.gestiondestock.services.impl;

import com.benzaied.gestiondestock.Repository.LigneCommandeClientRepository;
import com.benzaied.gestiondestock.dto.LigneCommandeClientDto;
import com.benzaied.gestiondestock.exception.EntityNotFoundException;
import com.benzaied.gestiondestock.model.Fournisseur;
import com.benzaied.gestiondestock.model.LigneCommandeClient;
import com.benzaied.gestiondestock.services.LigneCommandeClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LigneCommandeClientServiceImpl implements LigneCommandeClientService {

    private final LigneCommandeClientRepository ligneCommandeClientRepository;

    @Autowired
    public LigneCommandeClientServiceImpl(LigneCommandeClientRepository ligneCommandeClientRepository) {
        this.ligneCommandeClientRepository = ligneCommandeClientRepository;
    }

    @Override
    public LigneCommandeClientDto save(LigneCommandeClientDto ligneCommandeClientDto) {
        LigneCommandeClient ligneCommandeClient = LigneCommandeClientDto.toEntity(ligneCommandeClientDto);
        LigneCommandeClient savedLigneCommandeClient = ligneCommandeClientRepository.save(ligneCommandeClient);
        return LigneCommandeClientDto.fromEntity(savedLigneCommandeClient);
    }

    @Override
    public LigneCommandeClientDto findById(Integer id) {
        return ligneCommandeClientRepository.findById(id)
                .map(LigneCommandeClientDto::fromEntity)
                .orElse(null);
    }

    @Override
    public List<LigneCommandeClientDto> findAll() {
        List<LigneCommandeClient> lignesCommandeClient = ligneCommandeClientRepository.findAll();
        return lignesCommandeClient.stream()
                .map(LigneCommandeClientDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(Integer id) {
        if (id == null) {
            log.error("L'ID de Ligne CC est non valide");
            return false;
        }
        LigneCommandeClient ligneCommandeClient = ligneCommandeClientRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Ligne CC not found !!")
        );
        ligneCommandeClientRepository.deleteById(id);
        return true;
    }
}
