package by.bsuir.tattooparlor.dao.repository;

import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.Order;
import by.bsuir.tattooparlor.entity.TattooMaster;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByClient(Client client);
    List<Order> findAllByMaster(TattooMaster master);
}
