package by.bsuir.tattooparlor.controller;

import by.bsuir.tattooparlor.entity.Product;
import by.bsuir.tattooparlor.util.IProductManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GalleryController {

    private IProductManager productManager;

    @Autowired
    public GalleryController(IProductManager productManager) {
        this.productManager = productManager;
    }

    @GetMapping("/gallery")
    public String proceedToGallery(Model model) {
        List<Product> products = productManager.findAll();
        List<List<Product>> quarts = new ArrayList<>();

        List<Product> quart = new ArrayList<>();
        int j = 0;
        for (Product product : products) {
            quart.add(product);

            if(++j % 4 == 0 || j == products.size()) {
                quarts.add(quart);
                quart = new ArrayList<>();
            }
        }

        model.addAttribute("products", quarts);
        return "gallery";
    }

}
