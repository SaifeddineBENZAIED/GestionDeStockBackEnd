package com.benzaied.gestiondestock.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class Adresse {
	
	@Column
	private String adresse1;
	
	@Column
	private String adresse2;
	
	@Column
	private String ville;
	
	@Column
	private String codePostale;
	
	@Column
	private String pays;

}
