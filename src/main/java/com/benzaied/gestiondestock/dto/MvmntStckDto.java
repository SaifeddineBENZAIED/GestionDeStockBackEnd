package com.benzaied.gestiondestock.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.benzaied.gestiondestock.model.MvmntStck;
import com.benzaied.gestiondestock.model.SourceMvmntStck;
import com.benzaied.gestiondestock.model.TypedeMvmntStck;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MvmntStckDto {
	
	private Integer id;

	private Instant dateMvmnt;
	
	private BigDecimal quantite;

	private ArticleDto article;
	
	private TypedeMvmntStck typeMvmntStck;

	private SourceMvmntStck sourceMvmntStck;

	private Integer idEntreprise;

	public static MvmntStckDto fromEntity(MvmntStck mvmntStck) {
		if (mvmntStck == null) {
			return null;
		}

		return MvmntStckDto.builder()
				.id(mvmntStck.getId())
				.dateMvmnt(mvmntStck.getDateMvmnt())
				.quantite(mvmntStck.getQuantite())
				.article(ArticleDto.fromEntity(mvmntStck.getArticle()))
				.typeMvmntStck(mvmntStck.getTypeMvmntStck())
				.sourceMvmntStck(mvmntStck.getSourceMvmntStck())
				.idEntreprise(mvmntStck.getIdEntreprise())
				.build();
	}

	public static MvmntStck toEntity(MvmntStckDto mvmntStckDto) {
		if (mvmntStckDto == null) {
			return null;
		}

		MvmntStck mvmntStck = new MvmntStck();
		mvmntStck.setId(mvmntStckDto.getId());
		mvmntStck.setDateMvmnt(mvmntStckDto.getDateMvmnt());
		mvmntStck.setQuantite(mvmntStckDto.getQuantite());
		mvmntStck.setArticle(ArticleDto.toEntity(mvmntStckDto.getArticle()));
		mvmntStck.setTypeMvmntStck(mvmntStckDto.getTypeMvmntStck());
		mvmntStck.setSourceMvmntStck(mvmntStckDto.getSourceMvmntStck());
		mvmntStck.setIdEntreprise(mvmntStckDto.getIdEntreprise());

		return mvmntStck;
	}

}
