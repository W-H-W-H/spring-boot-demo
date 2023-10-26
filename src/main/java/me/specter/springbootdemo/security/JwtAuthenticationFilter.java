package me.specter.springbootdemo.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.specter.springbootdemo.token.TokenRepository;

@Component

public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final JwtService jwtService;
    
    

    // @RequireArgsConstructor
    public JwtAuthenticationFilter(
        JwtService jwtService, 
        TokenRepository tokenRepository
    ){
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, 
        @NonNull HttpServletResponse response, 
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        LOGGER.info("JWT Filtering Begin");
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final Authentication authentication;
        
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            LOGGER.info("The authorisation header does not contain JWT");
            filterChain.doFilter(request, response);
            return;
        }
       
        jwtToken = authHeader.substring(7);
        
        try{
            authentication = this.jwtService.validateAccessToken(jwtToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            LOGGER.info("Set SecurityContext where authentication=" + authentication);
        }catch(Exception e){
            LOGGER.info("Exception caught during validation of JWT " + e.getMessage());
        }
        filterChain.doFilter(request, response);        
    }
    
}
