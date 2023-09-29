package com.benzaied.gestiondestock.Repository;

import com.benzaied.gestiondestock.model.Client;
import com.benzaied.gestiondestock.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findByNomAndPrenomIgnoreCase(String nom, String prenom);
    Optional<Client> findByNomIgnoreCase(String nom);
    Optional<Client> findByPrenomIgnoreCase(String prenom);
    Optional<Client> findByEmail(String email);

    List<Client> findAllByIdEntreprise(Integer idEntreprise);
}
