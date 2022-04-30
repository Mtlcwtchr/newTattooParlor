package by.bsuir.tattooparlor.dao.repository;

import by.bsuir.tattooparlor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository<T extends User> extends JpaRepository<T, Long> {
}
