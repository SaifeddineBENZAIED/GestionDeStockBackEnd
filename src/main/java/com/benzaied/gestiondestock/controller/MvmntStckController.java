package com.benzaied.gestiondestock.controller;

import com.benzaied.gestiondestock.dto.MvmntStckDto;
import com.benzaied.gestiondestock.dto.ResponseDto;
import com.benzaied.gestiondestock.exception.InvalidEntityException;
import com.benzaied.gestiondestock.services.MvmntStckService;
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

@RestController
@RequestMapping(path = "/gestiondestock/v1/mvmntStocks")
@AllArgsConstructor
@Validated
public class MvmntStckController {

    @Autowired
    private final MvmntStckService mvmntStckService;

    @Autowired
    private final EmailSenderService emailSenderService;

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Create a new stock movement",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Stock movement created successfully", content = @Content(schema = @Schema(implementation = MvmntStckDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = InvalidEntityException.class)))
            }
    )
    public ResponseEntity<MvmntStckDto> save(@Valid @RequestBody MvmntStckDto mvmntStckDto) {
        MvmntStckDto savedMvmntStck = mvmntStckService.save(mvmntStckDto);
        emailSenderService.sendSimpleEmail();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedMvmntStck);
    }

    @GetMapping(value = "/real-stock/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get real stock quantity of an article",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Real stock quantity found", content = @Content(schema = @Schema(type = "number")))
            }
    )
    public ResponseEntity<BigDecimal> realStock(@PathVariable Integer idArticle) {
        BigDecimal stockReelArticle = mvmntStckService.stockReelArticle(idArticle);
        return ResponseEntity.status(HttpStatus.OK).body(stockReelArticle);
    }

    @GetMapping(value = "/filter/article/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get stock movements of an article",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Stock movements found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MvmntStckDto.class))))
            }
    )
    public ResponseEntity<List<MvmntStckDto>> mvmntDeStockArticle(@PathVariable Integer idArticle) {
        List<MvmntStckDto> mvmntStckDtos = mvmntStckService.mvmntStckArticle(idArticle);
        return ResponseEntity.status(HttpStatus.OK).body(mvmntStckDtos);
    }

    @PostMapping(value = "/entree", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Record stock entry",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Stock entry recorded successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
            }
    )
    public ResponseEntity<MvmntStckDto> entreeStock(@RequestBody MvmntStckDto mvmntStckDto) {
        MvmntStckDto mvmntStckDto1 = mvmntStckService.entreeStock(mvmntStckDto);
        emailSenderService.sendSimpleEmail();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mvmntStckDto1);
    }

    @PostMapping(value = "/sortie", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Record stock exit",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Stock exit recorded successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
            }
    )
    public ResponseEntity<MvmntStckDto> sortieStock(@RequestBody MvmntStckDto mvmntStckDto) {
        MvmntStckDto mvmntStckDto1 = mvmntStckService.sortieStock(mvmntStckDto);
        emailSenderService.sendSimpleEmail();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mvmntStckDto1);
    }

    @PostMapping(value = "/correction-pos", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Record positive stock correction",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Positive stock correction recorded successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
            }
    )
    public ResponseEntity<MvmntStckDto> correctionPos(@RequestBody MvmntStckDto mvmntStckDto) {
        MvmntStckDto mvmntStckDto1 = mvmntStckService.correctionStockPos(mvmntStckDto);
        emailSenderService.sendSimpleEmail();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mvmntStckDto1);
    }

    @PostMapping(value = "/correction-neg", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Record negative stock correction",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Negative stock correction recorded successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
            }
    )
    public ResponseEntity<MvmntStckDto> correctionNeg(@RequestBody MvmntStckDto mvmntStckDto) {
        MvmntStckDto mvmntStckDto1 = mvmntStckService.correctionStockNeg(mvmntStckDto);
        emailSenderService.sendSimpleEmail();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mvmntStckDto1);
    }

    /*@DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<ResponseDto> delete(@PathVariable Integer id) {
        boolean isDeleted = mvmntStckService.delete(id);
        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(Constants.STATUS_200, Constants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(Constants.STATUS_500, Constants.MESSAGE_500));
        }
    }*/
}
