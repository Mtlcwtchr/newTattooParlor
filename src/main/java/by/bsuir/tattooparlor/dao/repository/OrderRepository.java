package by.bsuir.tattooparlor.dao.repository;

import by.bsuir.tattooparlor.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
