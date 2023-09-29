package com.benzaied.gestiondestock.model;

import java.util.List;

import jakarta.persistence.Column;
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
public class Categorie extends AbstractEntity {
	
	@Column(unique = true)
	private String codeCategorie;

	@Column(unique = true)
	private String nomCategorie;
	
	@Column
	private String description;

	@Column(name = "entreprise_id")
	private Integer idEntreprise;
	
	@OneToMany(mappedBy = "categorie")
	private List<Article> articles;

}
