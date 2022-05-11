package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.Order;

import java.util.List;

public interface IOrderManager {
    List<Order> findAllByClient(Client client);
}
