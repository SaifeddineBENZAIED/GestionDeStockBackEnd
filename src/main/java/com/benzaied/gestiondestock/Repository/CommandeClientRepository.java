package com.benzaied.gestiondestock.Repository;

import com.benzaied.gestiondestock.model.CommandeClient;
import com.benzaied.gestiondestock.model.EtatCommande;
import com.benzaied.gestiondestock.model.LigneCommandeClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface CommandeClientRepository extends JpaRepository<CommandeClient, Integer> {
    Optional<CommandeClient> findByCodeCC(String codeCC);
    List<CommandeClient> findAllByClientId(Integer id);
    List<CommandeClient> findAllByIdEntrepriseAndDateCommandeBetweenAndEtatCommande(Integer idEntreprise, Instant dateDebut, Instant dateFin, EtatCommande livree);
}
