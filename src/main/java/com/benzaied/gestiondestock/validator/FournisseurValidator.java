package com.benzaied.gestiondestock.validator;

import com.benzaied.gestiondestock.dto.FournisseurDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class FournisseurValidator {

    public static List<String> validate(FournisseurDto fournisseurDto){
        List<String> errors = new ArrayList<>();

        if(fournisseurDto == null){
            errors.add("Verifier le nom de fournisseur, SVP");
            errors.add("Verifier le prenom de fournisseur, SVP");
            errors.add("Verifier l'adresse de fournisseur, SVP");
            errors.add("Verifier l'e-mail de fournisseur, SVP");
            errors.add("Verifier le numero de telephone, SVP");
        }else{
            if(!StringUtils.hasLength(fournisseurDto.getNom())){
                errors.add("Verifier le nom de fournisseur, SVP");
            }
            if(!StringUtils.hasLength(fournisseurDto.getPrenom())){
                errors.add("Verifier le prenom de fournisseur, SVP");
            }
            /*if(fournisseurDto.getAdresse()==null){
                errors.add("Verifier l'adresse de fournisseur', SVP");
            }else{
                if(!StringUtils.hasLength(fournisseurDto.getAdresse().getAdresse1())){
                    errors.add("Verifier SVP la premiere adresse de fournisseur");
                }
                if(!StringUtils.hasLength(fournisseurDto.getAdresse().getCodePostale())){
                    errors.add("Verifier SVP le code postale");
                }
                if(!StringUtils.hasLength(fournisseurDto.getAdresse().getVille())){
                    errors.add("Verifier SVP la ville de fournisseur");
                }
                if(!StringUtils.hasLength(fournisseurDto.getAdresse().getPays())){
                    errors.add("Verifier SVP le pays de fournisseur");
                }
            }*/
            if(!StringUtils.hasLength(fournisseurDto.getEmail())){
                errors.add("Verifier l'e-mail de fournisseur, SVP");
            }
            if(!StringUtils.hasLength(fournisseurDto.getNumTelephone())){
                errors.add("Verifier le numero de telephone de fournisseur, SVP");
            }
        }
        errors.addAll(AdresseValidator.validate(fournisseurDto.getAdresse()));

        return errors;
    }

}
