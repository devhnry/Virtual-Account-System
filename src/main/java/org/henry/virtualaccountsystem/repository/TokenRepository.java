package org.henry.virtualaccountsystem.repository;

import org.henry.virtualaccountsystem.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("""
            select t from Token t inner join Customer u on t.users.userId = u.userId
            where u.userId = :userId and (t.expired = false or t.revoked = false)
        """
    )
    List<Token> findValidTokenByCustomer(Long userId);
    Optional<Token> findByToken(String token);
}
