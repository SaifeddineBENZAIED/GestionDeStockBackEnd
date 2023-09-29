package com.benzaied.gestiondestock.controller;

import com.benzaied.gestiondestock.dto.EntrepriseDto;
import com.benzaied.gestiondestock.exception.EntityNotFoundException;
import com.benzaied.gestiondestock.exception.InvalidEntityException;
import com.benzaied.gestiondestock.services.EntrepriseService;
import com.benzaied.gestiondestock.dto.ResponseDto;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/gestiondestock/v1/entreprises")
@AllArgsConstructor
@Validated
public class EntrepriseController {

    @Autowired
    private final EntrepriseService entrepriseService;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Create a new company",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Company created successfully", content = @Content(schema = @Schema(implementation = EntrepriseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = InvalidEntityException.class)))
            }
    )
    public ResponseEntity<EntrepriseDto> save(@RequestBody EntrepriseDto entrepriseDto) {
        EntrepriseDto savedEntreprise = entrepriseService.save(entrepriseDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedEntreprise);
    }

    @GetMapping(value = "/find/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find company by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Company found", content = @Content(schema = @Schema(implementation = EntrepriseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Company not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
            }
    )
    public ResponseEntity<EntrepriseDto> findById(@PathVariable Integer id) {
        EntrepriseDto entrepriseDto = entrepriseService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(entrepriseDto);
    }

    @GetMapping(value = "/find/nom/{nom}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find company by name",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Company found", content = @Content(schema = @Schema(implementation = EntrepriseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Company not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
            }
    )
    public ResponseEntity<EntrepriseDto> findByNom(@PathVariable String nom) {
        EntrepriseDto entrepriseDto = entrepriseService.findByNomIgnoreCase(nom);
        return ResponseEntity.status(HttpStatus.OK).body(entrepriseDto);
    }

    @GetMapping(value = "/find/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find all companies",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Companies found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EntrepriseDto.class))))
            }
    )
    public ResponseEntity<List<EntrepriseDto>> findAll() {
        List<EntrepriseDto> entrepriseDtos = entrepriseService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(entrepriseDtos);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Operation(
            summary = "Delete a company by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Company deleted successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
            }
    )
    public ResponseEntity<ResponseDto> delete(@PathVariable Integer id) {
        boolean isDeleted = entrepriseService.delete(id);
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

    @DeleteMapping(value = "/delete/nom/{nom}")
    @Operation(
            summary = "Delete a company by name",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Company deleted successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
            }
    )
    public ResponseEntity<ResponseDto> deleteByNom(@PathVariable String nom) {
        boolean isDeleted = entrepriseService.deleteByNom(nom);
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
