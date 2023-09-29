package com.benzaied.gestiondestock.services.impl;

import com.benzaied.gestiondestock.Repository.MvmntStckRepository;
import com.benzaied.gestiondestock.dto.MvmntStckDto;
import com.benzaied.gestiondestock.exception.EntityNotFoundException;
import com.benzaied.gestiondestock.exception.ErrorCodes;
import com.benzaied.gestiondestock.exception.InvalidEntityException;
import com.benzaied.gestiondestock.model.LigneCommandeClient;
import com.benzaied.gestiondestock.model.MvmntStck;
import com.benzaied.gestiondestock.model.TypedeMvmntStck;
import com.benzaied.gestiondestock.services.ArticleService;
import com.benzaied.gestiondestock.services.MvmntStckService;
import com.benzaied.gestiondestock.validator.MvmntStckValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MvmntStckServiceImpl implements MvmntStckService {

    private final MvmntStckRepository mvmntStckRepository;
    private ArticleService articleService;

    @Autowired
    public MvmntStckServiceImpl(MvmntStckRepository mvmntStckRepository, ArticleService articleService) {
        this.mvmntStckRepository = mvmntStckRepository;
        this.articleService = articleService;
    }

    @Override
    public MvmntStckDto save(MvmntStckDto mvmntStckDto) {
        MvmntStck mvmntStck = MvmntStckDto.toEntity(mvmntStckDto);
        MvmntStck savedMvmntStck = mvmntStckRepository.save(mvmntStck);
        return MvmntStckDto.fromEntity(savedMvmntStck);
    }

    @Override
    public MvmntStckDto correctionStockNeg(MvmntStckDto mvmntStckDto) {
        List<String> errors = MvmntStckValidator.validate(mvmntStckDto);
        if (!errors.isEmpty()){
            log.error("Le mouvement de stock n'est pas valide {}", mvmntStckDto);
            throw new InvalidEntityException("Le mouvement de stock n'est pas valide", ErrorCodes.MVMNT_STCK_NOT_VALID, errors);
        }
        mvmntStckDto.setQuantite(BigDecimal.valueOf(Math.abs(mvmntStckDto.getQuantite().doubleValue())));
        mvmntStckDto.setTypeMvmntStck(TypedeMvmntStck.CORRECTION_NEG);

        return MvmntStckDto.fromEntity(mvmntStckRepository.save(MvmntStckDto.toEntity(mvmntStckDto)));
    }

    @Override
    public MvmntStckDto correctionStockPos(MvmntStckDto mvmntStckDto) {
        List<String> errors = MvmntStckValidator.validate(mvmntStckDto);
        if (!errors.isEmpty()){
            log.error("Le mouvement de stock n'est pas valide {}", mvmntStckDto);
            throw new InvalidEntityException("Le mouvement de stock n'est pas valide", ErrorCodes.MVMNT_STCK_NOT_VALID, errors);
        }
        mvmntStckDto.setQuantite(BigDecimal.valueOf(Math.abs(mvmntStckDto.getQuantite().doubleValue())));
        mvmntStckDto.setTypeMvmntStck(TypedeMvmntStck.CORRECTION_POS);

        return MvmntStckDto.fromEntity(mvmntStckRepository.save(MvmntStckDto.toEntity(mvmntStckDto)));
    }

    @Override
    public MvmntStckDto sortieStock(MvmntStckDto mvmntStckDto) {
        List<String> errors = MvmntStckValidator.validate(mvmntStckDto);
        if (!errors.isEmpty()){
            log.error("Le mouvement de stock n'est pas valide {}", mvmntStckDto);
            throw new InvalidEntityException("Le mouvement de stock n'est pas valide", ErrorCodes.MVMNT_STCK_NOT_VALID, errors);
        }
        mvmntStckDto.setQuantite(BigDecimal.valueOf(Math.abs(mvmntStckDto.getQuantite().doubleValue()) * -1 ));
        mvmntStckDto.setTypeMvmntStck(TypedeMvmntStck.SORTIE);

        return MvmntStckDto.fromEntity(mvmntStckRepository.save(MvmntStckDto.toEntity(mvmntStckDto)));
    }

    @Override
    public MvmntStckDto entreeStock(MvmntStckDto mvmntStckDto) {
        List<String> errors = MvmntStckValidator.validate(mvmntStckDto);
        if (!errors.isEmpty()){
            log.error("Le mouvement de stock n'est pas valide {}", mvmntStckDto);
            throw new InvalidEntityException("Le mouvement de stock n'est pas valide", ErrorCodes.MVMNT_STCK_NOT_VALID, errors);
        }
        mvmntStckDto.setQuantite(BigDecimal.valueOf(Math.abs(mvmntStckDto.getQuantite().doubleValue())));
        mvmntStckDto.setTypeMvmntStck(TypedeMvmntStck.ENTREE);

        return MvmntStckDto.fromEntity(mvmntStckRepository.save(MvmntStckDto.toEntity(mvmntStckDto)));
    }

    @Override
    public List<MvmntStckDto> mvmntStckArticle(Integer idArticle) {
        return mvmntStckRepository.findAllByArticleId(idArticle).stream()
                .map(MvmntStckDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal stockReelArticle(Integer idArticle) {

        if (idArticle==null){
            log.warn("ID article is null");
            return BigDecimal.valueOf(-1);
        }
        articleService.findById(idArticle);

        if(mvmntStckRepository.stockReelArticle(idArticle) != null){
            return mvmntStckRepository.stockReelArticle(idArticle);
        }else {
            return BigDecimal.valueOf(0);
        }
    }

    /*@Override
    public boolean delete(Integer id) {
        if (id == null) {
            log.error("L'ID de Mvmnt de Stock est non valide");
            return false;
        }
        MvmntStck mvmntStck = mvmntStckRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("MvmntStck not found !!")
        );
        mvmntStckRepository.deleteById(id);
        return true;
    }*/

    /*@Override
    public MvmntStckDto findById(Integer id) {
        return mvmntStckRepository.findById(id)
                .map(MvmntStckDto::fromEntity)
                .orElse(null);
    }

    @Override
    public List<MvmntStckDto> findAll() {
        List<MvmntStck> mvmntsStck = mvmntStckRepository.findAll();
        return mvmntsStck.stream()
                .map(MvmntStckDto::fromEntity)
                .collect(Collectors.toList());
    }*/
}
