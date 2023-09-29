package com.benzaied.gestiondestock.services.strategy;

import com.benzaied.gestiondestock.dto.ArticleDto;
import com.benzaied.gestiondestock.dto.EntrepriseDto;
import com.benzaied.gestiondestock.exception.ErrorCodes;
import com.benzaied.gestiondestock.exception.InvalidOperationException;
import com.benzaied.gestiondestock.model.Entreprise;
import com.benzaied.gestiondestock.services.ClientService;
import com.benzaied.gestiondestock.services.EntrepriseService;
import com.benzaied.gestiondestock.services.FlickrService;
import com.flickr4java.flickr.FlickrException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;

@Service("entrepriseStrategy")
@Slf4j
public class SaveEntrepriseImage implements Strategy<EntrepriseDto>{

    private FlickrService flickrService;
    private EntrepriseService entrepriseService;

    @Autowired
    public SaveEntrepriseImage(FlickrService flickrService, EntrepriseService entrepriseService){
        this.flickrService=flickrService;
        this.entrepriseService=entrepriseService;
    }

    @Override
    public EntrepriseDto saveImage(Integer id, InputStream image, String titre) throws FlickrException {
        EntrepriseDto entrepriseDto = entrepriseService.findById(id);
        String urlImage = flickrService.saveImage(image,titre);
        if (!StringUtils.hasLength(urlImage)){
            throw new InvalidOperationException("Erreur lors de l'enregistrement d'image d'entreprise", ErrorCodes.SAVE_IMAGE_EXCEPTION);
        }
        entrepriseDto.setImage(urlImage);

        return entrepriseService.save(entrepriseDto);
    }
}
