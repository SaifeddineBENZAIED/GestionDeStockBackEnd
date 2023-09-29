package com.benzaied.gestiondestock.validator;

import com.benzaied.gestiondestock.dto.UtilisateurDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UtilisateurValidator {

    /*public static List<String> validate(UtilisateurDto utilisateurDto){
        List<String> errors = new ArrayList<>();

        if(!StringUtils.hasLength(utilisateurDto.getNom()) || utilisateurDto == null){
            errors.add("Verifier SVP le nom d'utilisateur");
        }
        if(!StringUtils.hasLength(utilisateurDto.getPrenom()) || utilisateurDto == null){
            errors.add("Verifier SVP le prenom d'utilisateur");
        }
        if(!StringUtils.hasLength(utilisateurDto.getMotDePasse()) || utilisateurDto == null){
            errors.add("Verifier SVP le mot de passe d'utilisateur");
        }
        if(!StringUtils.hasLength(utilisateurDto.getEmail()) || utilisateurDto == null){
            errors.add("Verifier SVP l'e-mail d'utilisateur");
        }
        if(!StringUtils.hasLength(utilisateurDto.getNumTelephone()) || utilisateurDto == null){
            errors.add("Verifier SVP le numero du telephone d'utilisateur");
        }
        /*if(utilisateurDto.getAdresse() == null || utilisateurDto == null){
            errors.add("Verifier SVP l'adresse d'utilisateur");
        }else{
            if(!StringUtils.hasLength(utilisateurDto.getAdresse().getAdresse1())){
                errors.add("Verifier SVP la premiere adresse d'utilisateur");
            }
            if(!StringUtils.hasLength(utilisateurDto.getAdresse().getCodePostale())){
                errors.add("Verifier SVP le code postale");
            }
            if(!StringUtils.hasLength(utilisateurDto.getAdresse().getVille())){
                errors.add("Verifier SVP la ville d'utilisateur");
            }
            if(!StringUtils.hasLength(utilisateurDto.getAdresse().getPays())){
                errors.add("Verifier SVP le pays d'utilisateur");
            }
        }*//*
        if(utilisateurDto.getDateNaissance() == null || utilisateurDto == null){
            errors.add("Verifier SVP la date de naissance d'utilisateur");
        }
        errors.addAll(AdresseValidator.validate(utilisateurDto.getAdresse()));


        return errors;


    }*/

    public static List<String> validate(UtilisateurDto utilisateurDto) {
        List<String> errors = new ArrayList<>();

        if (utilisateurDto == null) {
            errors.add("Veuillez renseigner le nom d'utilisateur");
            errors.add("Veuillez renseigner le prenom d'utilisateur");
            errors.add("Veuillez renseigner le mot de passe d'utilisateur");
            errors.add("Veuillez renseigner l'email d'utilisateur");
            errors.add("Verifier SVP le numero du telephone d'utilisateur");
            errors.add("Verifier SVP la date de naissance d'utilisateur");
            errors.addAll(AdresseValidator.validate(null));
            return errors;
        }

        if (!StringUtils.hasLength(utilisateurDto.getNom())) {
            errors.add("Veuillez renseigner le nom d'utilisateur");
        }
        if (!StringUtils.hasLength(utilisateurDto.getPrenom())) {
            errors.add("Veuillez renseigner le prenom d'utilisateur");
        }
        if (!StringUtils.hasLength(utilisateurDto.getEmail())) {
            errors.add("Veuillez renseigner l'email d'utilisateur");
        }
        if (!StringUtils.hasLength(utilisateurDto.getMotDePasse())) {
            errors.add("Veuillez renseigner le mot de passe d'utilisateur");
        }
        if (utilisateurDto.getDateNaissance() == null) {
            errors.add("Veuillez renseigner la date de naissance d'utilisateur");
        }
        if(!StringUtils.hasLength(utilisateurDto.getNumTelephone())){
            errors.add("Verifier SVP le numero du telephone d'utilisateur");
        }
        errors.addAll(AdresseValidator.validate(utilisateurDto.getAdresse()));

        return errors;
    }

}
