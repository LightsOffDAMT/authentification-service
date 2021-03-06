package ru.lightsoff.authentication.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.lightsoff.authentication.DTO.LoginRequest;
import ru.lightsoff.authentication.DTO.RegisterRequest;
import ru.lightsoff.authentication.Entities.User;
import ru.lightsoff.authentication.configuration.UserRepository;

import javax.naming.AuthenticationException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    @Autowired
    UserRepository users;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Value("${secretKey}")
    String secretKey;

    public String login(LoginRequest req) throws AuthenticationException {
        User user = users.findByUsername(req.getUsername());
        if (user != null) {
            if (passwordEncoder.matches(req.getPassword(), user.getPassword() )) {
                return createToken(user);
            }
        }
        throw new AuthenticationException("Wrong username or password");
    }

    public void validateToken(String token) throws AuthenticationException {

        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new AuthenticationException("Token corrupted");
        }
        try {
            if (claims.getExpiration().after(new Date())) {
                return;
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        throw new AuthenticationException("Token expired");
    }

    public String register(RegisterRequest req) throws AuthenticationException {
        if (req.getEmail() != null && req.getUsername() != null && req.getPassword() != null) {
            User user = new User()
                    .withEmail(req.getEmail())
                    .withUsername(req.getUsername())
                    .withPassword(passwordEncoder.encode(req.getPassword()));
            users.save(user);
            User createdUser = users.findByUsername(user.getUsername());
            return createToken(createdUser);
        }
        throw new AuthenticationException("Invalid register data");
    }

    private String createToken(User user){
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("id", user.getId());
        tokenData.put("username", user.getUsername());
        tokenData.put("email", user.getEmail());

        JwtBuilder jwtBuilder = Jwts.builder();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        jwtBuilder.setClaims(tokenData);
        jwtBuilder.setExpiration(calendar.getTime());

        return jwtBuilder.signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }

}
