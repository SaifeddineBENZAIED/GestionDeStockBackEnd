package com.benzaied.gestiondestock.model;

import java.util.List;

import com.benzaied.gestiondestock.dto.AdresseDto;
import com.benzaied.gestiondestock.dto.UtilisateurDto;

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
public class Entreprise extends AbstractEntity {
	
	@Column(unique = true)
	private String nom;
	
	@Column
	private String description;
	
	@Embedded
	private Adresse adresse;
	
	@Column(unique = true)
	private String codeFiscal;
	
	@Column
	private String image;
	
	@Column(unique = true)
	private String email;
	
	@Column
	private String numTelephone;
	
	@Column
	private String siteWeb;
	
	@OneToMany(mappedBy = "entreprise")
	private List<Utilisateur> utilisateurs;
 
}
