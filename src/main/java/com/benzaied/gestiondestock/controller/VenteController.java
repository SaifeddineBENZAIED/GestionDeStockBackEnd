package com.benzaied.gestiondestock.controller;

import com.benzaied.gestiondestock.dto.*;
import com.benzaied.gestiondestock.exception.EntityNotFoundException;
import com.benzaied.gestiondestock.exception.InvalidEntityException;
import com.benzaied.gestiondestock.exception.InvalidOperationException;
import com.benzaied.gestiondestock.model.EtatCommande;
import com.benzaied.gestiondestock.services.VenteService;
import com.benzaied.gestiondestock.services.email.EmailSenderService;
import com.benzaied.gestiondestock.utils.Constants;
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

import java.math.BigDecimal;
import java.util.List;

import static com.benzaied.gestiondestock.utils.Constants.APP_ROOT;

@RestController
@RequestMapping(path = "/gestiondestock/v1/ventes")
@AllArgsConstructor
@Validated
public class VenteController {

    @Autowired
    private final VenteService venteService;

    @Autowired
    private final EmailSenderService emailSenderService;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new sale", responses = {
            @ApiResponse(responseCode = "201", description = "Sale created successfully", content = @Content(schema = @Schema(implementation = VenteDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = InvalidEntityException.class)))
    })
    public ResponseEntity<ResponseDto> save(@Valid @RequestBody VenteDto venteDto) {
        venteService.save(venteDto);
        emailSenderService.sendSimpleEmail();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(Constants.STATUS_201, Constants.MESSAGE_201));
    }

    @PatchMapping(value = "/update-etat/{idVente}/{etatVente}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Update sale's state",
            responses = {
                    @ApiResponse(responseCode = "201", description = "State updated successfully", content = @Content(schema = @Schema(implementation = VenteDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = InvalidOperationException.class)))
            }
    )
    public ResponseEntity<VenteDto> updateEtatVente(@PathVariable("idVente") Integer idVente, @PathVariable("etatVente") EtatCommande etatVente) {
        VenteDto savedVente = venteService.updateEtatCommande(idVente, etatVente);
        emailSenderService.sendSimpleEmail();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedVente);
    }

    @PatchMapping(value = "/update-quantite/{idVente}/{idLigneVente}/{quantite}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Update quantity of an article in a sale",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Quantity updated successfully", content = @Content(schema = @Schema(implementation = VenteDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = InvalidOperationException.class)))
            }
    )
    public ResponseEntity<VenteDto> updateQuantiteVente(@PathVariable("idVente") Integer idVente, @PathVariable("idLigneVente") Integer idLigneVente, @PathVariable("quantite") BigDecimal quantite){
        VenteDto savedVent = venteService.updateQuantiteCommande(idVente, idLigneVente, quantite);
        emailSenderService.sendSimpleEmail();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedVent);
    }

    @PatchMapping(value = "/update-article/{idVente}/{idLigneVente}/{newIdArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Update article in a sale",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Article updated successfully", content = @Content(schema = @Schema(implementation = VenteDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = InvalidOperationException.class)))
            }
    )
    public ResponseEntity<VenteDto> updateArticle(@PathVariable("idVente") Integer idVente, @PathVariable("idLigneVente") Integer idLigneVente, @PathVariable("newIdArticle") Integer newIdArticle){
        VenteDto savedVente = venteService.updateArticle(idVente, idLigneVente, newIdArticle);
        emailSenderService.sendSimpleEmail();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedVente);
    }

    @PatchMapping(value = "/delete-article/{idVente}/{idLigneVente}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Delete an article from a sale",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Article deleted successfully", content = @Content(schema = @Schema(implementation = VenteDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = InvalidOperationException.class)))
            }
    )
    public ResponseEntity<VenteDto> deleteArticle(@PathVariable("idVente") Integer idVente, @PathVariable("idLigneVente") Integer idLigneVente){
        VenteDto savedVente = venteService.deleteArticle(idVente, idLigneVente);
        emailSenderService.sendSimpleEmail();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedVente);
    }

    @GetMapping(value = "/find/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find sale by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sale found", content = @Content(schema = @Schema(implementation = VenteDto.class))),
                    @ApiResponse(responseCode = "404", description = "Sale not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
            }
    )
    public ResponseEntity<VenteDto> findById(@PathVariable Integer id) {
        VenteDto venteDto = venteService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(venteDto);
    }

    @GetMapping(value = "/find/code/{codeVente}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find sale by code",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sale found", content = @Content(schema = @Schema(implementation = VenteDto.class))),
                    @ApiResponse(responseCode = "404", description = "Sale not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
            }
    )
    public ResponseEntity<VenteDto> findByCodeVente(@PathVariable String codeVente) {
        VenteDto venteDto = venteService.findByCodeVente(codeVente);
        return ResponseEntity.status(HttpStatus.OK).body(venteDto);
    }

    @GetMapping(value = "/find/articles", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find all articles",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Articles found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ArticleDto.class))))
            }
    )
    public ResponseEntity<List<ArticleDto>> findArticles() {
        List<ArticleDto> articleDtos = venteService.getAllArticles();
        return ResponseEntity.status(HttpStatus.OK).body(articleDtos);
    }

    @GetMapping(value = "/find/articles-by-categorie/{idCategorie}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find all articles by category",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Articles found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ArticleDto.class))))
            }
    )
    public ResponseEntity<List<ArticleDto>> findArticlesByTypeAndCategorie(@PathVariable Integer idCategorie) {
        List<ArticleDto> articleDtos = venteService.getAllArticlesByCategorie(idCategorie);
        return ResponseEntity.status(HttpStatus.OK).body(articleDtos);
    }

    @GetMapping(value = "/find/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find all sales",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sales found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = VenteDto.class))))
            }
    )
    public ResponseEntity<List<VenteDto>> findAll() {
        List<VenteDto> venteDtos = venteService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(venteDtos);
    }

    @GetMapping(value = "/find/lignes-vente/{idVente}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find all line items of a sale",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Line items found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LigneVenteDto.class))))
            }
    )
    public ResponseEntity<List<LigneVenteDto>> findAllLigneVentesByVenteId(@PathVariable Integer idVente) {
        List<LigneVenteDto> ligneVenteDtos = venteService.findAllLigneVentesByVenteId(idVente);
        return ResponseEntity.status(HttpStatus.OK).body(ligneVenteDtos);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Operation(
            summary = "Delete a sale by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sale deleted successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
            }
    )
    public ResponseEntity<ResponseDto> delete(@PathVariable Integer id) {
        boolean isDeleted = venteService.delete(id);
        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(Constants.STATUS_200, Constants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(Constants.STATUS_500, Constants.MESSAGE_500));
        }
    }
}
