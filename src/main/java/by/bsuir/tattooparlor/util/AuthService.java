package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.dao.repository.ClientRepository;
import by.bsuir.tattooparlor.dao.repository.TariffRepository;
import by.bsuir.tattooparlor.dao.repository.TattooMasterRepository;
import by.bsuir.tattooparlor.dao.repository.UserRepository;
import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.Tariff;
import by.bsuir.tattooparlor.entity.TattooMaster;
import by.bsuir.tattooparlor.entity.User;
import by.bsuir.tattooparlor.entity.helpers.UserRole;
import by.bsuir.tattooparlor.entity.helpers.UserStatus;
import by.bsuir.tattooparlor.util.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final TattooMasterRepository tattooMasterRepository;
    private final TariffRepository tariffRepository;

    @Autowired
    public AuthService(UserRepository userRepository, ClientRepository clientRepository, TattooMasterRepository tattooMasterRepository, TariffRepository tariffRepository) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.tattooMasterRepository = tattooMasterRepository;
        this.tariffRepository = tariffRepository;
    }

    @Override
    public Optional<Client> applyGuest(String name, String phone) throws UtilException {
        Optional<Client> clientOpt = clientRepository.findByPhone(phone);
        Client client = null;

        if(clientOpt.isEmpty()) {
            User user = applyGuestUser(phone);
            client = new Client();
            client.setId(user.getId());
            client.setUser(user);
            client.setName(name);
            client.setPhone(phone);
        } else {
            client = clientOpt.get();
            if (!client.getUser().getLogin().equals(getGuestUserLogin(phone))) {
                throw new UserPresentedException();
            }
            client.setName(name);
        }

        Client savedClient = clientRepository.save(client);
        return Optional.of(savedClient);
    }

    @Override
    public Client applyClient(String username, String password, String email, String name, String phone) throws UtilException {
        ThrowIfExistUser(username, email);

        Optional<Client> guestOpt = clientRepository.findByPhone(phone);
        Client client = null;
        long clientId = 0;
        if (guestOpt.isPresent()) {
            client = guestOpt.get();
            User user = client.getUser();
            user.setLogin(username);
            user.setPassword(PasswordProtector.checkPassword(password));
            user.setEmail(email);
            client.setName(name);

            clientId = client.getId();
        } else {
            User user = applyUser(username, email, password);
            clientId = user.getId();

            client = new Client(name, phone, user);
            client.setId(clientId);
        }

        clientRepository.save(client);
        Optional<Client> savedClientOpt = clientRepository.findById(clientId);

        return savedClientOpt.orElseThrow(ApplyClientException::new);
    }

    @Override
    public TattooMaster applyMaster(String username, String password, String email, String name, Date workStarted) throws UtilException{
        User savedUser = applyUser(username, email, password);
        savedUser.setRole(UserRole.MODERATOR);
        savedUser.setStatus(UserStatus.CONFIRMED);
        long savedUserId = savedUser.getId();

        TattooMaster tattooMaster = new TattooMaster(name, workStarted, savedUser);
        tattooMaster.setId(savedUserId);
        tattooMaster.setTariff(tariffRepository.findById(1L).orElseThrow());
        tattooMasterRepository.save(tattooMaster);
        Optional<TattooMaster> savedMasterOpt = tattooMasterRepository.findById(savedUserId);

        return savedMasterOpt.orElseThrow(ApplyMasterException::new);
    }

    private User applyUser(String username, String email, String password) throws UserPresentedException, ApplyUserException {
        ThrowIfExistUser(username, email);
        password = PasswordProtector.checkPassword(password);

        User user = new User(username, password, email);
        user.setRole(UserRole.CLIENT);
        user.setStatus(UserStatus.UNCONFIRMED);
        userRepository.save(user);
        Optional<User> savedOpt = userRepository.findByLogin(username);
        return savedOpt.orElseThrow(ApplyUserException::new);
    }

    private void ThrowIfExistUser(String username, String email) throws UserPresentedException {
        Optional<User> userOpt = userRepository.findByLoginOrEmail(username, email);
        if (userOpt.isPresent()) {
            throw new UserPresentedException();
        }
    }

    @Override
    public User tryAuthenticate(String username, String password) throws UtilException {
        Optional<User> userOpt = userRepository.findByLogin(username);
        User user = userOpt.orElseThrow(IllegalLoginException::new);
        password = PasswordProtector.checkPassword(password);
        if(!user.getPassword().equals(password)) {
            throw new IllegalPasswordException();
        }

        if(user.getStatus() == UserStatus.BLOCKED) {
            throw new UserBlockedException();
        }

        return user;
    }

    @Override
    public Client tryAuthorizeClient(User user) throws UtilException {
        if(user.getRole() == UserRole.CLIENT) {
            Optional<Client> clientOpt = clientRepository.findById(user.getId());
            return clientOpt.orElseThrow(NoClientPresentedException::new);
        } else {
            throw new IllegalRolePresentedException();
        }
    }

    @Override
    public TattooMaster tryAuthorizeMaster(User user) throws UtilException {
        if(user.getRole() == UserRole.MODERATOR) {
            Optional<TattooMaster> tattooMasterOpt = tattooMasterRepository.findById(user.getId());
            return tattooMasterOpt.orElseThrow(NoMasterPresentedException::new);
        } else {
            throw new IllegalRolePresentedException();
        }
    }

    @Override
    public User tryAuthorizeAdmin(User user) throws UtilException {
        if(user.getRole() == UserRole.ADMIN) {
            return user;
        } else {
            throw new IllegalRolePresentedException();
        }
    }

    private User applyGuestUser(String phone) throws UtilException {
        String login = getGuestUserLogin(phone);
        userRepository.save(new User(login, "", ""));
        Optional<User> user = userRepository.findByLogin(login);
        return user.orElseThrow(NoGuestUserPresentedException::new);
    }

    private String getGuestUserLogin(String phone) {
        return "guest_" + Integer.toHexString(phone.hashCode());
    }

    private static class PasswordValidator {

        private static final String PASSWORD_PATTERN = ".{6,20}";

        private static boolean isValidPassword(String password) {
            return password.matches(PASSWORD_PATTERN);
        }
    }

    private static class PasswordProtector {

        private static final String GLOBAL_SALT = "$%^";
        private static final int hashDeep = 8;

        private static String checkPassword(String password) {
            int hash = (password + GLOBAL_SALT).hashCode();
            for(int i=0; i < hashDeep; ++i) {
                hash = (hash + GLOBAL_SALT).hashCode();
            }
            return Integer.toHexString(hash);
        }

    }
}
