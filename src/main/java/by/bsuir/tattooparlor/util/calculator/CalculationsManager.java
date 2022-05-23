package by.bsuir.tattooparlor.util.calculator;

import by.bsuir.tattooparlor.controller.helpers.ImageSize;
import by.bsuir.tattooparlor.entity.Product;
import by.bsuir.tattooparlor.entity.TattooMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
public class CalculationsManager implements ICalculationsManager {

    private static final int COLORS_PRICE_MULTIPLIER = 5;
    private static final int MIN_COST = 30;

    private final IImageProcessor imageProcessor;

    @Autowired
    public CalculationsManager(IImageProcessor imageProcessor) {
        this.imageProcessor = imageProcessor;
    }

    public int getDifficulty(BufferedImage image) {
        return imageProcessor.getImageDifficulty(image);
    }

    public int getTotalCost(BufferedImage image, int colorsCount, TattooMaster master, ImageSize size) {
        int imageDifficulty = imageProcessor.getImageDifficulty(image);
        colorsCount = colorsCount == 0 ? imageProcessor.getSimpleColors(image).size() : colorsCount;
        int normalizedValue = master.getTariff().getNormalizedValue();
        float sizePriceMultiplier = size.getPriceMultiplier();

        return MIN_COST + ((int) (imageDifficulty * normalizedValue * sizePriceMultiplier + (colorsCount - 1) * COLORS_PRICE_MULTIPLIER));
    }

    public int getTotalCost(Product product, TattooMaster master, ImageSize size) {
        int imageDifficulty = product.getDifficulty();
        int colorsCount = product.getColorsCount();
        int normalizedValue = master.getTariff().getNormalizedValue();
        float sizePriceMultiplier = size.getPriceMultiplier();

        return MIN_COST + ((int) (imageDifficulty * normalizedValue * sizePriceMultiplier + (colorsCount - 1) * COLORS_PRICE_MULTIPLIER));
    }

}
