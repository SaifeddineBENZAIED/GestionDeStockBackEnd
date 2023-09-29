package com.benzaied.gestiondestock.validator;

import com.benzaied.gestiondestock.dto.VenteDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class VenteValidator {

    public static List<String> validate(VenteDto venteDto) {
        List<String> errors = new ArrayList<>();

        if (venteDto == null) {
            errors.add("Verifier le code de vente, SVP");
            errors.add("Verifier la date de vente, SVP");
        } else {
            if (!StringUtils.hasLength(venteDto.getCodeVente())) {
                errors.add("Verifier le code de vente, SVP");
            }
            if (venteDto.getDateVente() == null) {
                errors.add("Verifier la date de vente, SVP");
            }
        }

        return errors;
    }
}
