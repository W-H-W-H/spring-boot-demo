package me.specter.springbootdemo.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.specter.springbootdemo.token.TokenRepository;

@Component

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AppUserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    // @RequireArgsConstructor
    public JwtAuthenticationFilter(
        JwtService jwtService, 
        AppUserDetailsService userDetailsService,
        TokenRepository tokenRepository
    ){
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, 
        @NonNull HttpServletResponse response, 
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {


        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        jwtToken = authHeader.substring(7);
        
        try{
            userEmail = jwtService.extractUsername(jwtToken);

            // User is not authenticated yet
            if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                boolean isTokenInDatabaseValid = tokenRepository
                    .findByToken(jwtToken)
                    .map(t -> !t.getIsExpired() && !t.getIsRevoked())
                    .orElse(false);
                if(isTokenInDatabaseValid && jwtService.validateToken(jwtToken, userDetails)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );

                    

                    // pass more information to authentication token
                    authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }catch(RuntimeException e){
            System.out.println("catch Exception: " + e.getMessage());
        }

        System.out.println("[SPTR]: Continue to filter");
        filterChain.doFilter(request, response);        
    }
    
}
