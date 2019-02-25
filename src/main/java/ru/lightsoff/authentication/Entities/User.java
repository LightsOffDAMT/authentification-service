package ru.lightsoff.authentication.Entities;

import org.springframework.data.annotation.Id;

/**
 * User entity
 */
public class User {
    @Id
    private Long id;
    private String username;
    private String password;
    private String email;

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public User withId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User withUsername(String username) {
        this.username = username;
        return this;
    }

    public User withPassword(String password) {
        this.password = password;
        return this;
    }

    public User withEmail(String email) {
        this.email = email;
        return this;
    }

}

