package by.bsuir.tattooparlor;

import by.bsuir.tattooparlor.dao.repository.ClientRepository;
import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.TattooMaster;
import by.bsuir.tattooparlor.util.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
public class TattooParlorApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(TattooParlorApplication.class, args);
    }

    private IAuthService authService;
    private ClientRepository clientRepository;

    @Autowired
    public  TattooParlorApplication(IAuthService authService, ClientRepository clientRepository) {
        this.authService = authService;
        this.clientRepository = clientRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Optional<Client> sergey = authService.applyGuest("sergey", "+375336951932");
        System.out.println(sergey);
        Optional<Client> dima = authService.applyGuest("dima", "+375336666666");
        System.out.println(dima);
        Optional<Client> notDima = authService.applyGuest("nikita", "+375336666666");
        System.out.println(notDima);

        System.out.println(clientRepository.findAll());
    }
}
