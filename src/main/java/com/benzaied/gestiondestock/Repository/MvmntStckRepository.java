package com.benzaied.gestiondestock.Repository;

import com.benzaied.gestiondestock.model.MvmntStck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface MvmntStckRepository extends JpaRepository<MvmntStck, Integer> {

    @Query(value = "select sum(m.quantite) from mvmnt_stck m where m.idarticle = :idArticle", nativeQuery = true)
    BigDecimal stockReelArticle(@Param("idArticle") Integer idArticle);

    List<MvmntStck> findAllByArticleId(Integer id);

}
