package by.bsuir.tattooparlor.controller;

import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.Product;
import by.bsuir.tattooparlor.util.IClientRateManager;
import by.bsuir.tattooparlor.util.IProductManager;
import by.bsuir.tattooparlor.util.ListUtils;
import by.bsuir.tattooparlor.util.exception.UtilException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class GalleryController {

    private final IProductManager productManager;
    private final IClientRateManager clientRateManager;

    @Autowired
    public GalleryController(IProductManager productManager, IClientRateManager clientRateManager) {
        this.productManager = productManager;
        this.clientRateManager = clientRateManager;
    }

    @GetMapping("/")
    public String proceedToMain(Model model) {
        List<Product> products = productManager.findAll();
        Collections.sort(products, this::sortByLikes);
        Collections.reverse(products);
        products = products.stream().limit(8).toList();
        List<List<Product>> quarts = ListUtils.mapToQuarts(products);

        model.addAttribute("products", quarts);
        return "index";
    }

    @GetMapping("/gallery")
    public String proceedToGallery(Model model) {
        List<Product> products = productManager.findAll();
        List<List<Product>> quarts = ListUtils.mapToQuarts(products);

        model.addAttribute("products", quarts);
        return "gallery";
    }

    @GetMapping("/rate")
    public String proceedRate(HttpSession session,
                              @RequestParam(name = "productId") int productId,
                              @RequestParam(name = "markValue") int value) {
        try {
            Client actor = (Client) session.getAttribute("currentClient");
            Product product = productManager.findById(productId);
            if(actor != null && product != null) {
                clientRateManager.rate(actor, product, value);
            }
        } catch (UtilException e) {
            e.printStackTrace();
        }

        return "redirect:/gallery";
    }

    private int sortByLikes(Product m, Product n) {
        return Long.compare(m.getLikesCount(), n.getLikesCount());
    }

}
