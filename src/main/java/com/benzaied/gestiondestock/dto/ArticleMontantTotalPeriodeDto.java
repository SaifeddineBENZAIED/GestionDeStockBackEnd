package com.benzaied.gestiondestock.dto;

import com.benzaied.gestiondestock.model.Article;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class ArticleMontantTotalPeriodeDto {

    private ArticleDto articleDto;
    private BigDecimal quantiteTotalPeriod;

    public ArticleMontantTotalPeriodeDto(ArticleDto articleDto, BigDecimal quantiteTotalPeriod) {

        this.articleDto = articleDto;
        this.quantiteTotalPeriod = quantiteTotalPeriod;

    }

}
