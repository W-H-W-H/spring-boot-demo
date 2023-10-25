package me.specter.springbootdemo.testing;

import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.specter.springbootdemo.role.AppRole;
import me.specter.springbootdemo.role.AppRoleRepository;
import me.specter.springbootdemo.token.Token;
import me.specter.springbootdemo.token.TokenRepository;
import me.specter.springbootdemo.user.AppUser;
import me.specter.springbootdemo.user.AppUserRepository;

@RestController
@RequestMapping("/api/v1/crud-test")
public class CrudTesting {
    private final AppRoleRepository appRoleRepository;
    private final AppUserRepository appUserRepository;
    private final TokenRepository tokenRepository;

    public CrudTesting(
        AppRoleRepository appRoleRepository, 
        AppUserRepository appUserRepository,
        TokenRepository tokenRepository
    ){
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;
        this.tokenRepository = tokenRepository;
    }

    @PostMapping("/roles")
    public void deleteOneRole(){
        List<AppUser> users = this.appUserRepository.findAll();
        AppRole targetRole = this.appRoleRepository.findById("R0002").get();

        users.forEach(
            user -> {
                Set<AppRole> roles = user.getRoles();
                if (roles.contains(targetRole)){
                    roles.remove(targetRole);
                    this.appUserRepository.save(user);
                }
            }
        );


        this.appRoleRepository.deleteById("R0002");
    }

    @PostMapping("/tokens/{token}")
    public void deleteToken(@PathVariable String token){
        Token tokenInDb = this.tokenRepository.findByToken(token).get();
        this.tokenRepository.delete(tokenInDb);
        
    }

    @PostMapping("/users/{id}")
    public void deleteUser(@PathVariable Integer id){
        this.appUserRepository.deleteById(id);
    }

}
