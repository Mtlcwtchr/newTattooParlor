package by.bsuir.tattooparlor.controller;

import by.bsuir.tattooparlor.config.GlobalPaths;
import by.bsuir.tattooparlor.controller.helpers.ImageSize;
import by.bsuir.tattooparlor.entity.Product;
import by.bsuir.tattooparlor.entity.TattooMaster;
import by.bsuir.tattooparlor.util.ITattooMasterManager;
import by.bsuir.tattooparlor.util.calculator.ICalculationsManager;
import by.bsuir.tattooparlor.util.exception.UtilException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Controller
public class PriceCalculationsController {

    private static final String SRC = GlobalPaths.IMAGES_SRC;

    private final ICalculationsManager calculationsManager;
    private final ITattooMasterManager masterManager;

    @Autowired
    public PriceCalculationsController(ICalculationsManager calculationsManager, ITattooMasterManager masterManager) {
        this.calculationsManager = calculationsManager;
        this.masterManager = masterManager;
    }

    @RequestMapping(value = "/calculator", method = {RequestMethod.POST, RequestMethod.GET})
    public String proceedToCalculator(@RequestParam(name = "uploadedFileUri", required = false) String fileUri,
                                      @RequestParam(name = "totalCost", required = false) Integer totalCost,
                                      Model model) {
        model.addAttribute("masters", masterManager.findAll());
        model.addAttribute("uploadedFileUri", fileUri);
        model.addAttribute("imageSizes", ImageSize.TEN);
        model.addAttribute("totalCost", totalCost);

        return "calculator";
    }

    @RequestMapping("/calculateImageCost")
    public String calculateImageCost(@RequestParam(name = "uploadFile") MultipartFile multipartFile,
                                     @RequestParam(name = "colorsCount") int colorsCount,
                                     @RequestParam(name = "masterId") int masterId,
                                     @RequestParam(name = "imageSize") ImageSize size,
                                     Model model,
                                     HttpSession session) {
        try {
            String fileName = getPictureNewUri(
                    Integer.toHexString(multipartFile.getName().hashCode() + new Date().hashCode()),
                    multipartFile.getContentType().replace("/", "."));
            String fileUri = fileName;

            File file = trySaveNewPictureByPath(multipartFile, fileName);
            BufferedImage image = ImageIO.read(file);

            TattooMaster master = null;
            if (masterId == 0) {
                master = masterManager.findPrior();
            } else {
                master = masterManager.findById(masterId);
            }

            int totalCost = calculationsManager.getTotalCost(image, colorsCount, master, size);
            int difficulty = calculationsManager.getDifficulty(image);

            Product product = new Product();
            product.setImageUri(fileUri);
            product.setColorsCount(colorsCount);
            product.setDifficulty(difficulty);

            session.setAttribute("product", product);
            session.setAttribute("master", master);
            session.setAttribute("imageSize", size);
            session.setAttribute("totalCost", totalCost);

            model.addAttribute("product", product);
            model.addAttribute("master", master);
            model.addAttribute("imageSize", size);
            model.addAttribute("totalCost", totalCost + " BYN");
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (UtilException ex) {
            ex.printStackTrace();
        }

        return "calculating-results";
    }

    private String getPictureNewUri(String fileName, String contentType) {
        return SRC + Integer.toHexString(fileName.hashCode()) + contentType;
    }

    private File trySaveNewPictureByPath(MultipartFile multipartFile, String pictureUri) throws IOException {
        File file = new File(pictureUri);
        multipartFile.transferTo(file);
        return file;
    }
}
