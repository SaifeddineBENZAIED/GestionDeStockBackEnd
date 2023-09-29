package com.benzaied.gestiondestock.dto;

import java.math.BigDecimal;

import com.benzaied.gestiondestock.model.LigneCommandeFournisseur;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LigneCommandeFournisseurDto {
	
	private Integer id;

	private ArticleDto article;
	
	private CommandeFournisseurDto commandeFournisseur;
	
	private BigDecimal quantite;
	
	private BigDecimal prixUnitaire;

	private Integer idEntreprise;

	public static LigneCommandeFournisseurDto fromEntity(LigneCommandeFournisseur ligneCommandeFournisseur) {
		if (ligneCommandeFournisseur == null) {
			return null;
		}

		return LigneCommandeFournisseurDto.builder()
				.id(ligneCommandeFournisseur.getId())
				.article(ArticleDto.fromEntity(ligneCommandeFournisseur.getArticle()))
				.commandeFournisseur(CommandeFournisseurDto.fromEntity(ligneCommandeFournisseur.getCommandeFournisseur()))
				.quantite(ligneCommandeFournisseur.getQuantite())
				.prixUnitaire(ligneCommandeFournisseur.getPrixUnitaire())
				.idEntreprise(ligneCommandeFournisseur.getIdEntreprise())
				.build();
	}

	public static LigneCommandeFournisseur toEntity(LigneCommandeFournisseurDto ligneCommandeFournisseurDto) {
		if (ligneCommandeFournisseurDto == null) {
			return null;
		}

		LigneCommandeFournisseur ligneCommandeFournisseur = new LigneCommandeFournisseur();
		ligneCommandeFournisseur.setId(ligneCommandeFournisseurDto.getId());
		ligneCommandeFournisseur.setArticle(ArticleDto.toEntity(ligneCommandeFournisseurDto.getArticle()));
		ligneCommandeFournisseur.setCommandeFournisseur(CommandeFournisseurDto.toEntity(ligneCommandeFournisseurDto.getCommandeFournisseur()));
		ligneCommandeFournisseur.setQuantite(ligneCommandeFournisseurDto.getQuantite());
		ligneCommandeFournisseur.setPrixUnitaire(ligneCommandeFournisseurDto.getPrixUnitaire());
		ligneCommandeFournisseur.setIdEntreprise(ligneCommandeFournisseurDto.getIdEntreprise());

		return ligneCommandeFournisseur;
	}


}
