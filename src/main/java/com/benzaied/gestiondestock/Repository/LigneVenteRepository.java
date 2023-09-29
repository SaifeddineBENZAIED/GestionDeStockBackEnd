package com.benzaied.gestiondestock.Repository;

import com.benzaied.gestiondestock.model.EtatCommande;
import com.benzaied.gestiondestock.model.LigneCommandeClient;
import com.benzaied.gestiondestock.model.LigneVente;
import com.benzaied.gestiondestock.model.Vente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface LigneVenteRepository extends JpaRepository<LigneVente, Integer> {
    List<LigneVente> findAllByArticleId(Integer idArticle);
    List<LigneVente> findAllByVenteId(Integer idVente);
    List<LigneVente> findAllByIdEntreprise(Integer idEntreprise);
}
