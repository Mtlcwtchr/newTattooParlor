package by.bsuir.tattooparlor.dao.repository;

import by.bsuir.tattooparlor.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByPhone(String phone);
}
