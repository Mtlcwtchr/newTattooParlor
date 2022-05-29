package by.bsuir.tattooparlor.controller;

import by.bsuir.tattooparlor.config.GlobalPaths;
import by.bsuir.tattooparlor.entity.*;
import by.bsuir.tattooparlor.entity.helpers.GalleryType;
import by.bsuir.tattooparlor.entity.helpers.UserRole;
import by.bsuir.tattooparlor.util.*;
import by.bsuir.tattooparlor.util.calculator.ICalculationsManager;
import by.bsuir.tattooparlor.util.exception.UtilException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class GalleryController {

    private final ICalculationsManager calculationsManager;
    private final IProductManager productManager;
    private final IClientRateManager clientRateManager;
    private final IOrderManager orderManager;

    public GalleryController(ICalculationsManager calculationsManager, IProductManager productManager, IClientRateManager clientRateManager, IOrderManager orderManager) {
        this.calculationsManager = calculationsManager;
        this.productManager = productManager;
        this.clientRateManager = clientRateManager;
        this.orderManager = orderManager;
    }

    @RequestMapping("/")
    public String proceedToMain(Model model) {
        List<Product> products = productManager.findAllGallery();
        Collections.sort(products, this::sortByLikes);
        Collections.reverse(products);
        products = products.stream().limit(8).toList();
        List<List<Product>> quarts = ListUtils.mapToQuarts(products);

        model.addAttribute("products", quarts);
        return "index";
    }

    @RequestMapping("/gallery")
    public String proceedToGallery(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        String retVal = "gallery";
        if (currentUser != null && currentUser.getRole() == UserRole.ADMIN) {
            retVal = "catalog-admin";
        }
        List<Product> products = productManager.findAllGallery();
        Collections.sort(products, Comparator.comparing(Product::getId));
        Collections.reverse(products);
        List<List<Product>> quarts = ListUtils.mapToQuarts(products);

        model.addAttribute("products", quarts);
        return retVal;
    }

    @RequestMapping("/workGallery")
    public String proceedToWorkGallery(HttpSession session, Model model) {
        List<Order> orders = orderManager.findAllCompleted().stream().filter(order -> !order.getProduct().getCompletedImageUri().isEmpty()).collect(Collectors.toList());
        model.addAttribute("products", mapToMasterOrdered(orders));
        return "work-gallery";
    }

    @RequestMapping("/rate")
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

    @PostMapping("/addNewItem")
    public String addNewItem(@RequestParam(name = "uploadFile") MultipartFile multipartFile,
                             HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if(currentUser != null && currentUser.getRole() == UserRole.ADMIN) {
            try {
                String fileName = FileUtils.getNewPictureUri(
                        Integer.toHexString(multipartFile.getName().hashCode() + new Date().hashCode()),
                        multipartFile.getContentType().replace("/", "."));
                String fileUri = fileName;

                File file = FileUtils.trySaveNewPictureByPath(multipartFile, fileName);
                BufferedImage image = ImageIO.read(file);
                int difficulty = calculationsManager.getDifficulty(image);
                int colorsCount = calculationsManager.getColorsCount(image);

                Product product = new Product();
                product.setImageUri(fileUri);
                product.setDifficulty(difficulty);
                product.setColorsCount(colorsCount);
                product.setGalleryType(GalleryType.GALLERY);

                productManager.save(product);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return "redirect:/gallery";
    }

    @RequestMapping("/deleteItem")
    public String deleteItem(HttpSession session,
                              @RequestParam(name = "productId") int productId) {
        User currentUser = (User) session.getAttribute("currentUser");
        if(currentUser != null && currentUser.getRole() == UserRole.ADMIN) {
            productManager.delete(productId);
        }
        return "redirect:/gallery";
    }

    private int sortByLikes(Product m, Product n) {
        return Long.compare(m.getLikesCount(), n.getLikesCount());
    }

    private Map<TattooMaster, List<Product>> mapToMasterOrdered(List<Order> orders) {
        if(orders == null || orders.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<TattooMaster, List<Product>> map = new HashMap<>();
        orders.sort(Comparator.comparing(Order::getDateTime));

        for (Order order : orders) {
            TattooMaster master = order.getMaster();
            if(!map.containsKey(master)) {
                map.put(master, new ArrayList<>());
            }
            map.get(master).add(order.getProduct());
        }

        return map;
    }
}
