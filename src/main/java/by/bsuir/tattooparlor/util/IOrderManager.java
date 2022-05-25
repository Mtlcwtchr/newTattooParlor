package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.Order;
import by.bsuir.tattooparlor.entity.TattooMaster;
import by.bsuir.tattooparlor.entity.helpers.OrderStatus;
import by.bsuir.tattooparlor.util.exception.UtilException;

import java.util.List;

public interface IOrderManager {
    Order findById(long id) throws UtilException;
    List<Order> findAllCompleted();
    List<Order> findAllByClient(Client client);
    List<Order> findUnacceptedByMaster(TattooMaster tattooMaster);
    List<Order> findAcceptedByMaster(TattooMaster tattooMaster);
    List<Order> findCompletedByMaster(TattooMaster tattooMaster);
    List<Order> findAllByMaster(TattooMaster tattooMaster);
    void updateOrderStatus(long id, OrderStatus status) throws UtilException;
    Order saveOrder(Order order);
}
