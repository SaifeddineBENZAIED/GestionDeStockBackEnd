package com.benzaied.gestiondestock.controller;

import com.benzaied.gestiondestock.dto.ArticleDto;
import com.benzaied.gestiondestock.dto.FournisseurDto;
import com.benzaied.gestiondestock.dto.ResponseDto;
import com.benzaied.gestiondestock.exception.EntityNotFoundException;
import com.benzaied.gestiondestock.exception.InvalidEntityException;
import com.benzaied.gestiondestock.model.Fournisseur;
import com.benzaied.gestiondestock.services.FournisseurService;
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
import java.util.Map;

@RestController
@RequestMapping(path = "/gestiondestock/v1/fournisseurs")
@AllArgsConstructor
@Validated
public class FournisseurController {

    @Autowired
    private final FournisseurService fournisseurService;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Create a new supplier",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Supplier created successfully", content = @Content(schema = @Schema(implementation = FournisseurDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = InvalidEntityException.class)))
            }
    )
    public ResponseEntity<FournisseurDto> save(@Valid @RequestBody FournisseurDto fournisseurDto) {
        FournisseurDto savedFournisseur = fournisseurService.save(fournisseurDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedFournisseur);
    }

    @GetMapping(value = "/quantite-totale/articles/{fournisseurId}/{entrepriseId}/{articleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get total supplied quantity of an article by supplier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Total quantity found", content = @Content(schema = @Schema(type = "number")))
            }
    )
    public ResponseEntity<BigDecimal> getQuantiteTotaleFournie(@PathVariable("fournisseurId") Integer fournisseurId,
                                                               @PathVariable("articleId") Integer articleId,
                                                               @PathVariable("entrepriseId") Integer idEntreprise) {
        BigDecimal quantiteTotaleArticle = fournisseurService.getQuantiteTotaleFournieArticle(fournisseurId, idEntreprise, articleId);
        return ResponseEntity.status(HttpStatus.OK).body(quantiteTotaleArticle);
    }

    @GetMapping(value = "/quantite-totale/{fournisseurId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get total supplied quantities by supplier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quantities found", content = @Content(schema = @Schema(implementation = Map.class)))
            }
    )
    public ResponseEntity<Map<ArticleDto, BigDecimal>> getQuantitesFournies(@PathVariable("fournisseurId") Integer fournisseurId) {
        FournisseurDto fournisseurDto = fournisseurService.findById(fournisseurId);
        Map<ArticleDto, BigDecimal> quantitesParArticle = fournisseurService.getQuantiteFournieParArticle(FournisseurDto.toEntity(fournisseurDto));
        return ResponseEntity.status(HttpStatus.OK).body(quantitesParArticle);
    }

    @GetMapping(value = "/find/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find supplier by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Supplier found", content = @Content(schema = @Schema(implementation = FournisseurDto.class))),
                    @ApiResponse(responseCode = "404", description = "Supplier not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
            }
    )
    public ResponseEntity<FournisseurDto> findById(@PathVariable Integer id) {
        FournisseurDto fournisseurDto = fournisseurService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(fournisseurDto);
    }

    @GetMapping(value = "/find/nom-prenom", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find supplier by name and last name",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Supplier found", content = @Content(schema = @Schema(implementation = FournisseurDto.class))),
                    @ApiResponse(responseCode = "404", description = "Supplier not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
            }
    )
    public ResponseEntity<FournisseurDto> findByNomAndPrenomIgnoreCase(
            @RequestParam String nom, @RequestParam String prenom) {
        FournisseurDto fournisseurDto = fournisseurService.findByNomAndPrenomIgnoreCase(nom, prenom);
        return ResponseEntity.status(HttpStatus.OK).body(fournisseurDto);
    }

    @GetMapping(value = "/find/nom/{nom}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find supplier by name",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Supplier found", content = @Content(schema = @Schema(implementation = FournisseurDto.class))),
                    @ApiResponse(responseCode = "404", description = "Supplier not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
            }
    )
    public ResponseEntity<FournisseurDto> findByNomIgnoreCase(@PathVariable String nom) {
        FournisseurDto fournisseurDto = fournisseurService.findByNomIgnoreCase(nom);
        return ResponseEntity.status(HttpStatus.OK).body(fournisseurDto);
    }

    @GetMapping(value = "/find/prenom/{prenom}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find supplier by last name",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Supplier found", content = @Content(schema = @Schema(implementation = FournisseurDto.class))),
                    @ApiResponse(responseCode = "404", description = "Supplier not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
            }
    )
    public ResponseEntity<FournisseurDto> findByPrenomIgnoreCase(@PathVariable String prenom) {
        FournisseurDto fournisseurDto = fournisseurService.findByPrenomIgnoreCase(prenom);
        return ResponseEntity.status(HttpStatus.OK).body(fournisseurDto);
    }

    @GetMapping(value = "/find/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find all suppliers",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Suppliers found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = FournisseurDto.class))))
            }
    )
    public ResponseEntity<List<FournisseurDto>> findAll() {
        List<FournisseurDto> fournisseurDtos = fournisseurService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(fournisseurDtos);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Operation(
            summary = "Delete a supplier by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Supplier deleted successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
            }
    )
    public ResponseEntity<ResponseDto> delete(@PathVariable Integer id) {
        boolean isDeleted = fournisseurService.delete(id);
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
