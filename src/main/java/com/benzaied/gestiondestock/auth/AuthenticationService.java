package com.benzaied.gestiondestock.auth;

import com.benzaied.gestiondestock.Repository.ClientRepository;
import com.benzaied.gestiondestock.Repository.UtilisateurRepository;
import com.benzaied.gestiondestock.dto.ClientDto;
import com.benzaied.gestiondestock.dto.UtilisateurDto;
import com.benzaied.gestiondestock.model.Client;
import com.benzaied.gestiondestock.model.Utilisateur;
import com.benzaied.gestiondestock.model.auth.ExtendedUser;
import com.benzaied.gestiondestock.services.auth.ApplicationUserDetailsForClientService;
import com.benzaied.gestiondestock.services.auth.ApplicationUserDetailsService;
import com.benzaied.gestiondestock.token.Token;
import com.benzaied.gestiondestock.token.TokenRepository;
import com.benzaied.gestiondestock.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UtilisateurRepository utilisateurRepository;

    private final ClientRepository clientRepository;

    private final ApplicationUserDetailsService applicationUserDetailsService;

    //private final ApplicationUserDetailsForClientService applicationUserDetailsForClientService;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    private final TokenRepository tokenRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        var userdto = UtilisateurDto.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .motDePasse(passwordEncoder.encode(request.getMotDePasse()))
                .numTelephone(request.getNumTelephone())
                .adresse(request.getAdresse())
                .image(request.getImage())
                .dateNaissance(request.getDateNaissance())
                .entreprise(request.getEntreprise())
                .roles(request.getRoles())
                .build();
        var user = UtilisateurDto.toEntity(userdto);
        var savedUser = utilisateurRepository.save(user);

        var userDetails = applicationUserDetailsService.loadUserByUsername(savedUser.getEmail());
        var jwtToken = jwtUtil.generateToken((ExtendedUser) userDetails);
        var refreshToken = jwtUtil.generateRefreshToken((ExtendedUser) userDetails);
        saveUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getMotDePasse()
                )
        );
        var userDetails = applicationUserDetailsService.loadUserByUsername(request.getEmail());
        var jwtToken = jwtUtil.generateToken((ExtendedUser) userDetails);
        var refreshToken = jwtUtil.generateRefreshToken((ExtendedUser) userDetails);
        var user = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow();
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(Utilisateur user, String jwtToken) {
        var token = Token.builder()
                .utilisateur(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Utilisateur utilisateur) {
        var validUserTokens = tokenRepository.findAllValidTokenByUtilisateur(utilisateur.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtUtil.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.utilisateurRepository.findByEmail(userEmail)
                    .orElseThrow();
            var userDetails = this.applicationUserDetailsService.loadUserByUsername(userEmail);
            if (jwtUtil.validateToken(refreshToken, userDetails)) {
                var accessToken = jwtUtil.generateToken((ExtendedUser) userDetails);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public AuthenticationResponse registerClient(RegisterClientRequest request) {
        var clientDto = ClientDto.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .motDePasse(passwordEncoder.encode(request.getMotDePasse()))
                .numTelephone(request.getNumTelephone())
                .adresse(request.getAdresse())
                .image(request.getImage())
                .typeClient(request.getTypeClient())
                .idEntreprise(request.getIdEntreprise())
                .build();
        var client = ClientDto.toEntity(clientDto);
        var savedClient = clientRepository.save(client);

        var userDetails = applicationUserDetailsService.loadUserByUsername(savedClient.getEmail());
        var jwtToken = jwtUtil.generateToken((ExtendedUser) userDetails);
        var refreshToken = jwtUtil.generateRefreshToken((ExtendedUser) userDetails);
        saveClientToken(savedClient, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }


    public AuthenticationResponse authenticateClient(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getMotDePasse()
                )
        );
        var userDetails = applicationUserDetailsService.loadUserByUsername(request.getEmail());
        var jwtToken = jwtUtil.generateToken((ExtendedUser) userDetails);
        var refreshToken = jwtUtil.generateRefreshToken((ExtendedUser) userDetails);
        var client = clientRepository.findByEmail(request.getEmail())
                .orElseThrow();
        revokeAllClientTokens(client);
        saveClientToken(client, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void refreshTokenForClient(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtUtil.extractUsername(refreshToken);
        if (userEmail != null) {
            var client = this.clientRepository.findByEmail(userEmail)
                    .orElseThrow();
            var userDetails = this.applicationUserDetailsService.loadUserByUsername(userEmail);
            if (jwtUtil.validateToken(refreshToken, userDetails)) {
                var accessToken = jwtUtil.generateToken((ExtendedUser) userDetails);
                revokeAllClientTokens(client);
                saveClientToken(client, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private void saveClientToken(Client client, String jwtToken) {
        var token = Token.builder()
                .client(client)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllClientTokens(Client client) {
        var validClientTokens = tokenRepository.findAllValidTokenByClient(client.getId());
        if (validClientTokens.isEmpty())
            return;
        validClientTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validClientTokens);
    }

}
