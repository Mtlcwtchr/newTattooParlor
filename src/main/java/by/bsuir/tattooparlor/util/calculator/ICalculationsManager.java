package by.bsuir.tattooparlor.util.calculator;

import by.bsuir.tattooparlor.controller.helpers.ImageSize;
import by.bsuir.tattooparlor.entity.Product;
import by.bsuir.tattooparlor.entity.TattooMaster;

import java.awt.image.BufferedImage;

public interface ICalculationsManager {
    int getColorsCount(BufferedImage image);
    int getDifficulty(BufferedImage image);
    int getTotalCost(BufferedImage image, int colorsCount, TattooMaster master, ImageSize size);
    int getTotalCost(Product product, TattooMaster master, ImageSize size);
}
