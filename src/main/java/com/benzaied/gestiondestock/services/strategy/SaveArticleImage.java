package com.benzaied.gestiondestock.services.strategy;

import com.benzaied.gestiondestock.dto.ArticleDto;
import com.benzaied.gestiondestock.exception.ErrorCodes;
import com.benzaied.gestiondestock.exception.InvalidOperationException;
import com.benzaied.gestiondestock.model.Article;
import com.benzaied.gestiondestock.services.ArticleService;
import com.benzaied.gestiondestock.services.FlickrService;
import com.flickr4java.flickr.FlickrException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;

@Service("articleStrategy")
@Slf4j
public class SaveArticleImage implements Strategy<ArticleDto>{

    private FlickrService flickrService;
    private ArticleService articleService;

    @Autowired
    public SaveArticleImage(FlickrService flickrService, ArticleService articleService){
        this.flickrService=flickrService;
        this.articleService=articleService;
    }

    @Override
    public ArticleDto saveImage(Integer id, InputStream image, String titre) throws FlickrException {
        ArticleDto articleDto = articleService.findById(id);
        String urlImage = flickrService.saveImage(image,titre);
        if (!StringUtils.hasLength(urlImage)){
            throw new InvalidOperationException("Erreur lors de l'enregistrement d'image d'article", ErrorCodes.SAVE_IMAGE_EXCEPTION);
        }
        articleDto.setImage(urlImage);

        return articleService.save(articleDto);
    }
}
