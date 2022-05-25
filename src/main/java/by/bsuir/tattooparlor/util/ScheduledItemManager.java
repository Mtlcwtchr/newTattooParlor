package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.dao.repository.ScheduledItemRepository;
import by.bsuir.tattooparlor.entity.Order;
import by.bsuir.tattooparlor.entity.ScheduledItem;
import by.bsuir.tattooparlor.entity.TattooMaster;
import by.bsuir.tattooparlor.entity.helpers.OrderStatus;
import by.bsuir.tattooparlor.util.exception.NoScheduledItemPresentedException;
import by.bsuir.tattooparlor.util.exception.UtilException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScheduledItemManager implements IScheduledItemManager {

    private final ScheduledItemRepository scheduledItemRepository;

    @Autowired
    public ScheduledItemManager(ScheduledItemRepository scheduledItemRepository) {
        this.scheduledItemRepository = scheduledItemRepository;
    }

    @Override
    public List<ScheduledItem> findUnacceptedByMaster(TattooMaster tattooMaster) {
        return scheduledItemRepository.findAllByMaster(tattooMaster).stream().filter(item -> item.getOrderStatus() == OrderStatus.REQUESTED).collect(Collectors.toList());
    }

    @Override
    public List<ScheduledItem> findAcceptedByMaster(TattooMaster tattooMaster) {
        return scheduledItemRepository.findAllByMaster(tattooMaster).stream().filter(item -> item.getOrderStatus() == OrderStatus.ACCEPTED).collect(Collectors.toList());
    }

    @Override
    public List<ScheduledItem> findCompletedByMaster(TattooMaster tattooMaster) {
        return scheduledItemRepository.findAllByMaster(tattooMaster).stream().filter(item -> item.getOrderStatus() == OrderStatus.COMPLETED).collect(Collectors.toList());
    }

    @Override
    public List<ScheduledItem> findAll() {
        return scheduledItemRepository.findAll();
    }

    @Override
    public ScheduledItem findById(long id) throws UtilException {
        return scheduledItemRepository.findById(id).orElseThrow(NoScheduledItemPresentedException::new);
    }

    @Override
    public ScheduledItem save(ScheduledItem scheduledItem) {
        return scheduledItemRepository.saveAndFlush(scheduledItem);
    }
}
