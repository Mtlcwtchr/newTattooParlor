package by.bsuir.tattooparlor.util;


import by.bsuir.tattooparlor.dao.repository.ClientDiscountRepository;
import by.bsuir.tattooparlor.dao.repository.DiscountRepository;
import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.ClientDiscount;
import by.bsuir.tattooparlor.entity.Discount;
import by.bsuir.tattooparlor.entity.helpers.ClientDiscountStatus;
import by.bsuir.tattooparlor.util.exception.NoDiscountPresentedException;
import by.bsuir.tattooparlor.util.exception.UtilException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DiscountManager implements IDiscountManager {

    private final ClientDiscountRepository clientDiscountRepository;
    private final DiscountRepository discountRepository;

    @Autowired
    public DiscountManager(ClientDiscountRepository clientDiscountRepository, DiscountRepository discountRepository) {
        this.clientDiscountRepository = clientDiscountRepository;
        this.discountRepository = discountRepository;
    }

    @Override
    public List<Discount> findAll() {
        return discountRepository.findAll();
    }

    @Override
    public List<ClientDiscount> findAllForClient(Client client) {
        return clientDiscountRepository.findAllByOwner(client);
    }

    @Override
    public Discount findById(long id) throws UtilException {
        return discountRepository.findById(id).orElseThrow(NoDiscountPresentedException::new);
    }

    @Override
    public Discount save(Discount discount) {
        return discountRepository.saveAndFlush(discount);
    }

    @Override
    public ClientDiscount save(ClientDiscount clientDiscount) {
        return clientDiscountRepository.saveAndFlush(clientDiscount);
    }

    @Override
    public ClientDiscount findByPromoForClient(Client client, String promo) throws UtilException {
        Discount discount = discountRepository.findByPromocode(promo).orElseThrow(NoDiscountPresentedException::new);
        ClientDiscount clientDiscount = clientDiscountRepository.findByDiscountAndOwner(discount, client).orElseThrow(NoDiscountPresentedException::new);

        return clientDiscount;
    }
}
