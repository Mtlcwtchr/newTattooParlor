package by.bsuir.tattooparlor.util;


import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.ClientDiscount;
import by.bsuir.tattooparlor.entity.Discount;
import by.bsuir.tattooparlor.entity.Product;
import by.bsuir.tattooparlor.util.exception.UtilException;

import java.util.Date;
import java.util.List;

public interface IDiscountManager {
    Discount create(int perc, String desc, Date expires);
    List<Discount> findAll();
    List<ClientDiscount> findAllForClient(Client client);
    ClientDiscount findByPromoForClient(Client client, String promo) throws UtilException;
    ClientDiscount tryAddPromoToClient(Client client, String promo) throws UtilException;
    Discount findById(long id) throws UtilException;
    Discount save(Discount discount);
    ClientDiscount save(ClientDiscount clientDiscount);
    void delete(long id);
}
