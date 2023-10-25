package me.specter.springbootdemo.user;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import me.specter.springbootdemo.bookmark.Bookmark;
import me.specter.springbootdemo.role.AppRole;
import me.specter.springbootdemo.token.Token;

@Entity
public class AppUser implements UserDetails{

    // @AllArgsConstructor
    public AppUser(Integer id, String email, String displayName, String password, boolean isEnabled, Set<AppRole> roles) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.password = password;
        this.isEnabled = isEnabled;
        this.roles = roles;
    }

    // @NoArgsConstructor
    public AppUser() {
    
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Bookmark> bookmarks;

    // If NOT EAGER, failed when do getAuthorities() otherwise
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "app_user_role",
        joinColumns = @JoinColumn(name = "app_user_id"),
        inverseJoinColumns = @JoinColumn(name = "app_role_id")
    )
    private Set<AppRole> roles;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user") // mappedBy <#Token Bean's Field name#>
    private List<Token> tokens;



    public static final Builder builder = new Builder();

    public static final class Builder{
        private Integer id;
        private String email;
        private String displayName;
        private String password;
        private boolean isEnabled;
        private Set<AppRole> roles;

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
    
        public Builder setRoles(Set<AppRole> roles) {
            this.roles = roles;
            return this;
        }
    
        public AppUser build(){
            return new AppUser(id, email, displayName, password, isEnabled, roles);
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

    public Set<AppRole> getRoles(){
        return this.roles;
    }

    public List<Bookmark> getBookmarks() {
        return this.bookmarks;
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

    public void setRole(Set<AppRole> roles){
        this.roles = roles;
    }

    public void setBookmarks(List<Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
    }

    // Methods from UserDetails
    // We must add prefix "ROLE_"
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles
            .stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
            .toList()
        ;
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
