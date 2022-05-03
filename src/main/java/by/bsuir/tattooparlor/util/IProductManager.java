package by.bsuir.tattooparlor.util;


import by.bsuir.tattooparlor.entity.Product;
import by.bsuir.tattooparlor.util.exception.UtilException;

import java.util.List;

public interface IProductManager {
    List<Product> findAll();
    Product findById(long id) throws UtilException;
    void delete(long id);
}
