package com.benzaied.gestiondestock.services;

import com.benzaied.gestiondestock.dto.MvmntStckDto;

import java.math.BigDecimal;
import java.util.List;

public interface MvmntStckService {

    MvmntStckDto save(MvmntStckDto mvmntStckDto);
    BigDecimal stockReelArticle(Integer idArticle);
    List<MvmntStckDto> mvmntStckArticle(Integer idArticle);
    MvmntStckDto entreeStock(MvmntStckDto mvmntStckDto);
    MvmntStckDto sortieStock(MvmntStckDto mvmntStckDto);
    MvmntStckDto correctionStockPos(MvmntStckDto mvmntStckDto);
    MvmntStckDto correctionStockNeg(MvmntStckDto mvmntStckDto);
    //boolean delete(Integer id);

    /*MvmntStckDto findById(Integer id);
    List<MvmntStckDto> findAll();*/
}

