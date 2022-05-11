package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.dao.repository.OrderRepository;
import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderManager implements IOrderManager {

    private OrderRepository orderRepository;

    @Autowired
    public OrderManager(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> findAllByClient(Client client) {
        return orderRepository.findAllByClient(client);
    }
}
