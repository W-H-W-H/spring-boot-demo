package me.specter.springbootdemo.security;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import me.specter.springbootdemo.token.TokenRepository;
import io.jsonwebtoken.Claims;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${application.security.jwt.access-token.expiration}")
    private long accessTokenExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    private final AppUserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public JwtService(
        AppUserDetailsService userDetailsService,
        TokenRepository tokenRepository
    ){
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
    }

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

    public Authentication validateAccessToken(String jwtToken) throws Exception{
        LOGGER.info("validate access token");
        return this.validateToken(jwtToken, false);
    }

    public Authentication validateRefreshToken(String jwtToken) throws Exception{
        LOGGER.info("validate refresh token");
        return this.validateToken(jwtToken, true);
    }

    public Authentication validateToken(String jwtToken, boolean isTokenNotInDB) throws Exception{

        final String userEmail = this.extractUsername(jwtToken);
        final Date expiration = extractClaims(jwtToken, Claims::getExpiration);
        final Date issuedAt = extractClaims(jwtToken, Claims::getIssuedAt);
        final Date now = new Date();

        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        boolean isTokenInDatabaseValid = isTokenNotInDB || tokenRepository
                .findByToken(jwtToken)
                .map(t -> !t.getIsExpired() && !t.getIsRevoked())
                .orElse(false)
        ;
        
        boolean isTokenReceivedValid = 
            userEmail.equals(userDetails.getUsername()) 
            && now.before(expiration) 
            && now.after(issuedAt)
        ;

        if(isTokenInDatabaseValid && isTokenReceivedValid){
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
            );
            
            return authToken;
        }

        return null;
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
