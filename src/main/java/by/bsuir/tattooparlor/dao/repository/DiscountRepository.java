package by.bsuir.tattooparlor.dao.repository;

import by.bsuir.tattooparlor.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Optional<Discount> findByPromocode(String promo);
}
