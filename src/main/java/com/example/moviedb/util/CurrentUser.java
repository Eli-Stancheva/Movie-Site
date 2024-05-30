package com.example.moviedb.util;

import com.example.moviedb.models.entity.UserRole;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component("currentUser")
@SessionScope
public class CurrentUser {
    private Long id;

    private String username;
    private String email;

    private String password;

    private UserRole role;

    private boolean isLogged;


    public CurrentUser() {}

    public Long getId() {
        return id;
    }

    public CurrentUser setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public CurrentUser setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CurrentUser setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public CurrentUser setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserRole getRole() {
        return role;
    }

    public CurrentUser setRole(UserRole role) {
        this.role = role;
        return this;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public CurrentUser setLogged(Boolean logged) {
        isLogged = logged;
        return this;
    }

    public boolean isAdmin() {
        return isLogged() && this.role.getName().name().equals("ADMIN");
    }

    public void logout() {
        this.id = null;
        this.username = null;
        this.email = null;
        this.role = null;
        this.isLogged = false;
    }
}
