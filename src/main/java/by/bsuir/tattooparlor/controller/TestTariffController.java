package by.bsuir.tattooparlor.controller;

import by.bsuir.tattooparlor.entity.Tariff;
import by.bsuir.tattooparlor.dao.repository.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class TestTariffController {

    private TariffRepository _tariffRepository;

    @Autowired
    public TestTariffController(TariffRepository tariffRepository) {
        _tariffRepository = tariffRepository;
    }

    @GetMapping("/test")
    public String test(HttpServletRequest request, HttpServletResponse response) {
        var newTariff = _tariffRepository.save(new Tariff(1));
        var savedTariff = _tariffRepository.findAll().stream().findFirst().orElseThrow();

        request.setAttribute("newTariff", newTariff);
        request.setAttribute("savedTariff", savedTariff);

        return "test";
    }

}
