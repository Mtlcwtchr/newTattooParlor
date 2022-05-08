package by.bsuir.tattooparlor.util.calculator;

import java.awt.*;
import java.util.Arrays;

public class Pixel {

    private static final double colorHueDelta = 15;

    private final Color color;

    public Pixel(int argb) {
        color = new Color(argb);
    }

    public int getRgb() {
        return color.getRGB();
    }

    public float[] getHSV() {
        return Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
    }

    public int getHue() {
        return (int) (getHSV()[0] * 360);
    }

    public int getSaturation() {
        return (int) (getHSV()[1] * 100);
    }

    public int getBrightness() {
        return (int) (getHSV()[2] * 100);
    }

    public boolean isSimpleEquals(Pixel pixel) {
        return SimpleColor.getSimpleColor(this).equals(SimpleColor.getSimpleColor(pixel));
    }

    public boolean isBrightnessEquals(Pixel pixel) {
        return Math.abs(getBrightness() - pixel.getBrightness()) < 10;
    }

    @Override
    public String toString() {
        return "Pixel{" +
                "hex=" + Integer.toHexString(color.getRGB()) + ", " +
                "hsv=" + Arrays.toString(getHSV()) +
                '}';
    }
}
