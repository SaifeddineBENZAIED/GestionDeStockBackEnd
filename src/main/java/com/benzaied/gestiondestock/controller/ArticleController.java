package com.benzaied.gestiondestock.controller;

import com.benzaied.gestiondestock.dto.*;
import com.benzaied.gestiondestock.exception.EntityNotFoundException;
import com.benzaied.gestiondestock.exception.InvalidEntityException;
import com.benzaied.gestiondestock.model.TypeClient;
import com.benzaied.gestiondestock.services.ArticleService;
import com.benzaied.gestiondestock.services.email.EmailSenderService;
import com.benzaied.gestiondestock.utils.Constants;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/gestiondestock/v1/articles")
@AllArgsConstructor
@Validated
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private EmailSenderService emailSenderService;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create an article", responses = {
            @ApiResponse(responseCode = "201", description = "Article created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = InvalidEntityException.class)))
    })
    public ResponseEntity<ArticleDto> save(@Valid @RequestBody ArticleDto articleDto) {
        ArticleDto savedArticle = articleService.save(articleDto);
        emailSenderService.sendSimpleEmail();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedArticle);
    }

    @GetMapping(value = "/find/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find article by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Article found", content = @Content(schema = @Schema(implementation = ArticleDto.class))),
            @ApiResponse(responseCode = "404", description = "Article not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
    })
    public ResponseEntity<ArticleDto> findById(@PathVariable Integer id) {
        ArticleDto articleDto = articleService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(articleDto);
    }

    @GetMapping(value = "/find/nom/{nomArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find article by name", responses = {
            @ApiResponse(responseCode = "200", description = "Article found", content = @Content(schema = @Schema(implementation = ArticleDto.class))),
            @ApiResponse(responseCode = "404", description = "Article not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
    })
    public ResponseEntity<ArticleDto> findByNomArticle(@PathVariable String nomArticle) {
        ArticleDto articleDto = articleService.findByNomArticle(nomArticle);
        return ResponseEntity.status(HttpStatus.OK).body(articleDto);
    }

    @GetMapping(value = "/find/code/{codeArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find article by code", responses = {
            @ApiResponse(responseCode = "200", description = "Article found", content = @Content(schema = @Schema(implementation = ArticleDto.class))),
            @ApiResponse(responseCode = "404", description = "Article not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
    })
    public ResponseEntity<ArticleDto> findByCodeArticle(@PathVariable String codeArticle) {
        ArticleDto articleDto = articleService.findByCodeArticle(codeArticle);
        return ResponseEntity.status(HttpStatus.OK).body(articleDto);
    }

    @GetMapping(value = "/find/type-client", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find articles by type of client", responses = {
            @ApiResponse(responseCode = "200", description = "Articles found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ArticleDto.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
    })
    public ResponseEntity<List<ArticleDto>> findByTypeClient(@Valid @RequestBody ClientDto clientDto) {
        List<ArticleDto> articleDtos = articleService.findByTypeClient(clientDto);
        return ResponseEntity.status(HttpStatus.OK).body(articleDtos);
    }

    @GetMapping(value = "/find/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find all articles", responses = {
            @ApiResponse(responseCode = "200", description = "Articles found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ArticleDto.class))))
    })
    public ResponseEntity<List<ArticleDto>> findAll() {
        List<ArticleDto> articleDtos = articleService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(articleDtos);
    }

    @GetMapping(value = "/find/history/vente/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find historical sales of an article", responses = {
            @ApiResponse(responseCode = "200", description = "Historical sales found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LigneVenteDto.class)))),
            @ApiResponse(responseCode = "404", description = "Article not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
    })
    public ResponseEntity<List<LigneVenteDto>> findHistoriqueVentes(@PathVariable Integer idArticle) {
        List<LigneVenteDto> ligneVenteDtos = articleService.findHistoriqueVentes(idArticle);
        return ResponseEntity.status(HttpStatus.OK).body(ligneVenteDtos);
    }

    @GetMapping(value = "/find/history/commande-client/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find historical client orders of an article", responses = {
            @ApiResponse(responseCode = "200", description = "Historical client orders found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LigneCommandeClientDto.class)))),
            @ApiResponse(responseCode = "404", description = "Article not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
    })
    public ResponseEntity<List<LigneCommandeClientDto>> findHistoriqueCommandeClient(@PathVariable Integer idArticle) {
        List<LigneCommandeClientDto> ligneCommandeClientDtos = articleService.findHistoriqueCommandeClient(idArticle);
        return ResponseEntity.status(HttpStatus.OK).body(ligneCommandeClientDtos);
    }

    @GetMapping(value = "/find/history/commande-fournisseur/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find historical supplier orders of an article", responses = {
            @ApiResponse(responseCode = "200", description = "Historical supplier orders found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LigneCommandeFournisseurDto.class)))),
            @ApiResponse(responseCode = "404", description = "Article not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
    })
    public ResponseEntity<List<LigneCommandeFournisseurDto>> findHistoriqueCommandeFournisseur(@PathVariable Integer idArticle) {
        List<LigneCommandeFournisseurDto> ligneCommandeFournisseurDtos = articleService.findHistoriqueCommandeFournisseur(idArticle);
        return ResponseEntity.status(HttpStatus.OK).body(ligneCommandeFournisseurDtos);
    }

    @GetMapping(value = "/find/All/categorie/{idCategorie}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find all articles by category ID", responses = {
            @ApiResponse(responseCode = "200", description = "Articles found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ArticleDto.class))))
    })
    public ResponseEntity<List<ArticleDto>> findAllArticleByIdCategory(@PathVariable Integer idCategorie) {
        List<ArticleDto> articleDtos = articleService.findAllArticleByIdCategory(idCategorie);
        return ResponseEntity.status(HttpStatus.OK).body(articleDtos);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Operation(summary = "Delete an article by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Article deleted successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    public ResponseEntity<ResponseDto> delete(@PathVariable Integer id) {
        boolean isDeleted = articleService.delete(id);
        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(Constants.STATUS_200, Constants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(Constants.STATUS_500, Constants.MESSAGE_500));
        }
    }

    @DeleteMapping(value = "/delete/nom/{nomArticle}")
    @Operation(summary = "Delete an article by name", responses = {
            @ApiResponse(responseCode = "200", description = "Article deleted successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    public ResponseEntity<ResponseDto> deleteByNomArticleIgnoreCase(@PathVariable String nomArticle) {
        boolean isDeleted = articleService.deleteByNomArticleIgnoreCase(nomArticle);
        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(Constants.STATUS_200, Constants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(Constants.STATUS_500, Constants.MESSAGE_500));
        }

    }
}
