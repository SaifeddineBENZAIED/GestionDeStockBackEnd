package com.benzaied.gestiondestock.Repository;

import com.benzaied.gestiondestock.model.EtatCommande;
import com.benzaied.gestiondestock.model.Vente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface VenteRepository extends JpaRepository<Vente, Integer> {
    Optional<Vente> findByCodeVente(String codeVente);

    List<Vente> findAllByIdEntrepriseAndDateVenteBetweenAndEtatCommande(Integer idEntreprise, Instant dateDebut, Instant dateFin, EtatCommande livree);
}
