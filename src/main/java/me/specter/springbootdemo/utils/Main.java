package me.specter.springbootdemo.utils;

import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Main {


    public static void main(String [] args){
        System.out.println(UUID.randomUUID());
        System.out.println(UUID.randomUUID());
        System.out.println(new BCryptPasswordEncoder().encode("dummypassword"));
    }
}
