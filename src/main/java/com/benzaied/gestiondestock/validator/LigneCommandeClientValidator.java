package com.benzaied.gestiondestock.validator;

import com.benzaied.gestiondestock.dto.LigneCommandeClientDto;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class LigneCommandeClientValidator {

    public static List<String> validate(LigneCommandeClientDto ligneCommandeClientDto) {
        List<String> errors = new ArrayList<>();

        if (ligneCommandeClientDto == null) {
            errors.add("LigneCommandeClientDto est vide.");
        } else {
            if (ligneCommandeClientDto.getArticle() == null) {
                errors.add("Veuillez spécifier l'article de la ligne de commande.");
            } else {
                if (ligneCommandeClientDto.getArticle().getId() == null) {
                    errors.add("Veuillez spécifier l'ID de l'article de la ligne de commande.");
                }
            }

            if (ligneCommandeClientDto.getCommandeClient() == null) {
                errors.add("Veuillez spécifier la commande client de la ligne de commande.");
            } else {
                if (ligneCommandeClientDto.getCommandeClient().getId() == null) {
                    errors.add("Veuillez spécifier l'ID de la commande client de la ligne de commande.");
                }
            }

            if (ligneCommandeClientDto.getQuantite() == null || ligneCommandeClientDto.getQuantite().compareTo(BigDecimal.ZERO) <= 0) {
                errors.add("La quantité de la ligne de commande doit être supérieure à zéro.");
            }

            if (ligneCommandeClientDto.getPrixUnitaire() == null || ligneCommandeClientDto.getPrixUnitaire().compareTo(BigDecimal.ZERO) <= 0) {
                errors.add("Le prix unitaire de la ligne de commande doit être supérieur à zéro.");
            }
        }

        return errors;
    }
}
