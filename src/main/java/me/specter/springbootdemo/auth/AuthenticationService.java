package me.specter.springbootdemo.auth;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.specter.springbootdemo.error.DataNotFoundException;
import me.specter.springbootdemo.error.UserNotFoundException;
import me.specter.springbootdemo.role.AppRoleRepository;
import me.specter.springbootdemo.role.AppRole.RoleName;
import me.specter.springbootdemo.security.JwtService;
import me.specter.springbootdemo.token.Token;
import me.specter.springbootdemo.token.TokenRepository;
import me.specter.springbootdemo.token.TokenType;
import me.specter.springbootdemo.user.AppUser;
import me.specter.springbootdemo.user.AppUserRepository;

@Service
public class AuthenticationService {


    private final PasswordEncoder passwordEncoder;
    private final AppUserRepository appUserRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final AppRoleRepository roleRepository;

    public AuthenticationService(
        PasswordEncoder passwordEncoder, 
        AppUserRepository appUserRepository, 
        JwtService jwtService,
        AuthenticationManager authenticationManager,
        TokenRepository tokenRepository,
        AppRoleRepository roleRepository
    ){
        this.passwordEncoder = passwordEncoder;
        this.appUserRepository = appUserRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
        this.roleRepository = roleRepository;
    }

    public AuthenticationResponse register(RegisterRequest request) {

        AppUser user = AppUser.builder
        .setId(null)
        .setEmail(request.email())
        .setDisplayName(request.displayName())
        .setPassword(passwordEncoder.encode(request.password()))
        .setIsEnabled(true)
        .setRoles(Set.of(
            this.roleRepository
                .findByRoleName(RoleName.USER)
                .orElseThrow(() -> new DataNotFoundException("Role User is not found"))
        ))
        .build();

        AppUser savedUser = appUserRepository.save(user);
        String jwtAccessToken = jwtService.generateAccessToken(user);
        String jwtRefreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtAccessToken);
        return new AuthenticationResponse(jwtAccessToken, jwtRefreshToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        AppUser user = appUserRepository
            .findByEmail(request.email())
            .orElseThrow(
                () -> new UserNotFoundException("User with email=%s is not found".formatted(request.email()))
            );     
        String jwtAccessToken = jwtService.generateAccessToken(user);
        String jwtRefreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtAccessToken);

        return new AuthenticationResponse(jwtAccessToken, jwtRefreshToken);
    }

    private void revokeAllUserTokens(AppUser user){
        List<Token> validTokens = tokenRepository.findAllValidToken(user.getId());
        if (!validTokens.isEmpty()){
            validTokens.stream()
                .forEach( 
                    token -> {
                        token.setIsExpired(true); 
                        token.setIsRevoked(true);
                    }
                );
            tokenRepository.saveAll(validTokens);
        }
    }

    private void saveUserToken(AppUser user, String jwtToken) {
        Token tokenInDatabase = new Token(
            null, 
            jwtToken, 
            false, 
            false, 
            TokenType.BEARER, 
            user
        );

        tokenRepository.save(tokenInDatabase);
    }

    public void refreshToken(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws StreamWriteException, DatabindException, IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
        AppUser user = appUserRepository
            .findByEmail(userEmail)
            .orElseThrow(() -> new UserNotFoundException(
                "User with email %s is not found".formatted(userEmail)
                )
            );
        if (jwtService.validateToken(refreshToken, user)) {
            String accessToken = jwtService.generateAccessToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, accessToken);
            AuthenticationResponse authResponse = new AuthenticationResponse(accessToken, refreshToken);
            new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
        }
        }
    }    

}
