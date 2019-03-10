package ru.lightsoff.authentication.DTO;

import ru.lightsoff.authentication.Entities.User;

public class GenerateTokenRequest {
    private String id;
    private String username;
    private String email;

    public GenerateTokenRequest(User user) {
        this.id = user.getId().toString();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

    public String getId() {
        return id;
    }

    public GenerateTokenRequest setId(String id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public GenerateTokenRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public GenerateTokenRequest setEmail(String email) {
        this.email = email;
        return this;
    }
}
