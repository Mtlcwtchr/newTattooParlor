package by.bsuir.tattooparlor.dao.repository;

import by.bsuir.tattooparlor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
    Optional<User> findByLoginOrEmail(String login, String email);
}
