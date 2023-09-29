package com.benzaied.gestiondestock.token;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query(value = """
            select t from Token t inner join Utilisateur u
            on t.utilisateur.id = u.id
            where u.id = :id and (t.expired = false or t.revoked = false)
            """)
    List<Token> findAllValidTokenByUtilisateur(Integer id);

    @Query(value = """
            select t from Token t inner join Client c
            on t.client.id = c.id
            where c.id = :id and (t.expired = false or t.revoked = false)
            """)
    List<Token> findAllValidTokenByClient(Integer id);

    Optional<Token> findByToken(String token);
}