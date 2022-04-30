package by.bsuir.tattooparlor.dao.repository;

import by.bsuir.tattooparlor.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
