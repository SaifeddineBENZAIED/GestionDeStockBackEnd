package com.benzaied.gestiondestock.validator;

import com.benzaied.gestiondestock.dto.AdresseDto;
import com.benzaied.gestiondestock.dto.ClientDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ClientValidator {

    public static List<String> validate(ClientDto clientDto){
        List<String> errors = new ArrayList<>();

        if(clientDto == null){
            errors.add("Verifier le nom de client, SVP");
            errors.add("Verifier le prenom de client, SVP");
            errors.add("Verifier l'adresse de client, SVP");
            errors.add("Verifier l'e-mail de client, SVP");
            errors.add("Verifier le numero de telephone, SVP");
            errors.add("Verifier le mot de passe, SVP");
            errors.add("Verifier le type, SVP");
        }else{
            if(!StringUtils.hasLength(clientDto.getNom())){
                errors.add("Verifier le nom de client, SVP");
            }
            if(!StringUtils.hasLength(clientDto.getPrenom())){
                errors.add("Verifier le prenom de client, SVP");
            }
            /*if(clientDto.getAdresse()==null){
                errors.add("Verifier l'adresse de client', SVP");
            }else{
                if(!StringUtils.hasLength(clientDto.getAdresse().getAdresse1())){
                    errors.add("Verifier SVP la premiere adresse de client");
                }
                if(!StringUtils.hasLength(clientDto.getAdresse().getCodePostale())){
                    errors.add("Verifier SVP le code postale");
                }
                if(!StringUtils.hasLength(clientDto.getAdresse().getVille())){
                    errors.add("Verifier SVP la ville de client");
                }
                if(!StringUtils.hasLength(clientDto.getAdresse().getPays())){
                    errors.add("Verifier SVP le pays de client");
                }
            }*/
            if(!StringUtils.hasLength(clientDto.getEmail())){
                errors.add("Verifier l'e-mail de client, SVP");
            }
            if(!StringUtils.hasLength(clientDto.getNumTelephone())){
                errors.add("Verifier le numero de telephone de client, SVP");
            }
            if(!StringUtils.hasLength(clientDto.getMotDePasse())){
                errors.add("Verifier le mot de passe, SVP");
            }
            if(clientDto.getTypeClient() == null){
                errors.add("Verifier le type de client, SVP");
            }
        }
        errors.addAll(AdresseValidator.validate(clientDto.getAdresse()));

        return errors;
    }

}
