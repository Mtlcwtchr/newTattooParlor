package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.dao.repository.ProductRepository;
import by.bsuir.tattooparlor.entity.Product;
import by.bsuir.tattooparlor.util.exception.NoProductPresentedException;
import by.bsuir.tattooparlor.util.exception.UtilException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductManager implements IProductManager{

    private final ProductRepository productRepository;

    @Autowired
    public ProductManager(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public List<Product> findAll() {
        return new ArrayList<>(productRepository.findAll());
    }

    @Override
    public Product findById(long id) throws UtilException {
        return productRepository.findById(id).orElseThrow(NoProductPresentedException::new);
    }

    @Override
    public void delete(long id) {
        productRepository.deleteById(id);
    }

}
