package com.benzaied.gestiondestock.model;

import java.util.List;

import com.benzaied.gestiondestock.token.Token;
import jakarta.persistence.*;
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
public class Client extends AbstractEntity {
	
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
	private String motDePasse;
	
	@Column
	private String numTelephone;

	@Column
	@Enumerated(EnumType.STRING)
	private TypeClient typeClient;

	@Column
	@Enumerated(EnumType.STRING)
	private Roles role = Roles.USER;

	@Column(name = "entreprise_id")
	private Integer idEntreprise;

	@OneToMany(mappedBy = "client")
	private List<Token> tokens;
	
	@OneToMany(mappedBy = "client")
	private List<CommandeClient> commandesClient;

}
