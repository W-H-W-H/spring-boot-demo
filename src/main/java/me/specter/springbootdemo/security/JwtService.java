package me.specter.springbootdemo.security;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${application.security.jwt.access-token.expiration}")
    private long accessTokenExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    public String extractUsername(String jwtToken) {
        return extractClaims(jwtToken, Claims::getSubject);
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts
            .parserBuilder()
            .setSigningKey(getSigninKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public boolean validateToken(String token, UserDetails userDetails){
        System.out.println("Validating Token");
        final String username = extractUsername(token);
        final Date expiration = extractClaims(token, Claims::getExpiration);
        final Date issuedAt = extractClaims(token, Claims::getIssuedAt);
        final Date now = new Date();
        return username.equals(userDetails.getUsername()) 
            && now.before(expiration) 
            && now.after(issuedAt)
            ;
    }

    public String generateAccessToken(UserDetails userDetails){
        return generateAccessToken(Collections.emptyMap() , userDetails);
    }

    public String generateAccessToken(
        Map<String, Object> extraClaims,
        UserDetails userDetails
    ){
        return buildJwtToken(extraClaims, userDetails, accessTokenExpiration);
    }

    public String generateRefreshToken(
        UserDetails userDetails
    ){
        return buildJwtToken(Collections.emptyMap(), userDetails, refreshTokenExpiration);
    }

    private String buildJwtToken(
        Map<String, Object> extraClaims, 
        UserDetails userDetails,
        long expiration
    ) {
        return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expiration) )
        .signWith(getSigninKey(), SignatureAlgorithm.HS256)
        .compact();
    }

    

    private Key getSigninKey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }
}
