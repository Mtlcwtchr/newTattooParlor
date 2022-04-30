package by.bsuir.tattooparlor.dao.repository;

import by.bsuir.tattooparlor.entity.User;

import java.util.Optional;

public interface UserRepository extends IUserRepository<User> {
    Optional<User> findByLogin(String login);
}
