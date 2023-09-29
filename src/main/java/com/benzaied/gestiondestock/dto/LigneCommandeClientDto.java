package com.benzaied.gestiondestock.dto;

import java.math.BigDecimal;

import com.benzaied.gestiondestock.model.LigneCommandeClient;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LigneCommandeClientDto {
	
	private Integer id;

	private ArticleDto article;

	private CommandeClientDto commandeClient;
	
	private BigDecimal quantite;
	
	private BigDecimal prixUnitaire;

	private Integer idEntreprise;

	public static LigneCommandeClientDto fromEntity(LigneCommandeClient ligneCommandeClient) {
		if (ligneCommandeClient == null) {
			return null;
		}

		return LigneCommandeClientDto.builder()
				.id(ligneCommandeClient.getId())
				.article(ArticleDto.fromEntity(ligneCommandeClient.getArticle()))
				.commandeClient(CommandeClientDto.fromEntity(ligneCommandeClient.getCommandeClient()))
				.quantite(ligneCommandeClient.getQuantite())
				.prixUnitaire(ligneCommandeClient.getPrixUnitaire())
				.idEntreprise(ligneCommandeClient.getIdEntreprise())
				.build();
	}

	public static LigneCommandeClient toEntity(LigneCommandeClientDto ligneCommandeClientDto) {
		if (ligneCommandeClientDto == null) {
			return null;
		}

		LigneCommandeClient ligneCommandeClient = new LigneCommandeClient();
		ligneCommandeClient.setId(ligneCommandeClientDto.getId());
		ligneCommandeClient.setArticle(ArticleDto.toEntity(ligneCommandeClientDto.getArticle()));
		ligneCommandeClient.setCommandeClient(CommandeClientDto.toEntity(ligneCommandeClientDto.getCommandeClient()));
		ligneCommandeClient.setQuantite(ligneCommandeClientDto.getQuantite());
		ligneCommandeClient.setPrixUnitaire(ligneCommandeClientDto.getPrixUnitaire());
		ligneCommandeClient.setIdEntreprise(ligneCommandeClientDto.getIdEntreprise());

		return ligneCommandeClient;
	}


}
