package me.specter.springbootdemo.user;


public class AppUserDto {
    final private Integer id;
    final private String email;
    final private String displayName;
    final private boolean isEnabled;

    public AppUserDto(AppUser user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.displayName = user.getDisplayName();
        this.isEnabled = user.getIsEnabled();
    }

    public Integer getId() {
        return this.id;
    }


    public String getEmail() {
        return this.email;
    }

    public String getDisplayName() {
        return this.displayName;
    }


    public boolean isIsEnabled() {
        return this.isEnabled;
    }

    public boolean getIsEnabled() {
        return this.isEnabled;
    }

}
