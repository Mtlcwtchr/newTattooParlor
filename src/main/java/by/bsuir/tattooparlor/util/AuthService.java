package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.dao.repository.ClientRepository;
import by.bsuir.tattooparlor.dao.repository.TattooMasterRepository;
import by.bsuir.tattooparlor.dao.repository.UserRepository;
import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.TattooMaster;
import by.bsuir.tattooparlor.entity.User;
import by.bsuir.tattooparlor.util.exception.NoGuestUserPresentedException;
import by.bsuir.tattooparlor.util.exception.UserPresentedException;
import by.bsuir.tattooparlor.util.exception.UtilException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class AuthService implements IAuthService {

    private UserRepository userRepository;
    private ClientRepository clientRepository;
    private TattooMasterRepository tattooMasterRepository;

    public AuthService(UserRepository userRepository, ClientRepository clientRepository, TattooMasterRepository tattooMasterRepository) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.tattooMasterRepository = tattooMasterRepository;
    }

    @Override
    public Optional<Client> applyGuest(String name, String phone) throws UtilException {
        Optional<Client> clientOpt = clientRepository.findByPhone(phone);
        Client client = null;

        if(clientOpt.isEmpty()) {
            User user = applyGuestUser(phone);
            client = new Client();
            client.setId(user.getId());
            client.setLogin(user.getLogin());
            client.setPassword(user.getPassword());
            client.setEmail(user.getEmail());
            client.setName(name);
            client.setPhone(phone);
        } else {
            client = clientOpt.get();
            if (!client.getLogin().equals(getGuestUserLogin(phone))) {
                throw new UserPresentedException();
            }
            client.setName(name);
        }

        Client savedClient = clientRepository.save(client);
        return Optional.of(savedClient);
    }

    @Override
    public Optional<Client> applyClient(String username, String password, String email, String name, String phone) {
        return Optional.empty();
    }

    @Override
    public Optional<TattooMaster> applyMaster(String username, String password, String email, String name, Date workStarted) {
        return Optional.empty();
    }

    @Override
    public Optional<User> tryAuthenticate(String username, String password) {
        return Optional.empty();
    }

    @Override
    public Optional<Client> tryAuthorizeClient(User user) {
        return Optional.empty();
    }

    @Override
    public Optional<TattooMaster> tryAuthorizeMaster(User user) {
        return Optional.empty();
    }

    @Override
    public Optional<User> tryAuthorizeAdmin(User user) {
        return Optional.empty();
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
}
