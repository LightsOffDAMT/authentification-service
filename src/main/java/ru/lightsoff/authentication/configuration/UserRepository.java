package ru.lightsoff.authentication.configuration;

import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.lightsoff.authentication.Entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Query("select * from users where username like :username;")
    User findByUsername(@Param("username") String username);
}
