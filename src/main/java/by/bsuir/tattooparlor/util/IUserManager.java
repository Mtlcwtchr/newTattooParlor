package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.entity.User;
import by.bsuir.tattooparlor.entity.helpers.UserRole;
import by.bsuir.tattooparlor.entity.helpers.UserStatus;
import by.bsuir.tattooparlor.util.exception.UtilException;

import java.util.List;

public interface IUserManager {
    List<User> findAll();
    boolean setStatus(long id, UserRole requesterRole, UserStatus status) throws UtilException;
}
