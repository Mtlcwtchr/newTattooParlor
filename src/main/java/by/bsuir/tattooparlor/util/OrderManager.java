package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.dao.repository.OrderRepository;
import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.Order;
import by.bsuir.tattooparlor.entity.TattooMaster;
import by.bsuir.tattooparlor.entity.helpers.OrderStatus;
import by.bsuir.tattooparlor.util.exception.NoOrderPresentedException;
import by.bsuir.tattooparlor.util.exception.UtilException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderManager implements IOrderManager {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderManager(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> findAllByClient(Client client) {
        return orderRepository.findAllByClient(client);
    }

    @Override
    public List<Order> findUnacceptedByMaster(TattooMaster tattooMaster) {
        return orderRepository.findAllByMaster(tattooMaster)
                .stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.REQUESTED)
                .toList();
    }

    @Override
    public List<Order> findAcceptedByMaster(TattooMaster tattooMaster) {
        return orderRepository.findAllByMaster(tattooMaster)
                .stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.ACCEPTED)
                .toList();
    }

    @Override
    public List<Order> findCompletedByMaster(TattooMaster tattooMaster) {
        return orderRepository.findAllByMaster(tattooMaster)
                .stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.COMPLETED)
                .toList();
    }

    @Override
    public List<Order> findAllByMaster(TattooMaster tattooMaster) {
        return orderRepository.findAllByMaster(tattooMaster);
    }

    @Override
    public void updateOrderStatus(long id, OrderStatus status) throws UtilException {
        Order order = orderRepository.findById(id).orElseThrow(NoOrderPresentedException::new);
        order.setOrderStatus(status);
        orderRepository.save(order);
    }
}
