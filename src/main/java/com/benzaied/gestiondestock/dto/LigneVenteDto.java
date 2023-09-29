package com.benzaied.gestiondestock.dto;

import java.math.BigDecimal;

import com.benzaied.gestiondestock.model.LigneVente;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LigneVenteDto {
	
	private Integer id;

	private ArticleDto article;

	private VenteDto vente;
	
	private BigDecimal quantite;
	
	private BigDecimal prixUnitaire;

	private Integer idEntreprise;

	public static LigneVenteDto fromEntity(LigneVente ligneVente) {
		if (ligneVente == null) {
			return null;
		}

		return LigneVenteDto.builder()
				.id(ligneVente.getId())
				.article(ArticleDto.fromEntity(ligneVente.getArticle()))
				.vente(VenteDto.fromEntity(ligneVente.getVente()))
				.quantite(ligneVente.getQuantite())
				.prixUnitaire(ligneVente.getPrixUnitaire())
				.idEntreprise(ligneVente.getIdEntreprise())
				.build();
	}

	public static LigneVente toEntity(LigneVenteDto ligneVenteDto) {
		if (ligneVenteDto == null) {
			return null;
		}

		LigneVente ligneVente = new LigneVente();
		ligneVente.setId(ligneVenteDto.getId());
		ligneVente.setArticle(ArticleDto.toEntity(ligneVenteDto.getArticle()));
		ligneVente.setVente(VenteDto.toEntity(ligneVenteDto.getVente()));
		ligneVente.setQuantite(ligneVenteDto.getQuantite());
		ligneVente.setPrixUnitaire(ligneVenteDto.getPrixUnitaire());
		ligneVente.setIdEntreprise(ligneVenteDto.getIdEntreprise());

		return ligneVente;
	}

}
