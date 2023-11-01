package me.specter.springbootdemo.user;

import java.util.List;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/users")
public class AppUserController {
    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService){
        this.appUserService = appUserService;
    }

    
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AppUserDto> findAllUsers(){
        return appUserService.findAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @PostAuthorize("hasRole('ADMIN') or returnObject.email == authentication.name")
    public AppUserDto findUserById(@PathVariable Integer id){
        return appUserService.findUserById(id);
    }

    
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @PostAuthorize("hasRole('ADMIN') or returnObject.email == authentication.name")
    public AppUserDto findUserByEmail(){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return appUserService.findUserByEmail(userEmail);
    }

    @PutMapping("/enable/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void enableUser(@PathVariable Integer id){
        appUserService.changeIsEnabled(id, true);
    }

    @PutMapping("/disable/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void disableUser(@PathVariable Integer id){
        appUserService.changeIsEnabled(id, false);
    }

}
