package com.benzaied.gestiondestock.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import com.benzaied.gestiondestock.token.Token;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table
public class Utilisateur extends AbstractEntity {
	
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
	
	@Column
	private Instant dateNaissance;
	
	@Column
	private String motDePasse;
	
	@ManyToOne
	@JoinColumn(name = "identreprise")
	private Entreprise entreprise;

	@Column
	@Enumerated(EnumType.STRING)
	private List<Roles> roles;

	@OneToMany(mappedBy = "utilisateur")
	private List<Token> tokens;

}
