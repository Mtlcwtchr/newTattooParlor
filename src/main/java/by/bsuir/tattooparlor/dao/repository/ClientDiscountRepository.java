package by.bsuir.tattooparlor.dao.repository;

import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.ClientDiscount;
import by.bsuir.tattooparlor.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientDiscountRepository extends JpaRepository<ClientDiscount, Long> {
    List<ClientDiscount> findAllByOwner(Client owner);
    Optional<ClientDiscount> findByDiscountAndOwner(Discount discountPromo, Client owner);
}