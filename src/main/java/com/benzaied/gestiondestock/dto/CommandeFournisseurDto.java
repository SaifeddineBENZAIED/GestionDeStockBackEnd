package com.benzaied.gestiondestock.dto;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import com.benzaied.gestiondestock.model.CommandeFournisseur;
import com.benzaied.gestiondestock.model.EtatCommande;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CommandeFournisseurDto {
	
	private Integer id;

	private String codeCF;
	
	private Instant dateCommande;

	private EtatCommande etatCommande;

	private FournisseurDto fournisseur;

	private Integer idEntreprise;

	private List<LigneCommandeFournisseurDto> ligneCommandeFournisseurs;

	public static CommandeFournisseurDto fromEntity(CommandeFournisseur commandeFournisseur) {
		if (commandeFournisseur == null) {
			return null;
		}

		return CommandeFournisseurDto.builder()
				.id(commandeFournisseur.getId())
				.codeCF(commandeFournisseur.getCodeCF())
				.dateCommande(commandeFournisseur.getDateCommande())
				.etatCommande(commandeFournisseur.getEtatCommande())
				.fournisseur(FournisseurDto.fromEntity(commandeFournisseur.getFournisseur()))
				.idEntreprise(commandeFournisseur.getIdEntreprise())
				/*.ligneCommandeFournisseurs(commandeFournisseur.getLigneCommandeFournisseurs().stream()
						.map(LigneCommandeFournisseurDto::fromEntity)
						.collect(Collectors.toList())
				)*/
				.build();
	}

	public static CommandeFournisseur toEntity(CommandeFournisseurDto commandeFournisseurDto) {
		if (commandeFournisseurDto == null) {
			return null;
		}

		CommandeFournisseur commandeFournisseur = new CommandeFournisseur();
		commandeFournisseur.setId(commandeFournisseurDto.getId());
		commandeFournisseur.setCodeCF(commandeFournisseurDto.getCodeCF());
		commandeFournisseur.setDateCommande(commandeFournisseurDto.getDateCommande());
		commandeFournisseur.setEtatCommande(commandeFournisseurDto.getEtatCommande());
		commandeFournisseur.setFournisseur(FournisseurDto.toEntity(commandeFournisseurDto.getFournisseur()));
		commandeFournisseur.setIdEntreprise(commandeFournisseurDto.getIdEntreprise());
		/*commandeFournisseur.setLigneCommandeFournisseurs(commandeFournisseurDto.getLigneCommandeFournisseurs().stream()
				.map(LigneCommandeFournisseurDto::toEntity)
				.collect(Collectors.toList())
		);*/

		return commandeFournisseur;
	}

	public boolean isCommandeLivree(){
		return EtatCommande.LIVREE.equals(this.etatCommande);
	}


}
