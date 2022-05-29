package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.TattooMaster;
import by.bsuir.tattooparlor.entity.User;
import by.bsuir.tattooparlor.util.exception.IllegalCredentialsException;
import by.bsuir.tattooparlor.util.exception.NoClientPresentedException;
import by.bsuir.tattooparlor.util.exception.NoMasterPresentedException;
import by.bsuir.tattooparlor.util.exception.UtilException;

import java.util.Date;
import java.util.Optional;

public interface IAuthService {
    Optional<Client> applyGuest(String name, String phone) throws UtilException;
    Client applyClient(String username, String password, String email, String name, String phone) throws UtilException;
    TattooMaster applyMaster(String username, String password, String email, String name, Date workStarted) throws UtilException;
    User tryAuthenticate(String username, String password) throws UtilException;
    Client tryAuthorizeClient(User user) throws UtilException;
    TattooMaster tryAuthorizeMaster(User user) throws UtilException;
    User tryAuthorizeAdmin(User user) throws UtilException;
    boolean updatePassword(long id, String password) throws UtilException;
}
