package com.benzaied.gestiondestock.validator;

import com.benzaied.gestiondestock.dto.ArticleDto;
import com.benzaied.gestiondestock.dto.CategorieDto;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ArticleValidator {

    public static List<String> validate(ArticleDto articleDto){
        List<String> errors = new ArrayList<>();

        if(articleDto == null){
            errors.add("Verifier le nom d'article, SVP");
            errors.add("Verifier le code d'article, SVP");
            errors.add("Verifier le description d'article, SVP");
            errors.add("Verifier le prix UHT d'article, SVP");
            errors.add("Verifier le prix UTTC d'article, SVP");
            errors.add("Verifier le taux du TVA d'article, SVP");
            errors.add("Verifier la categorie d'article, SVP");
        }else{
            if(!StringUtils.hasLength(articleDto.getNomArticle())){
                errors.add("Verifier le nom d'article, SVP");
            }
            if(!StringUtils.hasLength(articleDto.getCodeArticle())){
                errors.add("Verifier le code d'article, SVP");
            }
            if(!StringUtils.hasLength(articleDto.getDescription())){
                errors.add("Verifier la description d'article, SVP");
            }
            if(articleDto.getPrixUnitaireHT()==null){
                errors.add("Verifier le prix UHT d'article, SVP");
            }
            if(articleDto.getPrixUnitaireTTC()==null){
                errors.add("Verifier le prix UTTC d'article, SVP");
            }
            if(articleDto.getTauxTVA()==null){
                errors.add("Verifier le taux du TVA d'article, SVP");
            }
            if(articleDto.getCategorie().getId()==null){
                errors.add("Verifier la categorie d'article, SVP");
            }
        }

        return errors;
    }

}
