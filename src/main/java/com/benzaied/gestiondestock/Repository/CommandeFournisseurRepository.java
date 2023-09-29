package com.benzaied.gestiondestock.Repository;

import com.benzaied.gestiondestock.model.CommandeClient;
import com.benzaied.gestiondestock.model.CommandeFournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommandeFournisseurRepository extends JpaRepository<CommandeFournisseur, Integer> {
    Optional<CommandeFournisseur> findByCodeCF(String codeCF);
    List<CommandeFournisseur> findAllByFournisseurId(Integer id);
}
