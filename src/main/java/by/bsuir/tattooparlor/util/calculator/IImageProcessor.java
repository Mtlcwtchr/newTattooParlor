package by.bsuir.tattooparlor.util.calculator;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public interface IImageProcessor {
    int getImageDifficulty(BufferedImage image);
    List<Pixel> getSimpleColors(BufferedImage image);
}
