package me.specter.springbootdemo.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AppUserController {
    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService){
        this.appUserService = appUserService;
    }

    @GetMapping("/user")
    public List<AppUserDto> findAllUsers(){
        return appUserService.findAllUsers();
    }

    @GetMapping("/user/{id}")
    public AppUserDto findUserById(@PathVariable Integer id){
        return appUserService.findUserById(id);
    }

    @GetMapping("/user/search")
    public AppUserDto findUserByEmail(@RequestParam(value = "email", required = true, defaultValue = "") String email){
        return appUserService.findUserByEmail(email);
    }

    @PostMapping("/user")
    public ResponseEntity<Void> createUser(@Valid AppUser user){
        appUserService.createUser(user);
        return ResponseEntity.created(null).build();
    }



}
