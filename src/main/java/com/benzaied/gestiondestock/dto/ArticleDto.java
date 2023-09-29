package com.benzaied.gestiondestock.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.benzaied.gestiondestock.model.*;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ArticleDto {
	
	private Integer id;

	private String nomArticle;

	private String codeArticle;
	
	private String description;
	
	private BigDecimal prixUnitaireHT;
	
	private BigDecimal tauxTVA;
	
	private BigDecimal prixUnitaireTTC;

	private Instant dateLimiteConsommation;
	
	private String image;

	private List<TypeClient> typeClientsAutorise;
	
	private CategorieDto categorie;

	private Integer idEntreprise;

	public static ArticleDto fromEntity(Article article) {
		if (article == null) {
			return null;
		}

		return ArticleDto.builder()
				.id(article.getId())
				.nomArticle(article.getNomArticle())
				.codeArticle(article.getCodeArticle())
				.description(article.getDescription())
				.prixUnitaireHT(article.getPrixUnitaireHT())
				.tauxTVA(article.getTauxTVA())
				.prixUnitaireTTC(article.getPrixUnitaireTTC())
				.dateLimiteConsommation(article.getDateLimiteConsommation())
				.image(article.getImage())
				.typeClientsAutorise(article.getTypeClientsAutorise())
				.idEntreprise(article.getIdEntreprise())
				.categorie(CategorieDto.fromEntity(article.getCategorie()))
				.build();

	}

	public static Article toEntity(ArticleDto articleDto) {
		if (articleDto == null) {
			return null;
		}

		Article article = new Article();
		article.setId(articleDto.getId());
		article.setNomArticle(articleDto.getNomArticle());
		article.setCodeArticle(articleDto.getCodeArticle());
		article.setDescription(articleDto.getDescription());
		article.setPrixUnitaireHT(articleDto.getPrixUnitaireHT());
		article.setTauxTVA(articleDto.getTauxTVA());
		article.setPrixUnitaireTTC(articleDto.getPrixUnitaireTTC());
		article.setDateLimiteConsommation(articleDto.getDateLimiteConsommation());
		article.setImage(articleDto.getImage());
		article.setTypeClientsAutorise(articleDto.getTypeClientsAutorise());
		article.setIdEntreprise(articleDto.getIdEntreprise());
		article.setCategorie(CategorieDto.toEntity(articleDto.getCategorie()));

		return article;

	}

	public static BigDecimal getQuantiteTotaleFournieArticle(Integer idEntreprise, Fournisseur fournisseur, Article article) {
		BigDecimal quantiteTotale = BigDecimal.ZERO;
		for (LigneCommandeFournisseur ligneCommande : article.getLigneCommandeFournisseurs()) {
			if (ligneCommande.getCommandeFournisseur() != null
					&& ligneCommande.getCommandeFournisseur().getFournisseur().equals(fournisseur)
					&& ligneCommande.getArticle().getIdEntreprise().equals(idEntreprise)) {
				quantiteTotale = quantiteTotale.add(ligneCommande.getQuantite());
			}
		}
		return quantiteTotale;
	}

}
