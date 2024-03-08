package com.hexaware.lms.repository;

import com.hexaware.lms.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = """
      select t from Token t inner join Authentication a\s
      on t.user.id = a.id\s
      where a.id = :id and (t.expired = false or t.revoked = false)\s
      """)
    List<Token> findAllValidTokenByAuth(Long id);

    Optional<Token> findByToken(String token);
}