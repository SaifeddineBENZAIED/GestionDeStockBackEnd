package com.benzaied.gestiondestock.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.benzaied.gestiondestock.model.Fournisseur;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FournisseurDto {
	
	private Integer id;

	private String nom;
	
	private String prenom;
	
	private AdresseDto adresse;
	
	private String image;
	
	private String email;
	
	private String numTelephone;

	private Integer idEntreprise;

	@JsonIgnore
	private List<CommandeFournisseurDto> commandesFournisseur;

	public static FournisseurDto fromEntity(Fournisseur fournisseur) {
		if (fournisseur == null) {
			return null;
		}

		return FournisseurDto.builder()
				.id(fournisseur.getId())
				.nom(fournisseur.getNom())
				.prenom(fournisseur.getPrenom())
				.adresse(AdresseDto.fromEntity(fournisseur.getAdresse()))
				.image(fournisseur.getImage())
				.email(fournisseur.getEmail())
				.numTelephone(fournisseur.getNumTelephone())
				.idEntreprise(fournisseur.getIdEntreprise())
				/*.commandesFournisseur(fournisseur.getCommandesFournisseur().stream()
						.map(CommandeFournisseurDto::fromEntity)
						.collect(Collectors.toList())
				)*/
				.build();
	}

	public static Fournisseur toEntity(FournisseurDto fournisseurDto) {
		if (fournisseurDto == null) {
			return null;
		}

		Fournisseur fournisseur = new Fournisseur();
		fournisseur.setId(fournisseurDto.getId());
		fournisseur.setNom(fournisseurDto.getNom());
		fournisseur.setPrenom(fournisseurDto.getPrenom());
		fournisseur.setAdresse(AdresseDto.toEntity(fournisseurDto.getAdresse()));
		fournisseur.setImage(fournisseurDto.getImage());
		fournisseur.setEmail(fournisseurDto.getEmail());
		fournisseur.setNumTelephone(fournisseurDto.getNumTelephone());
		fournisseur.setIdEntreprise(fournisseurDto.getIdEntreprise());
		/*fournisseur.setCommandesFournisseur(fournisseurDto.getCommandesFournisseur().stream()
				.map(CommandeFournisseurDto::toEntity)
				.collect(Collectors.toList())
		);*/

		return fournisseur;
	}


}
