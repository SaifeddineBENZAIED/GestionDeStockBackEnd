package com.benzaied.gestiondestock.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@Data
public class ArticleDateLimiteDto {

    private ArticleDto articleDto;
    private Instant dateLimiteConsommation;

    public ArticleDateLimiteDto(ArticleDto articleDto, Instant dateLimiteConsommation) {
        this.articleDto=articleDto;
        this.dateLimiteConsommation=dateLimiteConsommation;
    }

}
