package by.bsuir.tattooparlor.dao.repository;

import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.ClientRate;
import by.bsuir.tattooparlor.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRateRepository extends JpaRepository<ClientRate, Long> {
    List<ClientRate> findAllByClient(Client client);
    List<ClientRate> findAllByProduct(Product product);
    Optional<ClientRate> findByClientAndProduct(Client client, Product product);
}
