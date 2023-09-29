package com.benzaied.gestiondestock.services.impl;

import com.benzaied.gestiondestock.Repository.ArticleRepository;
import com.benzaied.gestiondestock.Repository.CategorieRepository;
import com.benzaied.gestiondestock.dto.CategorieDto;
import com.benzaied.gestiondestock.exception.EntityNotFoundException;
import com.benzaied.gestiondestock.exception.ErrorCodes;
import com.benzaied.gestiondestock.exception.InvalidEntityException;
import com.benzaied.gestiondestock.exception.InvalidOperationException;
import com.benzaied.gestiondestock.model.Article;
import com.benzaied.gestiondestock.model.Categorie;
import com.benzaied.gestiondestock.model.LigneVente;
import com.benzaied.gestiondestock.services.CategorieService;
import com.benzaied.gestiondestock.validator.CategorieValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategorieServiceImpl implements CategorieService {

    private final CategorieRepository categorieRepository;
    private ArticleRepository articleRepository;

    @Autowired
    public CategorieServiceImpl(CategorieRepository categorieRepository, ArticleRepository articleRepository) {
        this.categorieRepository = categorieRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public CategorieDto save(CategorieDto categorieDto) {
        List<String> errors = CategorieValidator.validate(categorieDto);
        if(!errors.isEmpty()){
            log.error("La categorie n'est pas valide {}",categorieDto);
            throw new InvalidEntityException("La categorie n'est pas valide", ErrorCodes.CATEGORY_NOT_VALID, errors);
        }
        Categorie categorie = CategorieDto.toEntity(categorieDto);
        Categorie savedCategorie = categorieRepository.save(categorie);
        return CategorieDto.fromEntity(savedCategorie);
    }

    @Override
    public CategorieDto findById(Integer id) {
        if (id == null){
            log.error("L'ID de catégorie est non valide");
            return null;
        }
        Optional<Categorie> categorie = categorieRepository.findById(id);
        if (categorie.isEmpty()) {
            throw new EntityNotFoundException("Categorie with id " + id + " not found", ErrorCodes.CATEGORY_NOT_FOUND);
        }
        CategorieDto categorieDto = CategorieDto.fromEntity(categorie.get());
        return categorieDto;

    }

    @Override
    public CategorieDto findByCodeCategorie(String codeCategorie) {
        if (!StringUtils.hasLength(codeCategorie)) {
            log.error("Le code de catégorie est invalide");
            return null;
        }
        Categorie categorie = categorieRepository.findByCodeCategorie(codeCategorie)
                .orElseThrow(() -> new EntityNotFoundException("Categorie with code " + codeCategorie + " not found"));
        return CategorieDto.fromEntity(categorie);
    }

    @Override
    public CategorieDto findByNomCategorie(String nomCategorie) {
        if (!StringUtils.hasLength(nomCategorie)) {
            log.error("Le nom de catégorie est invalide");
            return null;
        }
        Categorie categorie = categorieRepository.findByNomCategorieIgnoreCase(nomCategorie)
                .orElseThrow(() -> new EntityNotFoundException("Categorie with name " + nomCategorie + " not found"));
        return CategorieDto.fromEntity(categorie);
    }

    @Override
    public List<CategorieDto> findAll() {
        List<Categorie> categories = categorieRepository.findAll();
        return categories.stream()
                .map(CategorieDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(Integer id) {
        if (!categorieRepository.existsById(id)) {
            log.error("Categorie with id " + id + " not found");
            return false;
        }
        Categorie categorie = categorieRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Category not found !!")
        );
        List<Article> articles = articleRepository.findAllByCategorieId(id);
        if (!articles.isEmpty()){
            throw new InvalidOperationException("Impossible de supprimer un categorie qui contient déja des articles",ErrorCodes.CATEGORY_ALREADY_IN_USE);
        }

        categorieRepository.deleteById(id);
        return true;
    }
}
