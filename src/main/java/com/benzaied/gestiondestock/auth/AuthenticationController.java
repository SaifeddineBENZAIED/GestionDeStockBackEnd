package com.benzaied.gestiondestock.auth;

import com.benzaied.gestiondestock.model.auth.ExtendedUser;
import com.benzaied.gestiondestock.services.JwtService;
import com.benzaied.gestiondestock.services.auth.ApplicationUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequestMapping("/gestiondestock/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register a new user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User registered successfully",
                            content = @Content(schema = @Schema(implementation = AuthenticationResponse.class)))
            })
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Authenticate user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Authentication successful",
                            content = @Content(schema = @Schema(implementation = AuthenticationResponse.class)))
            })
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping(value = "/refresh-token", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Refresh authentication token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token refreshed successfully")
            })
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }

    @PostMapping(value = "/register-client", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register a new client",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Client registered successfully",
                            content = @Content(schema = @Schema(implementation = AuthenticationResponse.class)))
            })
    public ResponseEntity<AuthenticationResponse> registerClient(@RequestBody RegisterClientRequest request){
        return ResponseEntity.ok(authenticationService.registerClient(request));
    }

    @PostMapping(value = "/authenticate-client", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Authenticate client",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Authentication successful",
                            content = @Content(schema = @Schema(implementation = AuthenticationResponse.class)))
            })
    public ResponseEntity<AuthenticationResponse> authenticateClient(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.authenticateClient(request));
    }

    @PostMapping(value = "/refresh-token-client", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Refresh authentication token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token refreshed successfully")
            })
    public void refreshTokenClient(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshTokenForClient(request, response);
    }

}
