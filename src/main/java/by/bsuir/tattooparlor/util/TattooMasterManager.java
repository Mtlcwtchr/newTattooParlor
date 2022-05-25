package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.dao.repository.TattooMasterRepository;
import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.TattooMaster;
import by.bsuir.tattooparlor.util.exception.NoMasterPresentedException;
import by.bsuir.tattooparlor.util.exception.UtilException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TattooMasterManager implements ITattooMasterManager {

    private final TattooMasterRepository tattooMasterRepository;

    @Autowired
    public TattooMasterManager(TattooMasterRepository tattooMasterRepository) {
        this.tattooMasterRepository = tattooMasterRepository;
    }

    @Override
    public TattooMaster update(TattooMaster tattooMaster) {
        return tattooMasterRepository.saveAndFlush(tattooMaster);
    }

    @Override
    public List<TattooMaster> findAll() {
        return tattooMasterRepository.findAll();
    }

    @Override
    public TattooMaster findById(long id) throws UtilException {
        return tattooMasterRepository.findById(id).orElseThrow(NoMasterPresentedException::new);
    }

    @Override
    public TattooMaster findPrior() throws UtilException {
        List<TattooMaster> masters = tattooMasterRepository.findAll();
        return masters.stream().findAny().orElseThrow(NoMasterPresentedException::new);
    }

    @Override
    public void updateProfilePictureUri(TattooMaster tattooMaster, String profilePictureUri) {
        if(tattooMaster.getProfilePictureUri() != null && tattooMaster.getProfilePictureUri().equals(profilePictureUri)) {
            return;
        }

        tattooMaster.setProfilePictureUri(profilePictureUri);
        tattooMasterRepository.save(tattooMaster);
    }
}
