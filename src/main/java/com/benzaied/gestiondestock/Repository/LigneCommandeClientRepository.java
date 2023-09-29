package com.benzaied.gestiondestock.Repository;

import com.benzaied.gestiondestock.model.EtatCommande;
import com.benzaied.gestiondestock.model.LigneCommandeClient;
import com.benzaied.gestiondestock.model.LigneCommandeFournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface LigneCommandeClientRepository extends JpaRepository<LigneCommandeClient, Integer> {

    List<LigneCommandeClient> findAllByCommandeClientId(Integer idCommande);

    List<LigneCommandeClient> findAllByArticleId(Integer idArticle);

    List<LigneCommandeClient> findAllByIdEntreprise(Integer idEntreprise);

}
