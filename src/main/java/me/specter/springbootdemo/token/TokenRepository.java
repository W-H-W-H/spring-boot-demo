package me.specter.springbootdemo.token;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer>{
    
    @Query("""
        SELECT t 
        FROM Token t 
        INNER JOIN AppUser u 
        ON u.id = t.user.id 
        WHERE t.isExpired = false 
        AND t.isRevoked = false 
        AND u.id = :userId
    """)
    List<Token> findAllValidToken(Integer userId);

    Optional<Token> findByToken(String token);
}
