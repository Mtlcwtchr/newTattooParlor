package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.entity.Client;

import java.util.List;

public interface IClientManager {
    void updateProfilePictureUri(Client client, String profilePictureUri);
    void update(Client client);
    List<Client> findAll();
}
