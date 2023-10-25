package me.specter.springbootdemo.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import me.specter.springbootdemo.user.AppUserRepository;

@Service
public class AppUserDetailsService implements UserDetailsService{

    private final AppUserRepository appUserRepository;

    AppUserDetailsService(AppUserRepository appUserRepository){
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Indeed the username is userEmail
        return appUserRepository
            .findByEmail(username)
            .orElseThrow(
                () -> new UsernameNotFoundException(
                    "User with email=%s is not found".formatted(username)
                )
            );

    }
    
}
