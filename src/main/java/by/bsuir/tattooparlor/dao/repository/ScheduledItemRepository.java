package by.bsuir.tattooparlor.dao.repository;

import by.bsuir.tattooparlor.entity.ScheduledItem;
import by.bsuir.tattooparlor.entity.TattooMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduledItemRepository extends JpaRepository<ScheduledItem, Long> {
    List<ScheduledItem> findAllByMaster(TattooMaster master);
}