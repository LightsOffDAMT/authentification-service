package ru.lightsoff.authentication.services;

import ru.lightsoff.authentication.DTO.LoginRequest;
import ru.lightsoff.authentication.DTO.RegisterRequest;
import ru.lightsoff.authentication.wrappers.TokenRetrievementWrapper;

import javax.naming.AuthenticationException;

public interface AuthService {
    TokenRetrievementWrapper login(LoginRequest req);
    TokenRetrievementWrapper register(RegisterRequest req);
}
