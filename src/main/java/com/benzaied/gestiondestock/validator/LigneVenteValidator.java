package com.benzaied.gestiondestock.validator;

import com.benzaied.gestiondestock.dto.LigneVenteDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class LigneVenteValidator {

    public static List<String> validate(LigneVenteDto ligneVenteDto) {
        List<String> errors = new ArrayList<>();

        if (ligneVenteDto == null) {
            errors.add("LigneVenteDto est vide.");
        } else {
            if (ligneVenteDto.getArticle() == null) {
                errors.add("Veuillez spécifier l'article de la ligne de vente.");
            } else {
                if (ligneVenteDto.getArticle().getId() == null) {
                    errors.add("Veuillez spécifier l'ID de l'article de la ligne de vente.");
                }
            }

            if (ligneVenteDto.getVente() == null) {
                errors.add("Veuillez spécifier la vente de la ligne de vente.");
            } else {
                if (ligneVenteDto.getVente().getId() == null) {
                    errors.add("Veuillez spécifier l'ID de la vente de la ligne de vente.");
                }
            }

            if (ligneVenteDto.getQuantite() == null || ligneVenteDto.getQuantite().compareTo(BigDecimal.ZERO) <= 0) {
                errors.add("La quantité de la ligne de vente doit être supérieure à zéro.");
            }

            if (ligneVenteDto.getPrixUnitaire() == null || ligneVenteDto.getPrixUnitaire().compareTo(BigDecimal.ZERO) <= 0) {
                errors.add("Le prix unitaire de la ligne de vente doit être supérieur à zéro.");
            }
        }

        return errors;
    }
}
