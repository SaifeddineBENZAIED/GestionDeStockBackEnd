package com.benzaied.gestiondestock.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.benzaied.gestiondestock.model.Entreprise;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EntrepriseDto {
	
	private Integer id;

	private String nom;
	
	private String description;
	
	private AdresseDto adresse;
	
	private String codeFiscal;
	
	private String image;
	
	private String email;
	
	private String numTelephone;
	
	private String siteWeb;

	@JsonIgnore
	private List<UtilisateurDto> utilisateurs;

	public static EntrepriseDto fromEntity(Entreprise entreprise) {
		if (entreprise == null) {
			return null;
		}

		return EntrepriseDto.builder()
				.id(entreprise.getId())
				.nom(entreprise.getNom())
				.description(entreprise.getDescription())
				.adresse(AdresseDto.fromEntity(entreprise.getAdresse()))
				.codeFiscal(entreprise.getCodeFiscal())
				.image(entreprise.getImage())
				.email(entreprise.getEmail())
				.numTelephone(entreprise.getNumTelephone())
				.siteWeb(entreprise.getSiteWeb())
				/*.utilisateurs(entreprise.getUtilisateurs().stream()
						.map(UtilisateurDto::fromEntity)
						.collect(Collectors.toList())
				)*/
				.build();
	}

	public static Entreprise toEntity(EntrepriseDto entrepriseDto) {
		if (entrepriseDto == null) {
			return null;
		}

		Entreprise entreprise = new Entreprise();
		entreprise.setId(entrepriseDto.getId());
		entreprise.setNom(entrepriseDto.getNom());
		entreprise.setDescription(entrepriseDto.getDescription());
		entreprise.setAdresse(AdresseDto.toEntity(entrepriseDto.getAdresse()));
		entreprise.setCodeFiscal(entrepriseDto.getCodeFiscal());
		entreprise.setImage(entrepriseDto.getImage());
		entreprise.setEmail(entrepriseDto.getEmail());
		entreprise.setNumTelephone(entrepriseDto.getNumTelephone());
		entreprise.setSiteWeb(entrepriseDto.getSiteWeb());
		/*entreprise.setUtilisateurs(entrepriseDto.getUtilisateurs().stream()
				.map(UtilisateurDto::toEntity)
				.collect(Collectors.toList())
		);*/

		return entreprise;
	}

}
