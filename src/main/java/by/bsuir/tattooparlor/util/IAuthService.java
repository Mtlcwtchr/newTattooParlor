package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.TattooMaster;
import by.bsuir.tattooparlor.entity.User;
import by.bsuir.tattooparlor.util.exception.UtilException;

import java.util.Date;
import java.util.Optional;

public interface IAuthService {
    Optional<Client> applyGuest(String name, String phone) throws UtilException;
    Optional<Client> applyClient(String username, String password, String email, String name, String phone);
    Optional<TattooMaster> applyMaster(String username, String password, String email, String name, Date workStarted);
    Optional<User> tryAuthenticate(String username, String password);
    Optional<Client> tryAuthorizeClient(User user);
    Optional<TattooMaster> tryAuthorizeMaster(User user);
    Optional<User> tryAuthorizeAdmin(User user);
}
