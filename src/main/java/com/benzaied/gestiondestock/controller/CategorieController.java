package com.benzaied.gestiondestock.controller;

import com.benzaied.gestiondestock.dto.CategorieDto;
import com.benzaied.gestiondestock.dto.ResponseDto;
import com.benzaied.gestiondestock.exception.EntityNotFoundException;
import com.benzaied.gestiondestock.exception.InvalidEntityException;
import com.benzaied.gestiondestock.services.CategorieService;
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
@RequestMapping(path = "/gestiondestock/v1/categories")
@AllArgsConstructor
@Validated
public class CategorieController {

    @Autowired
    private final CategorieService categorieService;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a category", responses = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = InvalidEntityException.class)))
    })
    public ResponseEntity<CategorieDto> save(@Valid @RequestBody CategorieDto categorieDto) {
        CategorieDto savedCategorie = categorieService.save(categorieDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedCategorie);
    }

    @GetMapping(value = "/find/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find category by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Category found", content = @Content(schema = @Schema(implementation = CategorieDto.class))),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
    })
    public ResponseEntity<CategorieDto> findById(@PathVariable Integer id) {
        CategorieDto categorieDto = categorieService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(categorieDto);
    }

    @GetMapping(value = "/find/code/{codeCategorie}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find category by code", responses = {
            @ApiResponse(responseCode = "200", description = "Category found", content = @Content(schema = @Schema(implementation = CategorieDto.class))),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
    })
    public ResponseEntity<CategorieDto> findByCodeCategorie(@PathVariable String codeCategorie) {
        CategorieDto categorieDto = categorieService.findByCodeCategorie(codeCategorie);
        return ResponseEntity.status(HttpStatus.OK).body(categorieDto);
    }

    @GetMapping(value = "/find/nom/{nomCategorie}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find category by name", responses = {
            @ApiResponse(responseCode = "200", description = "Category found", content = @Content(schema = @Schema(implementation = CategorieDto.class))),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content(schema = @Schema(implementation = EntityNotFoundException.class)))
    })
    public ResponseEntity<CategorieDto> findBynomCategorie(@PathVariable String nomCategorie) {
        CategorieDto categorieDto = categorieService.findByNomCategorie(nomCategorie);
        return ResponseEntity.status(HttpStatus.OK).body(categorieDto);
    }

    @GetMapping(value = "/find/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find all categories", responses = {
            @ApiResponse(responseCode = "200", description = "Categories found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategorieDto.class))))
    })
    public ResponseEntity<List<CategorieDto>> findAll() {
        List<CategorieDto> categorieDtos = categorieService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(categorieDtos);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Operation(summary = "Delete a category by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    public ResponseEntity<ResponseDto> delete(@PathVariable Integer id) {
        boolean isDeleted = categorieService.delete(id);
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
