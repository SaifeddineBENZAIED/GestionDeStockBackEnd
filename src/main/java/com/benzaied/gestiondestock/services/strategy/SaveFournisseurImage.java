package com.benzaied.gestiondestock.services.strategy;

import com.benzaied.gestiondestock.dto.ArticleDto;
import com.benzaied.gestiondestock.dto.FournisseurDto;
import com.benzaied.gestiondestock.exception.ErrorCodes;
import com.benzaied.gestiondestock.exception.InvalidOperationException;
import com.benzaied.gestiondestock.model.Fournisseur;
import com.benzaied.gestiondestock.services.ClientService;
import com.benzaied.gestiondestock.services.FlickrService;
import com.benzaied.gestiondestock.services.FournisseurService;
import com.flickr4java.flickr.FlickrException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;

@Service("fournisseurStrategy")
@Slf4j
public class SaveFournisseurImage implements Strategy<FournisseurDto>{

    private FlickrService flickrService;
    private FournisseurService fournisseurService;

    @Autowired
    public SaveFournisseurImage(FlickrService flickrService, FournisseurService fournisseurService){
        this.flickrService=flickrService;
        this.fournisseurService=fournisseurService;
    }

    @Override
    public FournisseurDto saveImage(Integer id, InputStream image, String titre) throws FlickrException {
        FournisseurDto fournisseurDto = fournisseurService.findById(id);
        String urlImage = flickrService.saveImage(image,titre);
        if (!StringUtils.hasLength(urlImage)){
            throw new InvalidOperationException("Erreur lors de l'enregistrement d'image de fournisseur", ErrorCodes.SAVE_IMAGE_EXCEPTION);
        }
        fournisseurDto.setImage(urlImage);

        return fournisseurService.save(fournisseurDto);
    }
}
