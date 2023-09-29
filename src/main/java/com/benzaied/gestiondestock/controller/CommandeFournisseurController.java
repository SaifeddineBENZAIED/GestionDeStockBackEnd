package com.benzaied.gestiondestock.controller;

import com.benzaied.gestiondestock.dto.*;
import com.benzaied.gestiondestock.exception.EntityNotFoundException;
import com.benzaied.gestiondestock.exception.InvalidEntityException;
import com.benzaied.gestiondestock.exception.InvalidOperationException;
import com.benzaied.gestiondestock.model.EtatCommande;
import com.benzaied.gestiondestock.services.CommandeFournisseurService;
import com.benzaied.gestiondestock.services.email.EmailSenderService;
import com.benzaied.gestiondestock.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static com.benzaied.gestiondestock.utils.Constants.APP_ROOT;

@RestController
@RequestMapping(path = "/gestiondestock/v1/commandes-fournisseurs")
@AllArgsConstructor
public class CommandeFournisseurController {

    @Autowired
    private final CommandeFournisseurService commandeFournisseurService;

    @Autowired
    private final EmailSenderService emailSenderService;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Create a supplier order",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Supplier order created successfully", content = @Content(schema = @Schema(implementation = CommandeFournisseurDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = InvalidEntityException.class)))
            }
    )
    public ResponseEntity<CommandeFournisseurDto> save(@RequestBody CommandeFournisseurDto commandeFournisseurDto) {
        CommandeFournisseurDto savedCommandeFournisseur = commandeFournisseurService.save(commandeFournisseurDto);
        emailSenderService.sendSimpleEmail();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedCommandeFournisseur);
    }

    @PatchMapping(value = "/update-etat/{idCommande}/{etatCommande}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Update the status of a supplier order",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Supplier order status updated successfully", content = @Content(schema = @Schema(implementation = CommandeFournisseurDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = InvalidOperationException.class)))
            }
    )
    public ResponseEntity<CommandeFournisseurDto> updateEtatCommande(@PathVariable("idCommande") Integer idCommande, @PathVariable("etatCommande") EtatCommande etatCommande) {
        CommandeFournisseurDto commandeFournisseurDto = commandeFournisseurService.updateEtatCommande(idCommande, etatCommande);
        emailSenderService.sendSimpleEmail();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commandeFournisseurDto);
    }

    @PatchMapping(value = "/update-quantite/{idCommande}/{idLigneCommande}/{quantite}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Update the quantity of an order line in a supplier order",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quantity updated successfully", content = @Content(schema = @Schema(implementation = CommandeFournisseurDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = InvalidOperationException.class)))
            }
    )
    public ResponseEntity<CommandeFournisseurDto> updateQuantite(@PathVariable Integer idCommande, @PathVariable Integer idLigneCommande, @PathVariable BigDecimal quantite) {
        CommandeFournisseurDto updatedDto = commandeFournisseurService.updateQuantiteCommande(idCommande, idLigneCommande, quantite);
        emailSenderService.sendSimpleEmail();
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }

    @PatchMapping(value = "/update-fournisseur/{idCommande}/{idFournisseur}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Update the supplier of a supplier order",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Supplier order updated successfully", content = @Content(schema = @Schema(implementation = CommandeFournisseurDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = InvalidOperationException.class)))
            }
    )
    public ResponseEntity<CommandeFournisseurDto> updateFournisseur(@PathVariable Integer idCommande, @PathVariable Integer idFournisseur) {
        CommandeFournisseurDto updatedDto = commandeFournisseurService.updateFournisseur(idCommande, idFournisseur);
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }

    @PatchMapping(value = "/update-article/{idCommande}/{idLigneCommande}/{newIdArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Update the article of an order line in a supplier order",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Article updated successfully", content = @Content(schema = @Schema(implementation = CommandeFournisseurDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = InvalidOperationException.class)))
            }
    )
    public ResponseEntity<CommandeFournisseurDto> updateArticle(@PathVariable Integer idCommande, @PathVariable Integer idLigneCommande, @PathVariable Integer newIdArticle) {
        CommandeFournisseurDto updatedDto = commandeFournisseurService.updateArticle(idCommande, idLigneCommande, newIdArticle);
        emailSenderService.sendSimpleEmail();
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }

    @PatchMapping(value = "/delete-article/{idCommande}/{idLigneCommande}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Delete an article from a supplier order",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Article deleted successfully", content = @Content(schema = @Schema(implementation = CommandeFournisseurDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = InvalidOperationException.class)))
            }
    )
    public ResponseEntity<CommandeFournisseurDto> deleteArticle(@PathVariable Integer idCommande, @PathVariable Integer idLigneCommande) {
        CommandeFournisseurDto updatedDto = commandeFournisseurService.deleteArticle(idCommande, idLigneCommande);
        emailSenderService.sendSimpleEmail();
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }

    @GetMapping(value = "/find/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find a supplier order by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Supplier order found", content = @Content(schema = @Schema(implementation = CommandeFournisseurDto.class))),
                    @ApiResponse(responseCode = "404", description = "Supplier order not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
            }
    )
    public ResponseEntity<CommandeFournisseurDto> findById(@PathVariable Integer id) {
        CommandeFournisseurDto commandeFournisseurDto = commandeFournisseurService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(commandeFournisseurDto);
    }

    @GetMapping(value = "/find/code/{codeCF}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find a supplier order by code",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Supplier order found", content = @Content(schema = @Schema(implementation = CommandeFournisseurDto.class))),
                    @ApiResponse(responseCode = "404", description = "Supplier order not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
            }
    )
    public ResponseEntity<CommandeFournisseurDto> findByCodeCF(@PathVariable String codeCF) {
        CommandeFournisseurDto commandeFournisseurDto = commandeFournisseurService.findByCodeCF(codeCF);
        return ResponseEntity.status(HttpStatus.OK).body(commandeFournisseurDto);
    }

    @GetMapping(value = "/find/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find all supplier orders",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Supplier orders found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CommandeFournisseurDto.class))))
            }
    )
    public ResponseEntity<List<CommandeFournisseurDto>> findAll() {
        List<CommandeFournisseurDto> commandesFournisseurs = commandeFournisseurService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(commandesFournisseurs);
    }

    @GetMapping(value = "/find/lignes-commande/{idCommande}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find all order lines of a supplier order",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order lines found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LigneCommandeFournisseurDto.class)))),
                    @ApiResponse(responseCode = "404", description = "Supplier order not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
            }
    )
    public ResponseEntity<List<LigneCommandeFournisseurDto>> findAllLigneCommandeFournisseursByCommandeFournisseurId(@PathVariable Integer idCommande) {
        List<LigneCommandeFournisseurDto> ligneCommandeFournisseurDtos = commandeFournisseurService.findAllLigneCommandeFournisseursByCommandeFournisseurId(idCommande);
        return ResponseEntity.status(HttpStatus.OK).body(ligneCommandeFournisseurDtos);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Operation(
            summary = "Delete a supplier order by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Supplier order deleted successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = InvalidOperationException.class)))
            }
    )
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        commandeFournisseurService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
