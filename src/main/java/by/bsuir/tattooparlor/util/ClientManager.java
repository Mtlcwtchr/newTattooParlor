package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.dao.repository.ClientRepository;
import by.bsuir.tattooparlor.entity.Client;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientManager implements IClientManager {

    private final ClientRepository clientRepository;

    public ClientManager(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void updateProfilePictureUri(Client client, String profilePictureUri) {
        if(client.getProfilePictureUri() != null && client.getProfilePictureUri().equals(profilePictureUri)) {
            return;
        }

        client.setProfilePictureUri(profilePictureUri);
        clientRepository.save(client);
    }

    public Client update(Client client) {
        return clientRepository.saveAndFlush(client);
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }
}
