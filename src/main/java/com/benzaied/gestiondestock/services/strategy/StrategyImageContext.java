package com.benzaied.gestiondestock.services.strategy;

import com.benzaied.gestiondestock.exception.ErrorCodes;
import com.benzaied.gestiondestock.exception.InvalidOperationException;
import com.flickr4java.flickr.FlickrException;
import lombok.Setter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class StrategyImageContext {

    private BeanFactory beanFactory;

    private Strategy strategy;

    @Setter
    private String context;

    @Autowired
    public StrategyImageContext(BeanFactory beanFactory){
        this.beanFactory=beanFactory;
    }

    public Object saveImage(String context, Integer id, InputStream image, String titre) throws FlickrException {
        determineContext(context);
        return strategy.saveImage(id, image, titre);
    }

    private void determineContext(String context){
        final String beanName = context + "Strategy";
        switch (context){
            case "article":
                strategy = beanFactory.getBean(beanName,SaveArticleImage.class);
                break;
            case "client":
                strategy = beanFactory.getBean(beanName,SaveClientImage.class);
                break;
            case "utilisateur":
                strategy = beanFactory.getBean(beanName,SaveUtilisateurImage.class);
                break;
            case "fournisseur":
                strategy = beanFactory.getBean(beanName,SaveFournisseurImage.class);
                break;
            case "entreprise":
                strategy = beanFactory.getBean(beanName,SaveEntrepriseImage.class);
                break;
            default: throw new InvalidOperationException("Contexte inconnue pour l'enregistrement d'image", ErrorCodes.UNKNOWN_CONTEXT);
        }
    }

}
