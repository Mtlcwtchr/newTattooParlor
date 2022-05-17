package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.dao.repository.UserRepository;
import by.bsuir.tattooparlor.entity.User;
import by.bsuir.tattooparlor.entity.helpers.UserRole;
import by.bsuir.tattooparlor.entity.helpers.UserStatus;
import by.bsuir.tattooparlor.util.exception.NoUserPresentedException;
import by.bsuir.tattooparlor.util.exception.UtilException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserManager implements IUserManager {

    private final UserRepository userRepository;

    @Autowired
    public UserManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean setStatus(long id, UserRole requesterRole, UserStatus status) throws UtilException {
        User user = userRepository.findById(id).orElseThrow(NoUserPresentedException::new);

        if(!requesterRole.isPrior(user.getRole())) {
            return false;
        }

        user.setStatus(status);
        userRepository.save(user);
        return true;
    }


}
