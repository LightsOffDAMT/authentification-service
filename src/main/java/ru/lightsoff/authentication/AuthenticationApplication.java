package ru.lightsoff.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.lightsoff.authentication.Entities.User;
import ru.lightsoff.authentication.configuration.UserRepository;

@SpringBootApplication
public class AuthenticationApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(AuthenticationApplication.class, args);

        UserRepository users = context.getBean(UserRepository.class);
        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
        User user = new User()
                .withEmail("123@ya.ru")
                .withUsername("123")
                .withPassword(encoder.encode("123"));
        users.save(user);

    }

}
