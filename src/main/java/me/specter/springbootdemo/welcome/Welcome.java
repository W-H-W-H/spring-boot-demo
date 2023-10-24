package me.specter.springbootdemo.welcome;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// For testing purpose at the very beginging
@RestController
@RequestMapping("api/v1/welcome")
public class Welcome {
    @GetMapping
    public String welcome(){
        return "Welcome";
    }

    @GetMapping("/with-authen")
    public String welcomeWithAuthen(){
        return "Welcome With Authen";
    }
}
