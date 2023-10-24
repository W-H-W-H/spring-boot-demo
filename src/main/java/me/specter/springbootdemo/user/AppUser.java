package me.specter.springbootdemo.user;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import me.specter.springbootdemo.token.Token;

@Entity
public class AppUser implements UserDetails{

    // @AllArgsConstructor
    public AppUser(Integer id, String email, String displayName, String password, boolean isEnabled, Role role) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.password = password;
        this.isEnabled = isEnabled;
        this.role = role;
    }

    // @NoArgsConstructor
    public AppUser() {
    
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Email(message = "Email format is not valid")
    @NotBlank(message = "Email should be provided")
    @Column(unique=true)
    private String email;

    @NotBlank(message = "Display name should be provided")
    private String displayName;

    @NotBlank(message = "Password should be provided")
    private String password;

    private boolean isEnabled;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user") // mappedBy <#Token Bean's Field name#>
    private List<Token> tokens;

    public static final Builder builder = new Builder();

    public static final class Builder{
        private Integer id;
        private String email;
        private String displayName;
        private String password;
        private boolean isEnabled;
        private Role role;

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }
    
        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }
    
        public Builder setDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }
    
        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }
    
        public Builder setIsEnabled(boolean isEnabled) {
            this.isEnabled = isEnabled;
            return this;
        }
    
        public Builder setRole(Role role) {
            this.role = role;
            return this;
        }
    
        public AppUser build(){
            return new AppUser(id, email, displayName, password, isEnabled, role);
        }
    }

    // @Getter
    public Integer getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public boolean getIsEnabled() {
        return this.isEnabled;
    }

    public Role getRole(){
        return this.getRole();
    }

    // @Setter
    public void setId(Integer id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public void setRole(Role role){
        this.role = role;
    }

    // Methods from UserDetails
    // We must add prefix "ROLE_"
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
            new SimpleGrantedAuthority("ROLE_" + role.name())
        );
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
       return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
    
}
