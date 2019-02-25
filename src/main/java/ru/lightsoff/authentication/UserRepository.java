package ru.lightsoff.authentication;

import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.repository.CrudRepository;
import ru.lightsoff.authentication.Entities.User;

@EnableJdbcRepositories
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String login);
}
