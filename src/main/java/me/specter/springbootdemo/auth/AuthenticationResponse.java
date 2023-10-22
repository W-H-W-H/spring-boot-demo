package me.specter.springbootdemo.auth;

public record AuthenticationResponse(String accessToken, String refreshToken) {
    
}
