package com.benzaied.gestiondestock.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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
public class Fournisseur extends AbstractEntity {
	
	@Column
	private String nom;
	
	@Column
	private String prenom;
	
	@Embedded
	private Adresse adresse;
	
	@Column
	private String image;
	
	@Column(unique = true)
	private String email;
	
	@Column
	private String numTelephone;

	@Column(name = "entreprise_id")
	private Integer idEntreprise;
	
	@OneToMany(mappedBy = "fournisseur")
	private List<CommandeFournisseur> commandesFournisseur;


}
