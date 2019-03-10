package ru.lightsoff.authentication.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.lightsoff.authentication.DTO.LoginRequest;
import ru.lightsoff.authentication.DTO.RegisterRequest;
import ru.lightsoff.authentication.services.AuthService;
import ru.lightsoff.authentication.wrappers.TokenRetrievementVerdict;
import ru.lightsoff.authentication.wrappers.TokenRetrievementWrapper;

@RestController
public class AuthController {
    @Autowired
    AuthService authService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAuthException(Exception e) {
        LOGGER.debug("Exception occured" + e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/login")
    ResponseEntity<String> login(@RequestBody LoginRequest req){
            TokenRetrievementWrapper resWrapper = authService.login(req);
            return handleResponse(resWrapper);
    }

    @PostMapping("/register")
    ResponseEntity<String> register(@RequestBody RegisterRequest req){
        TokenRetrievementWrapper resWrapper = authService.register(req);
        return handleResponse(resWrapper);
    }

    private ResponseEntity<String> setTokenCookie(String token){
        LOGGER.debug("Setting token cookie");
        HttpHeaders responseHeaders = new HttpHeaders();
        String cookie = String.format("token=%s; HttpOnly", token);
        responseHeaders.set("Set-Cookie", cookie);
        return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
    }

    private ResponseEntity<String> handleResponse(TokenRetrievementWrapper resWrapper){
        if (resWrapper.getVerdict() == TokenRetrievementVerdict.DECLINED){
            return new ResponseEntity<>(resWrapper.getPayload(), HttpStatus.BAD_REQUEST);
        } else if (resWrapper.getVerdict() == TokenRetrievementVerdict.UNEXPECTED_ERROR){
            return new ResponseEntity<>(resWrapper.getPayload(), HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return setTokenCookie(resWrapper.getPayload());
        }
    }
}

