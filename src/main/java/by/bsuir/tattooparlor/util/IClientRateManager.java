package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.Product;

public interface IClientRateManager {
    boolean rate(Client client, Product product, int value);
}
