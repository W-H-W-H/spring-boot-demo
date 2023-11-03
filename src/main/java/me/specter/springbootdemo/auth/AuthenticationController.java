package me.specter.springbootdemo.auth;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
        @RequestBody RegisterRequest request
    ) {
        this.authenticationService.register(request);
        return ResponseEntity.created(null).build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
        @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(this.authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshAccessToken(
        @NotNull @RequestHeader("Refresh-Token") String refreshToken
    ) throws Exception {
        Optional<AuthenticationResponse> response =  this.authenticationService.refreshAccessToken(refreshToken);
        if(response.isPresent()){
            return ResponseEntity.ok(response.get());
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }




}
