package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.TattooMaster;
import by.bsuir.tattooparlor.util.exception.UtilException;

import java.util.List;

public interface ITattooMasterManager {
    List<TattooMaster> findAll();
    TattooMaster update(TattooMaster tattooMaster);
    TattooMaster findById(long id) throws UtilException;
    TattooMaster findPrior() throws UtilException;
    void updateProfilePictureUri(TattooMaster tattooMaster, String profilePictureUri);
}
