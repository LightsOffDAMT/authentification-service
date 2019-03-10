package ru.lightsoff.authentication.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.lightsoff.authentication.DTO.GenerateTokenRequest;
import ru.lightsoff.authentication.DTO.LoginRequest;
import ru.lightsoff.authentication.DTO.RegisterRequest;
import ru.lightsoff.authentication.Entities.User;
import ru.lightsoff.authentication.configuration.UserRepository;
import ru.lightsoff.authentication.wrappers.TokenRetrievementVerdict;
import ru.lightsoff.authentication.wrappers.TokenRetrievementWrapper;

import static ru.lightsoff.authentication.messages.AuthenticationErrorMessages.*;

@Service
public class BaseAuthService implements AuthService {
    @Autowired
    UserRepository users;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Value("${securityServiceUrl}")
    String securityServiceUrl;
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseAuthService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    public TokenRetrievementWrapper login(LoginRequest req) {
        LOGGER.debug("Validating login request");
        if (req == null || req.getUsername() == null || req.getPassword() == null
                || !validateInput(req.getPassword()) || !validateInput(req.getUsername())) {
            LOGGER.debug(INVALID_DATA_MSG);
            return new TokenRetrievementWrapper(TokenRetrievementVerdict.DECLINED, INVALID_DATA_MSG);
        }

        User user = users.findByUsername(req.getUsername());
        if (user != null) {
            if (passwordEncoder.matches(req.getPassword(), user.getPassword())) {
                String token = generateToken(new GenerateTokenRequest(user));
                return new TokenRetrievementWrapper(TokenRetrievementVerdict.ACCEPTED, token);
            }
        }
        LOGGER.debug(WRONG_CREDENTIALS_MSG);
        return new TokenRetrievementWrapper(TokenRetrievementVerdict.DECLINED, WRONG_CREDENTIALS_MSG);
    }


    public TokenRetrievementWrapper register(RegisterRequest req) {
        LOGGER.debug("Validating register request");
        if (req.getEmail() == null || req.getUsername() == null || req.getPassword() == null
                || !validateInput(req.getPassword()) || !validateInput(req.getUsername()) || !validateInput(req.getEmail())){
            LOGGER.debug(INVALID_DATA_MSG);
            return new TokenRetrievementWrapper(TokenRetrievementVerdict.DECLINED, INVALID_DATA_MSG);
        }
        if (!(isEmailUnique(req.getEmail()) || !(isUsernameUnique(req.getUsername())))) {
            LOGGER.debug(USER_EXISTS_MSG);
            return new TokenRetrievementWrapper(TokenRetrievementVerdict.DECLINED, USER_EXISTS_MSG);
        }

        LOGGER.debug("Saving user to database");
        User user = new User()
                .withEmail(req.getEmail())
                .withUsername(req.getUsername())
                .withPassword(passwordEncoder.encode(req.getPassword()));
        users.save(user);
        User createdUser = users.findByUsername(user.getUsername());
        String token = generateToken(new GenerateTokenRequest(createdUser));
        return new TokenRetrievementWrapper(TokenRetrievementVerdict.ACCEPTED, token);
    }

    private String generateToken(GenerateTokenRequest req){
        LOGGER.debug("Sending generate token request");
        return restTemplate.postForObject(securityServiceUrl, req, String.class);
    }

    private boolean isUsernameUnique(String username) {
        LOGGER.debug("Checking username uniqueness");
        User existingUser = users.findByUsername(username);
        return existingUser == null;
    }

    private boolean isEmailUnique(String email) {
        LOGGER.debug("Checking email uniqueness");
        User existingUser = users.findByEmail(email);
        return existingUser == null;
    }

    private boolean validateInput(String input) {
        return input.chars()
                .allMatch(value -> Character.isAlphabetic(value) || Character.isDigit(value));
    }
}
