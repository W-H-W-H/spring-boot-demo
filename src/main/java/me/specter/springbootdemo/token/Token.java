package me.specter.springbootdemo.token;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import me.specter.springbootdemo.user.AppUser;

@Entity
public class Token {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique=true)
    private String token;
    private boolean isRevoked;
    private boolean isExpired;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id")
    private AppUser user;

    public Token(Integer id, String token, boolean isRevoked, boolean isExpired, TokenType tokenType, AppUser user) {
        this.id = id;
        this.token = token;
        this.isRevoked = isRevoked;
        this.isExpired = isExpired;
        this.tokenType = tokenType;
        this.user = user;
    }

    public Token(){}
    
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isIsRevoked() {
        return this.isRevoked;
    }

    public boolean getIsRevoked() {
        return this.isRevoked;
    }

    public void setIsRevoked(boolean isRevoked) {
        this.isRevoked = isRevoked;
    }

    public boolean isIsExpired() {
        return this.isExpired;
    }

    public boolean getIsExpired() {
        return this.isExpired;
    }

    public void setIsExpired(boolean isExpired) {
        this.isExpired = isExpired;
    }

    public TokenType getTokenType() {
        return this.tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public AppUser getUser() {
        return this.user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

}
