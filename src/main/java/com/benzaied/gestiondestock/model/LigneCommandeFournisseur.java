package com.benzaied.gestiondestock.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class LigneCommandeFournisseur extends AbstractEntity {
	
	@ManyToOne
	@JoinColumn(name="idarticle")
	private Article article;
	
	@ManyToOne
	@JoinColumn(name = "idcommandefournisseur")
	private CommandeFournisseur commandeFournisseur;
	
	@Column
	private BigDecimal quantite;
	
	@Column
	private BigDecimal prixUnitaire;

	@Column(name = "entreprise_id")
	private Integer idEntreprise;

}
