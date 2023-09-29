package com.benzaied.gestiondestock.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import jakarta.persistence.*;

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
public class Article extends AbstractEntity {

	@Column(unique = true)
	private String nomArticle;

	@Column(unique = true)
	private String codeArticle;
	
	@Column
	private String description;
	
	@Column
	private BigDecimal prixUnitaireHT;
	
	@Column
	private BigDecimal tauxTVA;
	
	@Column
	private BigDecimal prixUnitaireTTC;

	@Column
	private Instant dateLimiteConsommation;
	
	@Column
	private String image;

	@Column(name = "entreprise_id")
	private Integer idEntreprise;

	@Column
	@Enumerated(EnumType.STRING)
	private List<TypeClient> typeClientsAutorise;
	
	@ManyToOne
	@JoinColumn(name = "idcategorie")
	private Categorie categorie;

	@OneToMany(mappedBy = "article")
	private List<LigneCommandeClient> ligneCommandeClients;

	@OneToMany(mappedBy = "article")
	private List<LigneCommandeFournisseur> ligneCommandeFournisseurs;

	@OneToMany(mappedBy = "article")
	private List<LigneVente> ligneVentes;

	@OneToMany(mappedBy = "article")
	private List<MvmntStck> mvmntStcks;

}
