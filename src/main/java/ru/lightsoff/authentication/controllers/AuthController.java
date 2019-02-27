package ru.lightsoff.authentication.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.lightsoff.authentication.DTO.LoginRequest;
import ru.lightsoff.authentication.DTO.RegisterRequest;
import ru.lightsoff.authentication.services.AuthService;

import javax.naming.AuthenticationException;

@RestController
public class AuthController {
    @Autowired
    AuthService authService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAuthException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/login")
    ResponseEntity<String> login(@RequestBody LoginRequest req) throws AuthenticationException {
            String token = authService.login(req);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("token", token);
            return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
    }

    @PostMapping("/validateToken")
    void validateToken(@RequestBody String token) throws AuthenticationException {
        authService.validateToken(token);
    }

    @PostMapping("/register")
    ResponseEntity<String> register(@RequestBody RegisterRequest req) throws AuthenticationException {
        String token = authService.register(req);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("token", token);
        return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
    }
}
