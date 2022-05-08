package by.bsuir.tattooparlor.util.calculator;

import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Component
public class ImageProcessor implements IImageProcessor {

    public int getImageDifficulty(BufferedImage image) {
        int difficulty = 0;
        Pixel[][] pixels = getPixels(image);
        for (int i = 1; i < pixels.length - 1; ++i) {
            for (int j = 1; j < pixels[i].length - 1; ++j) {
                difficulty += getDifferentNeighborsCount(pixels, i, j);
            }
        }

        return border(difficulty);
    }

    private int border(int val) {
        int value = (int) (((float) val / (float) 500000) * 100);
        return Math.min(value, 100);
    }

    private int getDifferentNeighborsCount(Pixel[][] pixels, int x, int y) {
        Pixel pivot = pixels[x][y];
        int result = 0;
        for(int i = x-1; i < x+1; ++i) {
            for(int j = y-1; j < y+1; ++j) {
                Pixel neighbor = pixels[i][j];
                if (!pivot.isBrightnessEquals(neighbor)) {
                    ++result;
                }
            }
        }
        return result;
    }

    public List<Pixel> getSimpleColors(BufferedImage image) {
        return getSimpleColors(getPixels(ImagePixelator.pixelate(image, 48)));
    }

    private Pixel[][] getPixels(BufferedImage image) {
        int height = image.getHeight();
        int width = image.getWidth();
        Pixel[][] matrix = new Pixel[height][width];
        for(int i=0; i<height; ++i) {
            for(int j=0; j<width; ++j) {
                matrix[i][j] = new Pixel(image.getRGB(j, i));
            }
        }
        return matrix;
    }

    private List<Pixel> getSimpleColors(Pixel[][] matrix) {
        List<Pixel> simpleColors = new ArrayList<>();
        for (Pixel[] line : matrix) {
            for (Pixel pixel : line) {
                if (simpleColors.stream().noneMatch(pixel::isSimpleEquals)) {
                    simpleColors.add(pixel);
                }
            }
        }

        return simpleColors;
    }

}
