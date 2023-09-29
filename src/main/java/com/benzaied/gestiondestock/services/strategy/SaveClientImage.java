package com.benzaied.gestiondestock.services.strategy;

import com.benzaied.gestiondestock.dto.ClientDto;
import com.benzaied.gestiondestock.exception.ErrorCodes;
import com.benzaied.gestiondestock.exception.InvalidOperationException;
import com.benzaied.gestiondestock.services.ClientService;
import com.benzaied.gestiondestock.services.FlickrService;
import com.flickr4java.flickr.FlickrException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;

@Service("clientStrategy")
@Slf4j
public class SaveClientImage implements Strategy<ClientDto>{

    private FlickrService flickrService;
    private ClientService clientService;

    @Autowired
    public SaveClientImage(FlickrService flickrService, ClientService clientService){
        this.flickrService=flickrService;
        this.clientService=clientService;
    }

    @Override
    public ClientDto saveImage(Integer id, InputStream image, String titre) throws FlickrException {
        ClientDto clientDto = clientService.findById(id);
        String urlImage = flickrService.saveImage(image,titre);
        if (!StringUtils.hasLength(urlImage)){
            throw new InvalidOperationException("Erreur lors de l'enregistrement d'image de client", ErrorCodes.SAVE_IMAGE_EXCEPTION);
        }
        clientDto.setImage(urlImage);

        return clientService.save(clientDto);
    }
}
