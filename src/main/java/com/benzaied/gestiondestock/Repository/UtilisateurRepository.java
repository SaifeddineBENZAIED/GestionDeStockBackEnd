package com.benzaied.gestiondestock.Repository;

import com.benzaied.gestiondestock.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    Optional<Utilisateur> findByNomAndPrenomIgnoreCase(String nom, String prenom);
    Optional<Utilisateur> findByNomIgnoreCase(String nom);
    Optional<Utilisateur> findByPrenomIgnoreCase(String prenom);
    Optional<Utilisateur> findByEmail(String email);
    /*// JPQL query
    @Query(value = "select u from Utilisateur u where u.email = :email")
    Optional<Utilisateur> findUtilisateurByEmail(@Param("email") String email);*/
}
