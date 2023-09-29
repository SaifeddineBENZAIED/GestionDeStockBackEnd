package com.benzaied.gestiondestock.validator;

import com.benzaied.gestiondestock.dto.LigneCommandeFournisseurDto;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class LigneCommandeFournisseurValidator {

    public static List<String> validate(LigneCommandeFournisseurDto ligneCommandeFournisseurDto) {
        List<String> errors = new ArrayList<>();

        if (ligneCommandeFournisseurDto == null) {
            errors.add("LigneCommandeFournisseurDto est vide.");
        } else {
            if (ligneCommandeFournisseurDto.getArticle() == null) {
                errors.add("Veuillez spécifier l'article de la ligne de commande fournisseur.");
            } else {
                if (ligneCommandeFournisseurDto.getArticle().getId() == null) {
                    errors.add("Veuillez spécifier l'ID de l'article de la ligne de commande fournisseur.");
                }
            }

            if (ligneCommandeFournisseurDto.getCommandeFournisseur() == null) {
                errors.add("Veuillez spécifier la commande fournisseur de la ligne de commande fournisseur.");
            } else {
                if (ligneCommandeFournisseurDto.getCommandeFournisseur().getId() == null) {
                    errors.add("Veuillez spécifier l'ID de la commande fournisseur de la ligne de commande fournisseur.");
                }
            }

            if (ligneCommandeFournisseurDto.getQuantite() == null || ligneCommandeFournisseurDto.getQuantite().compareTo(BigDecimal.ZERO) <= 0) {
                errors.add("La quantité de la ligne de commande fournisseur doit être supérieure à zéro.");
            }

            if (ligneCommandeFournisseurDto.getPrixUnitaire() == null || ligneCommandeFournisseurDto.getPrixUnitaire().compareTo(BigDecimal.ZERO) <= 0) {
                errors.add("Le prix unitaire de la ligne de commande fournisseur doit être supérieur à zéro.");
            }
        }

        return errors;
    }
}
