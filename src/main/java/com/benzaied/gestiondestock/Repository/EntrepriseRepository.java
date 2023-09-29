package com.benzaied.gestiondestock.Repository;

import com.benzaied.gestiondestock.model.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EntrepriseRepository extends JpaRepository<Entreprise, Integer> {
    Optional<Entreprise> findByNomIgnoreCase(String nom);
    void deleteByNom(String nom);
}
