package com.benzaied.gestiondestock.validator;

import com.benzaied.gestiondestock.dto.LigneVenteDto;
import com.benzaied.gestiondestock.dto.MvmntStckDto;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MvmntStckValidator {

    public static List<String> validate(MvmntStckDto mvmntStckDto) {
        List<String> errors = new ArrayList<>();
        if (mvmntStckDto == null){
            errors.add("Verifier la date du mouvement");
            errors.add("Verifier la quantite du mouvement");
            errors.add("Verifier l' article du mouvement");
            errors.add("Verifier le type du mouvement");
            return errors;
        }
        if (mvmntStckDto.getDateMvmnt() == null){
            errors.add("Verifier la date du mouvement");
        }
        if (mvmntStckDto.getQuantite() == null || mvmntStckDto.getQuantite().compareTo(BigDecimal.ZERO)==0){
            errors.add("Verifier la quantite du mouvement");
        }
        if (mvmntStckDto.getArticle() == null || mvmntStckDto.getArticle().getId() == null){
            errors.add("Verifier l' article du mouvement");
        }
        if (!StringUtils.hasLength(mvmntStckDto.getTypeMvmntStck().name())){
            errors.add("Verifier le type du mouvement");
        }


        return null;

    }

}
