package by.bsuir.tattooparlor.util;

import by.bsuir.tattooparlor.dao.repository.ClientRateRepository;
import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.ClientRate;
import by.bsuir.tattooparlor.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ClientRateManager implements IClientRateManager {

    private final ClientRateRepository clientRateRepository;

    @Autowired
    public ClientRateManager(ClientRateRepository clientRateRepository) {
        this.clientRateRepository = clientRateRepository;
    }

    @Override
    public boolean rate(Client client, Product product, int value) {
        Optional<ClientRate> rate = clientRateRepository.findByClientAndProduct(client, product);
        if(rate.isPresent()) {
            ClientRate clientRate = rate.get();
            if(clientRate.getMark() == value) {
                return false;
            } else {
                clientRate.setMark(value);
                clientRateRepository.save(clientRate);
                return true;
            }
        }

        ClientRate clientRate = new ClientRate(client, product, value);
        clientRateRepository.save(clientRate);
        return true;
    }

    @Override
    public List<Product> getLikedProducts(Client client) {
        return clientRateRepository.findAllByClient(client)
                .stream()
                .filter(clientRate -> clientRate.getMark() > 0)
                .map(ClientRate::getProduct)
                .toList();
    }
}
