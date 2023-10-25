package me.specter.springbootdemo.role;

import java.util.function.Function;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import jakarta.persistence.Id;

@Entity
public class AppRole {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    
    public enum RoleName{
        USER,
        ADMIN,
        MANAGER
    }

    public AppRole(String id, RoleName roleName){
        this.id = id;
        this.roleName = roleName;
    }

    public AppRole(){}

    public String getId(){
        return this.id;
    }

    public RoleName getRoleName(){
        return this.roleName;
    }

    public static Function<String, RoleName> mapper = 
        (roleNameInString) -> {

            if(roleNameInString.equalsIgnoreCase("ADMIN")){
                return RoleName.ADMIN;
            }else if(roleNameInString.equalsIgnoreCase("MANAGER")){
                return RoleName.MANAGER;
            }else{
                return RoleName.USER;
            }
        };
}
