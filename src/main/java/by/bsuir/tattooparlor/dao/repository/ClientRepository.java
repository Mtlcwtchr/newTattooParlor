package by.bsuir.tattooparlor.dao.repository;

import by.bsuir.tattooparlor.entity.Client;

import java.util.Optional;

public interface ClientRepository extends IUserRepository<Client> {
    Optional<Client> findByPhone(String phone);
}
