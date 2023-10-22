package me.specter.springbootdemo.user;


public class AppUserBuilder {
    private Integer id;
    private String email;
    private String displayName;
    private String password;
    private boolean isEnabled;
    private Role role;

    

    public AppUserBuilder setId(Integer id) {
        this.id = id;
        return this;
    }

    public AppUserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public AppUserBuilder setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public AppUserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public AppUserBuilder setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
        return this;
    }

    public AppUserBuilder setRole(Role role) {
        this.role = role;
        return this;
    }

    public AppUser build(){
        return new AppUser(id, email, displayName, password, isEnabled, role);
    }

    
}
