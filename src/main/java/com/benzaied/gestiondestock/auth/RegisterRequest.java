package com.benzaied.gestiondestock.auth;

import com.benzaied.gestiondestock.dto.AdresseDto;
import com.benzaied.gestiondestock.dto.EntrepriseDto;
import com.benzaied.gestiondestock.model.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private AdresseDto adresse;
    private String image;
    private String numTelephone;
    private Instant dateNaissance;
    private List<Roles> roles;
    private EntrepriseDto entreprise;

}
