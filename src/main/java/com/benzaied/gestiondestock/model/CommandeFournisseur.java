package com.benzaied.gestiondestock.model;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table
public class CommandeFournisseur extends AbstractEntity {
	
	@Column(unique = true)
	private String codeCF;
	
	@Column
	private Instant dateCommande;

	@Column
	private EtatCommande etatCommande;

	@Column(name = "entreprise_id")
	private Integer idEntreprise;
	
	@ManyToOne
	@JoinColumn(name = "idfournisseur")
	private Fournisseur fournisseur;
	
	@OneToMany(mappedBy = "commandeFournisseur")
	private List<LigneCommandeFournisseur> ligneCommandeFournisseurs;

}
