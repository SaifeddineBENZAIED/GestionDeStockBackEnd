package com.benzaied.gestiondestock.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    /*String extractUsername(String jwt);
    <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver);
    String extractIdEntreprise(String token);
    String generateToken(UserDetails userDetails);
    String generateToken(Map<String,Object> extraClaims, UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);
    String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration);
    boolean isTokenValid(String jwt , UserDetails userDetails);
    boolean isTokenExpired(String jwt);
    Date extractExpiration(String jwt);
    Claims extractAllClaims(String jwt);
    Key getSignInKey();*/
}
