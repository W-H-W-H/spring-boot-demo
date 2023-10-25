package me.specter.springbootdemo.auth;

public record AuthenticationRequest(String userEmail, String password) {
    
}
