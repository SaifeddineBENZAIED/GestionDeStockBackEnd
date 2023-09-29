package com.benzaied.gestiondestock.controller;

import com.benzaied.gestiondestock.exception.InvalidOperationException;
import com.benzaied.gestiondestock.services.strategy.StrategyImageContext;
import com.flickr4java.flickr.FlickrException;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = "/gestiondestock/v1/images")
@AllArgsConstructor
@Validated
public class ImageController {

    @Autowired
    private StrategyImageContext strategyImageContext;

    @PostMapping(value = "/save/{id}/{titre}/{context}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Save an image",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Image saved successfully",
                            content = @Content(schema = @Schema(implementation = Object.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @Content(schema = @Schema(implementation = InvalidOperationException.class)))
            })
    public ResponseEntity<Object> save(@PathVariable("id") Integer id, @PathVariable("context") String context, @RequestPart("file") MultipartFile file, @PathVariable("titre") String titre) throws IOException, FlickrException {
        Object savedImage = strategyImageContext.saveImage(context,id,file.getInputStream(),titre);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedImage);
    }

}
