package com.benzaied.gestiondestock.dto;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import com.benzaied.gestiondestock.model.Roles;
import com.benzaied.gestiondestock.model.Utilisateur;
import com.benzaied.gestiondestock.token.Token;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UtilisateurDto {
	
	private Integer id;

	private String nom;
	
	private String prenom;
	
	private AdresseDto adresse;
	
	private String image;
	
	private String email;
	
	private String numTelephone;
	
	private Instant dateNaissance;
	
	private String motDePasse;
	
	private EntrepriseDto entreprise;

	private List<Roles> roles;

	@JsonIgnore
	private List<Token> tokens;

	public static UtilisateurDto fromEntity(Utilisateur utilisateur) {
		if (utilisateur == null) {
			return null;
		}

		return UtilisateurDto.builder()
				.id(utilisateur.getId())
				.nom(utilisateur.getNom())
				.prenom(utilisateur.getPrenom())
				.adresse(AdresseDto.fromEntity(utilisateur.getAdresse()))
				.image(utilisateur.getImage())
				.email(utilisateur.getEmail())
				.numTelephone(utilisateur.getNumTelephone())
				.dateNaissance(utilisateur.getDateNaissance())
				.motDePasse(utilisateur.getMotDePasse())
				.entreprise(EntrepriseDto.fromEntity(utilisateur.getEntreprise()))
				.roles(utilisateur.getRoles())
				.tokens(utilisateur.getTokens())
				.build();
	}

	public static Utilisateur toEntity(UtilisateurDto utilisateurDto) {
		if (utilisateurDto == null) {
			return null;
		}

		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setId(utilisateurDto.getId());
		utilisateur.setNom(utilisateurDto.getNom());
		utilisateur.setPrenom(utilisateurDto.getPrenom());
		utilisateur.setAdresse(AdresseDto.toEntity(utilisateurDto.getAdresse()));
		utilisateur.setImage(utilisateurDto.getImage());
		utilisateur.setEmail(utilisateurDto.getEmail());
		utilisateur.setNumTelephone(utilisateurDto.getNumTelephone());
		utilisateur.setDateNaissance(utilisateurDto.getDateNaissance());
		utilisateur.setMotDePasse(utilisateurDto.getMotDePasse());
		utilisateur.setEntreprise(EntrepriseDto.toEntity(utilisateurDto.getEntreprise()));
		utilisateur.setRoles(utilisateurDto.getRoles());
		utilisateur.setTokens(utilisateurDto.getTokens());

		return utilisateur;
	}


}
