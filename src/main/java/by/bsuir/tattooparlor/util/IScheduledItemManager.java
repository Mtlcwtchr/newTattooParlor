package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.entity.Order;
import by.bsuir.tattooparlor.entity.ScheduledItem;
import by.bsuir.tattooparlor.entity.TattooMaster;
import by.bsuir.tattooparlor.util.exception.UtilException;

import java.util.List;

public interface IScheduledItemManager {
    List<ScheduledItem> findAll();
    List<ScheduledItem> findUnacceptedByMaster(TattooMaster tattooMaster);
    List<ScheduledItem> findAcceptedByMaster(TattooMaster tattooMaster);
    List<ScheduledItem> findCompletedByMaster(TattooMaster tattooMaster);
    ScheduledItem findById(long id) throws UtilException;
    ScheduledItem save(ScheduledItem scheduledItem);
}
