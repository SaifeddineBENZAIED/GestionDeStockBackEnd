package com.benzaied.gestiondestock.model;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table
public class Vente extends AbstractEntity {
	
	@Column(unique = true)
	private String codeVente;
	
	@Column
	private Instant dateVente;
	
	@Column
	private String commentaire;

	@Column
	private EtatCommande etatCommande;

	@Column
	private String typeClient = "PASSAGER";

	@Column(name = "entreprise_id")
	private Integer idEntreprise;

	@OneToMany(mappedBy= "vente")
	private List<LigneVente> ligneVentes;

}
