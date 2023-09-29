package com.benzaied.gestiondestock.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.benzaied.gestiondestock.model.Article;
import com.benzaied.gestiondestock.model.Categorie;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class CategorieDto {
	
	private Integer id;

	private String codeCategorie;

	private String nomCategorie;
	
	private String description;

	private Integer idEntreprise;

	@JsonIgnore
	private List<ArticleDto> articles;
	
	public static CategorieDto fromEntity(Categorie categorie) {
		if (categorie == null) {
			return null;
		}
		
		return CategorieDto.builder()
				.id(categorie.getId())
				.codeCategorie(categorie.getCodeCategorie())
				.nomCategorie(categorie.getNomCategorie())
				.description(categorie.getDescription())
				.idEntreprise(categorie.getIdEntreprise())
				/*.articles(categorie.getArticles().stream()
						.map(ArticleDto::fromEntity)
						.collect(Collectors.toList())
				)*/
				.build();
		
	}

	public static Categorie toEntity(CategorieDto categorieDto) {
		if (categorieDto == null) {
			return null;
		}

		Categorie categorie = new Categorie();
		categorie.setId(categorieDto.getId());
		categorie.setCodeCategorie(categorieDto.getCodeCategorie());
		categorie.setNomCategorie(categorieDto.getNomCategorie());
		categorie.setDescription(categorieDto.getDescription());
		categorie.setIdEntreprise(categorieDto.getIdEntreprise());
		/*categorie.setArticles(categorieDto.getArticles().stream()
				.map(ArticleDto::toEntity)
				.collect(Collectors.toList())
		);*/

		return categorie;

	}

}
