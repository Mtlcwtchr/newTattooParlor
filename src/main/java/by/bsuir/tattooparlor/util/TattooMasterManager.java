package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.dao.repository.TattooMasterRepository;
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
    public List<TattooMaster> findAll() {
        return tattooMasterRepository.findAll();
    }

    @Override
    public TattooMaster findById(long id) throws UtilException {
        return tattooMasterRepository.findById(id).orElseThrow(NoMasterPresentedException::new);
    }
}
