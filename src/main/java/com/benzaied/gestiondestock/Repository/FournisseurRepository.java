package com.benzaied.gestiondestock.Repository;

import com.benzaied.gestiondestock.model.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FournisseurRepository extends JpaRepository<Fournisseur, Integer> {
    Optional<Fournisseur> findByNomAndPrenomIgnoreCase(String nom, String prenom);
    Optional<Fournisseur> findByNomIgnoreCase(String nom);
    Optional<Fournisseur> findByPrenomIgnoreCase(String prenom);

}
