package me.specter.springbootdemo.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.specter.springbootdemo.token.TokenRepository;

@Component
public class LogoutHandlerComponent implements LogoutHandler{

    private final TokenRepository tokenRepository;

    public LogoutHandlerComponent(TokenRepository tokenRepository){
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void logout(
        HttpServletRequest request, 
        HttpServletResponse response, 
        Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }
        jwtToken = authHeader.substring(7);
        
        tokenRepository.findByToken(jwtToken)
            .stream()
            .forEach( 
                t -> {
                    t.setIsExpired(true);
                    t.setIsRevoked(true);
                    tokenRepository.save(t);
                } 
            );
    }
}
