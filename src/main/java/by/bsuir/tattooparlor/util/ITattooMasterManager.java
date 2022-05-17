package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.entity.TattooMaster;
import by.bsuir.tattooparlor.util.exception.UtilException;

import java.util.List;

public interface ITattooMasterManager {
    List<TattooMaster> findAll();
    TattooMaster findById(long id) throws UtilException;
    TattooMaster findPrior() throws UtilException;
}
