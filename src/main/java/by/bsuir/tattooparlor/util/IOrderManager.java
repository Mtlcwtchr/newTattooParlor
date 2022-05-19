package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.Order;
import by.bsuir.tattooparlor.entity.TattooMaster;

import java.util.List;

public interface IOrderManager {
    List<Order> findAllByClient(Client client);
    List<Order> findUnacceptedByMaster(TattooMaster tattooMaster);
    List<Order> findAcceptedByMaster(TattooMaster tattooMaster);
    List<Order> findCompletedByMaster(TattooMaster tattooMaster);
    List<Order> findAllByMaster(TattooMaster tattooMaster);
}
