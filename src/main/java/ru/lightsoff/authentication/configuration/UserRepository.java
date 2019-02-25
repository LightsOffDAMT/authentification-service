package ru.lightsoff.authentication.configuration;

import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.lightsoff.authentication.Entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String login);
}
