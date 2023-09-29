package com.benzaied.gestiondestock.controller;

import com.benzaied.gestiondestock.dto.*;
import com.benzaied.gestiondestock.services.StatistiqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/gestiondestock/v1/statistiques")
public class StatistiqueController {

    @Autowired
    private  StatistiqueService statistiqueService;


    @GetMapping(value = "/articles-plus-vendus", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get the most sold articles",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Most sold articles found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ArticleQuantiteDto.class))))
            }
    )
    public List<ArticleQuantiteDto> getArticlesPlusVendus(@RequestParam Integer idEntreprise) {
        return statistiqueService.getArticlesPlusVendus(idEntreprise);
    }

    @GetMapping(value = "/clients-plus-fidels", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get the most loyal clients",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Most loyal clients found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = FideliteClientDto.class))))
            }
    )
    public List<FideliteClientDto> getClientsPlusFidels(@RequestParam Integer idEntreprise) {
        return statistiqueService.getClientsPlusFidels(idEntreprise);
    }

    @GetMapping(value = "/vente-statistique-par-periode", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get sales statistics for a specific period",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sales statistics found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ArticleMontantTotalPeriodeDto.class))))
            }
    )
    public List<ArticleMontantTotalPeriodeDto> getVenteStatistiqueParPeriode(
            @RequestParam Integer idEntreprise,
            @RequestParam Instant dateDebut,
            @RequestParam Instant dateFin) {
        return statistiqueService.getVenteStatistiqueParPeriode(idEntreprise, dateDebut, dateFin);
    }

    @GetMapping(value = "/articles-expiration", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get articles with expiration dates",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Articles with expiration dates found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ArticleDateLimiteDto.class))))
            }
    )
    public List<ArticleDateLimiteDto> getArticleParDateLimite(@RequestParam Integer idEntreprise) {
        return statistiqueService.getArticleParDateLimite(idEntreprise);
    }

}
