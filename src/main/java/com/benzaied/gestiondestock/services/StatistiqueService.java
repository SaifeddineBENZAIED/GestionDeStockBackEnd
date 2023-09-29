package com.benzaied.gestiondestock.services;

import com.benzaied.gestiondestock.dto.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface StatistiqueService {

    List<ArticleQuantiteDto> getArticlesPlusVendus(Integer idEntreprise);
    List<FideliteClientDto> getClientsPlusFidels(Integer idEntreprise);
    List<ArticleMontantTotalPeriodeDto> getVenteStatistiqueParPeriode(Integer idEntreprise, Instant dateDebut, Instant dateFin);
    List<ArticleDateLimiteDto> getArticleParDateLimite(Integer idEntreprise);

}
