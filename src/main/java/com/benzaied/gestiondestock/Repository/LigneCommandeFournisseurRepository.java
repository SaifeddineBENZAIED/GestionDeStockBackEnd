package com.benzaied.gestiondestock.Repository;

import com.benzaied.gestiondestock.model.Fournisseur;
import com.benzaied.gestiondestock.model.LigneCommandeClient;
import com.benzaied.gestiondestock.model.LigneCommandeFournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LigneCommandeFournisseurRepository extends JpaRepository<LigneCommandeFournisseur, Integer> {
    List<LigneCommandeFournisseur> findAllByCommandeFournisseurId(Integer idcommande);

    List<LigneCommandeFournisseur> findAllByArticleId(Integer idArticle);

    List<LigneCommandeFournisseur> findByCommandeFournisseur_Fournisseur(Fournisseur fournisseur);

    List<LigneCommandeFournisseur> findAllByIdEntreprise(Integer idEntreprise);
}
