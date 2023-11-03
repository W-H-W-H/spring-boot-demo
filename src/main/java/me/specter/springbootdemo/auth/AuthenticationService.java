package me.specter.springbootdemo.auth;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

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

    public void register(RegisterRequest request) {

        AppUser user = AppUser.builder
        .setId(null)
        .setEmail(request.userEmail())
        .setDisplayName(request.displayName())
        .setPassword(this.passwordEncoder.encode(request.password()))
        .setIsEnabled(true)
        .setRoles(Set.of(
            this.roleRepository
                .findByRoleName(RoleName.USER)
                .orElseThrow(() -> new DataNotFoundException("Role User is not found"))
        ))
        .build();

        this.appUserRepository.save(user);
        //AppUser savedUser = appUserRepository.save(user);
        // String jwtAccessToken = jwtService.generateAccessToken(savedUser);
        // String jwtRefreshToken = jwtService.generateRefreshToken(savedUser);
        // saveUserToken(savedUser, jwtAccessToken);
        //return new AuthenticationResponse(jwtAccessToken, jwtRefreshToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        
        this.authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.userEmail(), request.password())
        );

        AppUser user = this.appUserRepository
            .findByEmail(request.userEmail())
            .orElseThrow(
                () -> new UserNotFoundException("User with email=%s is not found".formatted(request.userEmail()))
            );     
        String jwtAccessToken = this.jwtService.generateAccessToken(user);
        String jwtRefreshToken = this.jwtService.generateRefreshToken(user);
        this.revokeAllUserTokens(user);
        this.saveUserToken(user, jwtAccessToken);
        return new AuthenticationResponse(jwtAccessToken, jwtRefreshToken);
    }

    private void revokeAllUserTokens(AppUser user){
        List<Token> validTokens = this.tokenRepository.findAllValidToken(user.getId());
        if (!validTokens.isEmpty()){
            validTokens.stream()
                .forEach( 
                    token -> {
                        token.setIsExpired(true); 
                        token.setIsRevoked(true);
                    }
                );
            this.tokenRepository.saveAll(validTokens);
        }
    }

    private void saveUserToken(AppUser user, String jwtToken) {
        LOGGER.info("Saved jwtToken=%s".formatted(jwtToken));
        Token tokenInDatabase = new Token(
            null, 
            jwtToken, 
            false, 
            false, 
            TokenType.BEARER, 
            user
        );

        this.tokenRepository.save(tokenInDatabase);
    }

    public Optional<AuthenticationResponse> refreshAccessToken(
        String refreshToken
    ) throws Exception {
        if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
            return Optional.empty();
        }
        refreshToken = refreshToken.substring(7);
        this.jwtService.validateRefreshToken(refreshToken);
        
        String userEmail = this.jwtService.extractUsername(refreshToken);
        AppUser user = this.appUserRepository
            .findByEmail(userEmail)
            .orElseThrow(() -> new UserNotFoundException(
                "User with email %s is not found".formatted(userEmail)
                )
            );
        String accessToken = this.jwtService.generateAccessToken(user);
        this.revokeAllUserTokens(user);
        this.saveUserToken(user, accessToken);
        
        return Optional.of(new AuthenticationResponse(accessToken, refreshToken));
    } 
      

}
