package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.config.GlobalPaths;
import by.bsuir.tattooparlor.dao.repository.OrderRepository;
import by.bsuir.tattooparlor.dao.repository.ProductRepository;
import by.bsuir.tattooparlor.entity.Order;
import by.bsuir.tattooparlor.entity.Product;
import by.bsuir.tattooparlor.entity.helpers.GalleryType;
import by.bsuir.tattooparlor.entity.helpers.OrderStatus;
import by.bsuir.tattooparlor.util.calculator.ICalculationsManager;
import by.bsuir.tattooparlor.util.exception.NoProductPresentedException;
import by.bsuir.tattooparlor.util.exception.UtilException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProductManager implements IProductManager{

    private final ProductRepository productRepository;

    @Autowired
    public ProductManager(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findAllCompleted() {
        return findAll().stream().filter(product -> product.getGalleryType() == GalleryType.COMPLETED).collect(Collectors.toList());
    }

    @Override
    public List<Product> findAllGallery() {
        return findAll().stream().filter(product -> product.getGalleryType() == GalleryType.GALLERY).collect(Collectors.toList());
    }

    @Override
    public Product findById(long id) throws UtilException {
        return productRepository.findById(id).orElseThrow(NoProductPresentedException::new);
    }

    @Override
    public void delete(long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product save(Product product) {
        return productRepository.saveAndFlush(product);
    }
}
