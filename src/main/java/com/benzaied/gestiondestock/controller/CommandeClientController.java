package com.benzaied.gestiondestock.controller;

import com.benzaied.gestiondestock.dto.CommandeClientDto;
import com.benzaied.gestiondestock.dto.LigneCommandeClientDto;
import com.benzaied.gestiondestock.dto.ResponseDto;
import com.benzaied.gestiondestock.exception.EntityNotFoundException;
import com.benzaied.gestiondestock.exception.InvalidEntityException;
import com.benzaied.gestiondestock.exception.InvalidOperationException;
import com.benzaied.gestiondestock.model.EtatCommande;
import com.benzaied.gestiondestock.services.CommandeClientService;
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
@RequestMapping(path = "/gestiondestock/v1/commandes-clients")
@AllArgsConstructor
@Validated
public class CommandeClientController {

    @Autowired
    private final CommandeClientService commandeClientService;

    @Autowired
    private final EmailSenderService emailSenderService;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a client order", responses = {
            @ApiResponse(responseCode = "201", description = "Client order created successfully", content = @Content(schema = @Schema(implementation = CommandeClientDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = InvalidEntityException.class)))
    })
    public ResponseEntity<CommandeClientDto> save(@Valid @RequestBody CommandeClientDto commandeClientDto) {
        CommandeClientDto savedCommandeClient = commandeClientService.save(commandeClientDto);
        emailSenderService.sendSimpleEmail();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedCommandeClient);
    }

    @PatchMapping(value = "/update-etat/{idCommande}/{etatCommande}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update the status of a client order", responses = {
            @ApiResponse(responseCode = "201", description = "Client order status updated successfully", content = @Content(schema = @Schema(implementation = CommandeClientDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = InvalidOperationException.class)))
    })
    public ResponseEntity<CommandeClientDto> updateEtatCommande(@PathVariable("idCommande") Integer idCommande, @PathVariable("etatCommande") EtatCommande etatCommande) {
        CommandeClientDto commandeClientDto = commandeClientService.updateEtatCommande(idCommande, etatCommande);
        emailSenderService.sendSimpleEmail();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commandeClientDto);
    }

    @PatchMapping(value = "/update-quantite/{idCommande}/{idLigneCommande}/{quantite}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update the quantity of a line item in a client order", responses = {
            @ApiResponse(responseCode = "201", description = "Quantity of line item updated successfully", content = @Content(schema = @Schema(implementation = CommandeClientDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = InvalidOperationException.class)))
    })
    public ResponseEntity<CommandeClientDto> updateQuantiteCommande(@PathVariable("idCommande") Integer idCommande, @PathVariable("idLigneCommande") Integer idLigneCommande, @PathVariable("quantite") BigDecimal quantite){
        CommandeClientDto commandeClientDto = commandeClientService.updateQuantiteCommande(idCommande, idLigneCommande, quantite);
        emailSenderService.sendSimpleEmail();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commandeClientDto);
    }

    @PatchMapping(value = "/update-client/{idCommande}/{idClient}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update the client of a client order", responses = {
            @ApiResponse(responseCode = "201", description = "Client of client order updated successfully", content = @Content(schema = @Schema(implementation = CommandeClientDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = InvalidOperationException.class)))
    })
    public ResponseEntity<CommandeClientDto> updateClient(@PathVariable("idCommande") Integer idCommande, @PathVariable("idClient") Integer idClient){
        CommandeClientDto commandeClientDto = commandeClientService.updateClient(idCommande, idClient);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commandeClientDto);
    }

    @PatchMapping(value = "/update-article/{idCommande}/{idLigneCommande}/{newIdArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update the article of a line item in a client order", responses = {
            @ApiResponse(responseCode = "201", description = "Article of line item updated successfully", content = @Content(schema = @Schema(implementation = CommandeClientDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = InvalidOperationException.class)))
    })
    public ResponseEntity<CommandeClientDto> updateArticle(@PathVariable("idCommande") Integer idCommande, @PathVariable("idLigneCommande") Integer idLigneCommande, @PathVariable("newIdArticle") Integer newIdArticle){
        CommandeClientDto commandeClientDto = commandeClientService.updateArticle(idCommande, idLigneCommande, newIdArticle);
        emailSenderService.sendSimpleEmail();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commandeClientDto);
    }

    @PatchMapping(value = "/delete-article/{idCommande}/{idLigneCommande}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete a line item from a client order", responses = {
            @ApiResponse(responseCode = "201", description = "Line item deleted successfully", content = @Content(schema = @Schema(implementation = CommandeClientDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = InvalidOperationException.class)))
    })
    public ResponseEntity<CommandeClientDto> deleteArticle(@PathVariable("idCommande") Integer idCommande, @PathVariable("idLigneCommande") Integer idLigneCommande){
        CommandeClientDto commandeClientDto = commandeClientService.deleteArticle(idCommande, idLigneCommande);
        emailSenderService.sendSimpleEmail();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commandeClientDto);
    }

    @GetMapping(value = "/find/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find a client order by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Client order found", content = @Content(schema = @Schema(implementation = CommandeClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Client order not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
    })
    public ResponseEntity<CommandeClientDto> findById(@PathVariable Integer id) {
        CommandeClientDto commandeClientDto = commandeClientService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(commandeClientDto);
    }

    @GetMapping(value = "/find/code/{codeCC}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find a client order by code", responses = {
            @ApiResponse(responseCode = "200", description = "Client order found", content = @Content(schema = @Schema(implementation = CommandeClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Client order not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
    })
    public ResponseEntity<CommandeClientDto> findByCodeCC(@PathVariable String codeCC) {
        CommandeClientDto commandeClientDto = commandeClientService.findByCodeCC(codeCC);
        return ResponseEntity.status(HttpStatus.OK).body(commandeClientDto);
    }

    @GetMapping(value = "/find/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find all client orders", responses = {
            @ApiResponse(responseCode = "200", description = "Client orders found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CommandeClientDto.class))))
    })
    public ResponseEntity<List<CommandeClientDto>> findAll() {
        List<CommandeClientDto> commandeClientDtos = commandeClientService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(commandeClientDtos);
    }

    @GetMapping(value = "/find/lignes-commande/{idCommande}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find all line items for a client order", responses = {
            @ApiResponse(responseCode = "200", description = "Line items found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LigneCommandeClientDto.class)))),
            @ApiResponse(responseCode = "404", description = "Line items not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
    })
    public ResponseEntity<List<LigneCommandeClientDto>> findAllLigneCommandeClientsByCommandeClientId(@PathVariable Integer idCommande) {
        List<LigneCommandeClientDto> ligneCommandeClientDtos = commandeClientService.findAllLigneCommandeClientsByCommandeClientId(idCommande);
        return ResponseEntity.status(HttpStatus.OK).body(ligneCommandeClientDtos);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Operation(summary = "Delete a client order by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Client order deleted successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    public ResponseEntity<ResponseDto> delete(@PathVariable Integer id) {
        commandeClientService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(Constants.STATUS_200, Constants.MESSAGE_200));
    }
}
