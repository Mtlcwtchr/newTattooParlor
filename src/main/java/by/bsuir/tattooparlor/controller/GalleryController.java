package by.bsuir.tattooparlor.controller;

import by.bsuir.tattooparlor.config.GlobalPaths;
import by.bsuir.tattooparlor.entity.Client;
import by.bsuir.tattooparlor.entity.Product;
import by.bsuir.tattooparlor.entity.User;
import by.bsuir.tattooparlor.entity.helpers.GalleryType;
import by.bsuir.tattooparlor.entity.helpers.UserRole;
import by.bsuir.tattooparlor.util.IClientRateManager;
import by.bsuir.tattooparlor.util.IProductManager;
import by.bsuir.tattooparlor.util.ListUtils;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
public class GalleryController {

    private final ICalculationsManager calculationsManager;
    private final IProductManager productManager;
    private final IClientRateManager clientRateManager;

    @Autowired
    public GalleryController(ICalculationsManager calculationsManager, IProductManager productManager, IClientRateManager clientRateManager) {
        this.calculationsManager = calculationsManager;
        this.productManager = productManager;
        this.clientRateManager = clientRateManager;
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
        Collections.sort(products, this::sortByLikes);
        Collections.reverse(products);
        List<List<Product>> quarts = ListUtils.mapToQuarts(products);

        model.addAttribute("products", quarts);
        return retVal;
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
                String fileName = getPictureNewUri(
                        Integer.toHexString(multipartFile.getName().hashCode() + new Date().hashCode()),
                        multipartFile.getContentType().replace("/", "."));
                String fileUri = fileName;

                File file = trySaveNewPictureByPath(multipartFile, fileName);
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

    private int sortByLikes(Product m, Product n) {
        return Long.compare(m.getLikesCount(), n.getLikesCount());
    }

    private String getPictureNewUri(String fileName, String contentType) {
        return GlobalPaths.IMAGES_SRC + Integer.toHexString(fileName.hashCode()) + contentType;
    }

    private File trySaveNewPictureByPath(MultipartFile multipartFile, String pictureUri) throws IOException {
        File file = new File(pictureUri);
        multipartFile.transferTo(file);
        return file;
    }
}
