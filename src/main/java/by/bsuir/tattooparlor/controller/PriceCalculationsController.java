package by.bsuir.tattooparlor.controller;

import by.bsuir.tattooparlor.controller.helpers.ImageSize;
import by.bsuir.tattooparlor.entity.Tariff;
import by.bsuir.tattooparlor.entity.TattooMaster;
import by.bsuir.tattooparlor.util.calculator.CalculationsManager;
import by.bsuir.tattooparlor.util.calculator.ICalculationsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Controller
public class PriceCalculationsController {

    private static final String SRC = "/Users/stanislav/idea/new/TattooParlor/src/main/resources/static/imgs/";

    private final ICalculationsManager calculationsManager;

    public PriceCalculationsController(ICalculationsManager calculationsManager) {
        this.calculationsManager = calculationsManager;
    }

    public String calculateImageCost(@RequestParam(name = "image") MultipartFile multipartFile,
                                     @RequestParam(name = "colorsCount") int colorsCount,
                                     @RequestParam(name = "masterId") int masterId,
                                     @RequestParam(name = "size") ImageSize size) {
        try {
            String fileName = Integer.toHexString(multipartFile.getName().hashCode() + new Date().hashCode());
            File file = new File(SRC + fileName);
            multipartFile.transferTo(file);
            BufferedImage image = ImageIO.read(file);
            TattooMaster tattooMaster = new TattooMaster();
            calculationsManager.getTotalCost(image, colorsCount, tattooMaster, size);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return "price-calculations";
    }

}
