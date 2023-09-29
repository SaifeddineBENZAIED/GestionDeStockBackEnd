package com.benzaied.gestiondestock.controller;

import com.benzaied.gestiondestock.dto.ChangerMotDePasseUtilisateurDto;
import com.benzaied.gestiondestock.dto.ResponseDto;
import com.benzaied.gestiondestock.dto.UtilisateurDto;
import com.benzaied.gestiondestock.exception.EntityNotFoundException;
import com.benzaied.gestiondestock.exception.InvalidEntityException;
import com.benzaied.gestiondestock.services.UtilisateurService;
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
@RequestMapping(path = "/gestiondestock/v1/utilisateurs")
@AllArgsConstructor
@Validated
public class UtilisateurController {

    @Autowired
    private final UtilisateurService utilisateurService;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Create a user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(schema = @Schema(implementation = UtilisateurDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = InvalidEntityException.class)))
            }
    )
    public ResponseEntity<UtilisateurDto> save(@Valid @RequestBody UtilisateurDto utilisateurDto) {
        UtilisateurDto savedUtilisateur = utilisateurService.save(utilisateurDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedUtilisateur);
    }

    @GetMapping(value = "/find/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find user by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UtilisateurDto.class))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
            }
    )
    public ResponseEntity<UtilisateurDto> findById(@PathVariable Integer id) {
        UtilisateurDto utilisateurDto = utilisateurService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(utilisateurDto);
    }

    @GetMapping(value = "/find/nom-prenom", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find user by last name and first name",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UtilisateurDto.class))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
            }
    )
    public ResponseEntity<UtilisateurDto> findByNomAndPrenomIgnoreCase(
            @RequestParam String nom,
            @RequestParam String prenom) {
        UtilisateurDto utilisateurDto = utilisateurService.findByNomAndPrenomIgnoreCase(nom, prenom);
        return ResponseEntity.status(HttpStatus.OK).body(utilisateurDto);
    }

    @GetMapping(value = "/find/nom/{nom}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find user by last name",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UtilisateurDto.class))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
            }
    )
    public ResponseEntity<UtilisateurDto> findByNomIgnoreCase(@PathVariable String nom) {
        UtilisateurDto utilisateurDto = utilisateurService.findByNomIgnoreCase(nom);
        return ResponseEntity.status(HttpStatus.OK).body(utilisateurDto);
    }

    @GetMapping(value = "/find/prenom/{prenom}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find user by first name",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UtilisateurDto.class))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
            }
    )
    public ResponseEntity<UtilisateurDto> findByPrenomIgnoreCase(@PathVariable String prenom) {
        UtilisateurDto utilisateurDto = utilisateurService.findByPrenomIgnoreCase(prenom);
        return ResponseEntity.status(HttpStatus.OK).body(utilisateurDto);
    }

    @GetMapping(value = "/find/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find all users",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Users found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UtilisateurDto.class))))
            }
    )
    public ResponseEntity<List<UtilisateurDto>> findAll() {
        List<UtilisateurDto> utilisateurDtos = utilisateurService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(utilisateurDtos);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Operation(
            summary = "Delete user by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User deleted successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
            }
    )
    public ResponseEntity<ResponseDto> delete(@PathVariable Integer id) {
        boolean isDeleted = utilisateurService.delete(id);
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

    @GetMapping(value = "/find/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find user by email",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UtilisateurDto.class))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
            }
    )
    public ResponseEntity<UtilisateurDto> findByEmail(@PathVariable String email) {
        UtilisateurDto utilisateurDto = utilisateurService.findByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(utilisateurDto);
    }

    @PatchMapping(value = "/changer-mot-de-passe", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Change user password",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Password changed successfully", content = @Content(schema = @Schema(implementation = UtilisateurDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = InvalidEntityException.class)))
            }
    )
    public ResponseEntity<UtilisateurDto> changerMotDePasse(@RequestBody ChangerMotDePasseUtilisateurDto changerMDPDto){
        UtilisateurDto utilisateurWithChangedPassword = utilisateurService.changerMotDePasse(changerMDPDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(utilisateurWithChangedPassword);
    }

}
