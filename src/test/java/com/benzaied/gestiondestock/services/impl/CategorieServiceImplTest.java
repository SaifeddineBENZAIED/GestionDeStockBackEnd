package com.benzaied.gestiondestock.services.impl;

import com.benzaied.gestiondestock.dto.CategorieDto;
import com.benzaied.gestiondestock.exception.EntityNotFoundException;
import com.benzaied.gestiondestock.exception.ErrorCodes;
import com.benzaied.gestiondestock.exception.InvalidEntityException;
import com.benzaied.gestiondestock.services.CategorieService;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategorieServiceImplTest {

    @Autowired
    private CategorieService categorieService;

    @Test
    public void shouldSaveCategorieWithSucces(){
        CategorieDto expectedCategorie = CategorieDto.builder()
                .codeCategorie("Categ test")
                .description("Description test")
                .idEntreprise(1)
                .build();

        CategorieDto savedCategorie = categorieService.save(expectedCategorie);

        assertNotNull(savedCategorie);
        assertNotNull(savedCategorie.getId());
        assertEquals(expectedCategorie.getCodeCategorie(),savedCategorie.getCodeCategorie());
        assertEquals(expectedCategorie.getDescription(),savedCategorie.getDescription());
        assertEquals(expectedCategorie.getIdEntreprise(),savedCategorie.getIdEntreprise());
    }

    @Test
    public void shouldUpdateCategorieWithSucces(){
        CategorieDto expectedCategorie = CategorieDto.builder()
                .codeCategorie("Categ test")
                .description("Description test")
                .idEntreprise(1)
                .build();

        CategorieDto savedCategorie = categorieService.save(expectedCategorie);

        CategorieDto categorieToUpdate = savedCategorie;
        categorieToUpdate.setCodeCategorie("Categ update");

        savedCategorie = categorieService.save(categorieToUpdate);

        assertNotNull(categorieToUpdate);
        assertNotNull(categorieToUpdate.getId());
        assertEquals(categorieToUpdate.getCodeCategorie(),savedCategorie.getCodeCategorie());
        assertEquals(categorieToUpdate.getDescription(),savedCategorie.getDescription());
        assertEquals(categorieToUpdate.getIdEntreprise(),savedCategorie.getIdEntreprise());
    }

    @Test
    public void shouldThrowInvalidEntityException(){
        CategorieDto expectedCategorie = CategorieDto.builder().build();

        InvalidEntityException expectedException = assertThrows(InvalidEntityException.class, () -> categorieService.save(expectedCategorie));

        assertEquals(ErrorCodes.CATEGORY_NOT_VALID, expectedException.getErrorCode());

        assertEquals(1,expectedException.getErrors().size());

        assertEquals("Verifier SVP le code de la categorie", expectedException.getErrors().get(0));

    }

    @Test
    public void shouldThrowEntityNotFoundException(){

        EntityNotFoundException expectedException = assertThrows(EntityNotFoundException.class, () -> categorieService.findById(0));

        assertEquals(ErrorCodes.CATEGORY_NOT_FOUND, expectedException.getErrorCode());

        assertEquals("Categorie with id 0 not found",expectedException.getMessage());

    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundException2(){

        categorieService.findById(0);

    }

}