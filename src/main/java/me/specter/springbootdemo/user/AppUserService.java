package me.specter.springbootdemo.user;

import org.springframework.stereotype.Service;

import me.specter.springbootdemo.error.UserNotFoundException;

import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository){
        this.appUserRepository = appUserRepository;
    }

    public List<AppUserDto> findAllUsers(){
        return appUserRepository.findAll()
            .stream()
            .map(u -> new AppUserDto(u))
            .collect(Collectors.toList());
    }

    public AppUserDto findUserById(Integer id){
        Optional<AppUser> user =  appUserRepository.findById(id);
        if(user.isPresent()){
            return user.map(u -> new AppUserDto(u)).get();
        }else{
            throw new UserNotFoundException("User %d is not found".formatted(id));
        }
    }

    public AppUserDto findUserByEmail(String email){
        Optional<AppUser> user =  appUserRepository.findByEmail(email);
        if(user.isPresent()){
            return user.map(u -> new AppUserDto(u)).get();
        }else{
            throw new UserNotFoundException("User with email(%s) is not found".formatted(email));
        }
    }
    
}
