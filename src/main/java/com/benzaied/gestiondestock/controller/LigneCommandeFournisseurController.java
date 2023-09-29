package com.benzaied.gestiondestock.controller;

import com.benzaied.gestiondestock.dto.LigneCommandeFournisseurDto;
import com.benzaied.gestiondestock.dto.ResponseDto;
import com.benzaied.gestiondestock.exception.InvalidEntityException;
import com.benzaied.gestiondestock.services.LigneCommandeFournisseurService;
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

import java.util.List;

@RestController
@RequestMapping(path = "/gestiondestock/v1/ligneCommandeFournisseurs")
@AllArgsConstructor
@Validated
public class LigneCommandeFournisseurController {

    @Autowired
    private final LigneCommandeFournisseurService ligneCommandeFournisseurService;

    @Autowired
    private final EmailSenderService emailSenderService;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Create a new line item for a supplier order",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Line item created successfully", content = @Content(schema = @Schema(implementation = LigneCommandeFournisseurDto.class)))
            }
    )
    public ResponseEntity<LigneCommandeFournisseurDto> save(@Valid @RequestBody LigneCommandeFournisseurDto ligneCommandeFournisseurDto) {
        LigneCommandeFournisseurDto savedLigneCommandeFournisseur = ligneCommandeFournisseurService.save(ligneCommandeFournisseurDto);
        emailSenderService.sendSimpleEmail();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedLigneCommandeFournisseur);
    }

    @GetMapping(value = "/find/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find a line item by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Line item found")
            }
    )
    public ResponseEntity<LigneCommandeFournisseurDto> findById(@PathVariable Integer id) {
        LigneCommandeFournisseurDto ligneCommandeFournisseurDto = ligneCommandeFournisseurService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(ligneCommandeFournisseurDto);
    }

    @GetMapping(value = "/find/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Find all line items",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Line items found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LigneCommandeFournisseurDto.class))))
            }
    )
    public ResponseEntity<List<LigneCommandeFournisseurDto>> findAll() {
        List<LigneCommandeFournisseurDto> ligneCommandeFournisseurDtos = ligneCommandeFournisseurService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(ligneCommandeFournisseurDtos);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Operation(
            summary = "Delete a line item by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Line item deleted successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
            }
    )
    public ResponseEntity<ResponseDto> delete(@PathVariable Integer id) {
        boolean isDeleted = ligneCommandeFournisseurService.delete(id);
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
