package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.entity.Client;

public interface IClientManager {
    void updateProfilePictureUri(Client client, String profilePictureUri);
    void update(Client client);
}
