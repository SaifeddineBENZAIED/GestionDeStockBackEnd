package com.benzaied.gestiondestock.services.strategy;

import com.benzaied.gestiondestock.dto.ArticleDto;
import com.benzaied.gestiondestock.dto.UtilisateurDto;
import com.benzaied.gestiondestock.exception.ErrorCodes;
import com.benzaied.gestiondestock.exception.InvalidOperationException;
import com.benzaied.gestiondestock.services.FlickrService;
import com.benzaied.gestiondestock.services.UtilisateurService;
import com.flickr4java.flickr.FlickrException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;

@Service("utilisateurStrategy")
@Slf4j
public class SaveUtilisateurImage implements Strategy<UtilisateurDto>{

    private FlickrService flickrService;
    private UtilisateurService utilisateurService;

    @Autowired
    public SaveUtilisateurImage(FlickrService flickrService, UtilisateurService utilisateurService){
        this.flickrService=flickrService;
        this.utilisateurService=utilisateurService;
    }

    @Override
    public UtilisateurDto saveImage(Integer id, InputStream image, String titre) throws FlickrException {
        UtilisateurDto utilisateurDto = utilisateurService.findById(id);
        String urlImage = flickrService.saveImage(image,titre);
        if (!StringUtils.hasLength(urlImage)){
            throw new InvalidOperationException("Erreur lors de l'enregistrement d'image d'utilisateur", ErrorCodes.SAVE_IMAGE_EXCEPTION);
        }
        utilisateurDto.setImage(urlImage);

        return utilisateurService.save(utilisateurDto);
    }
}
