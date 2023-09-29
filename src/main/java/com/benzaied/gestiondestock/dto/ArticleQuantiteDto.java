package com.benzaied.gestiondestock.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@RequiredArgsConstructor
public class ArticleQuantiteDto {

    private ArticleDto articleDto;

    private BigDecimal quantite;

    public ArticleQuantiteDto(ArticleDto articleDto, BigDecimal quantiteVendue) {

        this.articleDto = articleDto;
        this.quantite = quantiteVendue;

    }
}
