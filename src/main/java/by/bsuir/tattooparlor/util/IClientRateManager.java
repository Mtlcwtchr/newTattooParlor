package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.Product;

import java.util.List;

public interface IClientRateManager {
    boolean rate(Client client, Product product, int value);
    List<Product> getLikedProducts(Client client);
}
