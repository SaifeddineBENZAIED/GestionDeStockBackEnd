package com.benzaied.gestiondestock.Repository;

import com.benzaied.gestiondestock.model.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategorieRepository extends JpaRepository<Categorie, Integer> {
    Optional<Categorie> findByCodeCategorie(String codeCategorie);
    Optional<Categorie> findByNomCategorieIgnoreCase(String nomCategorie);
}
