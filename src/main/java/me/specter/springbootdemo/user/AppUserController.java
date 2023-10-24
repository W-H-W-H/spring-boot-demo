package me.specter.springbootdemo.user;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/users")
public class AppUserController {
    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService){
        this.appUserService = appUserService;
    }

    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<AppUserDto> findAllUsers(){
        return appUserService.findAllUsers();
    }

    @GetMapping("/{id}")
    public AppUserDto findUserById(@PathVariable Integer id){
        return appUserService.findUserById(id);
    }

    
    @GetMapping("/user/search")
    @PreAuthorize("hasRole('ADMIN')")
    public AppUserDto findUserByEmail(@RequestParam(value = "email", required = true, defaultValue = "") String email){
        return appUserService.findUserByEmail(email);
    }

}
