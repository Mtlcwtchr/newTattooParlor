package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.dao.repository.OrderRepository;
import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.Order;
import by.bsuir.tattooparlor.entity.TattooMaster;
import by.bsuir.tattooparlor.entity.helpers.OrderStatus;
import by.bsuir.tattooparlor.util.exception.NoOrderPresentedException;
import by.bsuir.tattooparlor.util.exception.UtilException;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderManager implements IOrderManager {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderManager(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order findById(long id) throws UtilException {
        return orderRepository.findById(id).orElseThrow(NoOrderPresentedException::new);
    }

    @Override
    public List<Order> findAllCompleted() {
        return orderRepository.findAll().stream().filter(order -> order.getOrderStatus() == OrderStatus.COMPLETED).collect(Collectors.toList());
    }

    public List<Order> findAllByClient(Client client) {
        return orderRepository.findAllByClient(client);
    }

    @Override
    public List<Order> findUnacceptedByMaster(TattooMaster tattooMaster) {
        List<Order> orders = orderRepository.findAllByMaster(tattooMaster)
                .stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.REQUESTED)
                .collect(Collectors.toList());
        return orders;
    }

    @Override
    public List<Order> findAcceptedByMaster(TattooMaster tattooMaster) {
        List<Order> orders = orderRepository.findAllByMaster(tattooMaster)
                .stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.ACCEPTED)
                .collect(Collectors.toList());
        return orders;
    }

    @Override
    public List<Order> findCompletedByMaster(TattooMaster tattooMaster) {
        List<Order> orders = orderRepository.findAllByMaster(tattooMaster)
                .stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.COMPLETED)
                .collect(Collectors.toList());
        return orders;
    }

    @Override
    public List<Order> findAllByMaster(TattooMaster tattooMaster) {
        List<Order> orders = orderRepository.findAllByMaster(tattooMaster);
        return orders == null ? Collections.emptyList() : orders;
    }

    @Override
    public void updateOrderStatus(long id, OrderStatus status) throws UtilException {
        Order order = orderRepository.findById(id).orElseThrow(NoOrderPresentedException::new);
        order.setOrderStatus(status);
        orderRepository.save(order);
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.saveAndFlush(order);
    }
}
