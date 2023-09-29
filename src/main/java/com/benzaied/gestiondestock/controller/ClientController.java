package com.benzaied.gestiondestock.controller;

import com.benzaied.gestiondestock.dto.*;
import com.benzaied.gestiondestock.exception.EntityNotFoundException;
import com.benzaied.gestiondestock.exception.InvalidEntityException;
import com.benzaied.gestiondestock.services.ClientService;
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

import java.util.List;

@RestController
@RequestMapping(path = "/gestiondestock/v1/clients")
@AllArgsConstructor
@Validated
public class ClientController {

    @Autowired
    private final ClientService clientService;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a client", responses = {
            @ApiResponse(responseCode = "201", description = "Client created successfully", content = @Content(schema = @Schema(implementation = ClientDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = InvalidEntityException.class)))
    })
    public ResponseEntity<ClientDto> save(@Valid @RequestBody ClientDto clientDto) {
        ClientDto savedClient = clientService.save(clientDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedClient);
    }

    @GetMapping(value = "/find/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find client by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Client found", content = @Content(schema = @Schema(implementation = ClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Client not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
    })
    public ResponseEntity<ClientDto> findById(@PathVariable Integer id) {
        ClientDto clientDto = clientService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(clientDto);
    }

    @GetMapping(value = "/find/nom-prenom", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find client by name and surname", responses = {
            @ApiResponse(responseCode = "200", description = "Client found", content = @Content(schema = @Schema(implementation = ClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Client not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
    })
    public ResponseEntity<ClientDto> findByNomAndPrenomIgnoreCase(
            @RequestParam String nom, @RequestParam String prenom) {
        ClientDto clientDto = clientService.findByNomAndPrenomIgnoreCase(nom, prenom);
        return ResponseEntity.status(HttpStatus.OK).body(clientDto);
    }

    @GetMapping(value = "/find/nom/{nom}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find client by last name", responses = {
            @ApiResponse(responseCode = "200", description = "Client found", content = @Content(schema = @Schema(implementation = ClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Client not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
    })
    public ResponseEntity<ClientDto> findByNom(@PathVariable String nom) {
        ClientDto clientDto = clientService.findByNomIgnoreCase(nom);
        return ResponseEntity.status(HttpStatus.OK).body(clientDto);
    }

    @GetMapping(value = "/find/prenom/{prenom}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find client by first name", responses = {
            @ApiResponse(responseCode = "200", description = "Client found", content = @Content(schema = @Schema(implementation = ClientDto.class))),
            @ApiResponse(responseCode = "404", description = "Client not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
    })
    public ResponseEntity<ClientDto> findByPrenom(@PathVariable String prenom) {
        ClientDto clientDto = clientService.findByPrenomIgnoreCase(prenom);
        return ResponseEntity.status(HttpStatus.OK).body(clientDto);
    }

    @GetMapping(value = "/find/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find all clients", responses = {
            @ApiResponse(responseCode = "200", description = "Clients found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ClientDto.class))))
    })
    public ResponseEntity<List<ClientDto>> findAll() {
        List<ClientDto> clientDtos = clientService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(clientDtos);
    }

    @GetMapping(value = "/find/articles", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find all articles for a client by type", responses = {
            @ApiResponse(responseCode = "200", description = "Articles found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ArticleDto.class))))
    })
    public ResponseEntity<List<ArticleDto>> findAllArticlesByTypeClient(@Valid @RequestBody ClientDto clientDto) {
        List<ArticleDto> articleDtos = clientService.findAllArticlesByTypeClient(clientDto);
        return ResponseEntity.status(HttpStatus.OK).body(articleDtos);
    }

    @GetMapping(value = "/find/articles-categorie/{idCategorie}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find all articles for a client by type and category", responses = {
            @ApiResponse(responseCode = "200", description = "Articles found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ArticleDto.class))))
    })
    public ResponseEntity<List<ArticleDto>> findAllArticlesByTypeClientAndCategorie(@Valid @RequestBody ClientDto clientDto, @PathVariable("idCategorie") Integer idCategorie) {
        List<ArticleDto> articleDtos = clientService.getArticlesForClientByTypeAndCategory(clientDto, idCategorie);
        return ResponseEntity.status(HttpStatus.OK).body(articleDtos);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Operation(summary = "Delete a client by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Client deleted successfully")
    })
    public ResponseEntity<ResponseDto> delete(@PathVariable Integer id) {
        clientService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(Constants.STATUS_200, Constants.MESSAGE_200));
    }

    @GetMapping(value = "/find/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find client by email",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Client found", content = @Content(schema = @Schema(implementation = ClientDto.class))),
                    @ApiResponse(responseCode = "404", description = "Client not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
            }
    )
    public ResponseEntity<ClientDto> findByEmail(@PathVariable String email) {
        ClientDto clientDto = clientService.findByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(clientDto);
    }

    @PatchMapping(value = "/changer-mot-de-passe", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Change client password",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Password changed successfully", content = @Content(schema = @Schema(implementation = ClientDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = InvalidEntityException.class)))
            }
    )
    public ResponseEntity<ClientDto> changerMotDePasse(@RequestBody ChangerMotDePasseUtilisateurDto changerMDPDto){
        ClientDto clientWithChangedPassword = clientService.changerMotDePasse(changerMDPDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clientWithChangedPassword);
    }

}
