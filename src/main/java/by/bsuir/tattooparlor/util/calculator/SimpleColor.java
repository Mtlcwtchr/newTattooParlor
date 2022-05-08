package by.bsuir.tattooparlor.util.calculator;

public enum SimpleColor {
    UNDEFINED,
    BLACK,
    WHITE,
    GRAY,
    RED,
    ORANGE,
    YELLOW,
    CHARTREUSE_GREEN,
    GREEN,
    SPRING_GREEN,
    CYAN,
    AZURE,
    BLUE,
    VIOLET,
    MAGENTA,
    ROSE;

    @Override
    public String toString() {
        return this.name();
    }

    public static SimpleColor getSimpleColor(Pixel pixel) {
        int sat = pixel.getSaturation();
        int bg = pixel.getBrightness();

        if (bg <= 32) {
            return SimpleColor.BLACK;
        }

        if (sat <= 6) {
            return bg >= 66 ? SimpleColor.WHITE : SimpleColor.GRAY;
        }

        int hue = pixel.getHue();
        int simpleVal = hue / 30;
        return switch (simpleVal) {
            case 0, 12 -> SimpleColor.RED;
            case 1 -> SimpleColor.ORANGE;
            case 2 -> SimpleColor.YELLOW;
            case 3 -> SimpleColor.CHARTREUSE_GREEN;
            case 4 -> SimpleColor.GREEN;
            case 5 -> SimpleColor.SPRING_GREEN;
            case 6 -> SimpleColor.CYAN;
            case 7 -> SimpleColor.AZURE;
            case 8 -> SimpleColor.BLUE;
            case 9 -> SimpleColor.VIOLET;
            case 10 -> SimpleColor.MAGENTA;
            case 11 -> SimpleColor.ROSE;
            default -> SimpleColor.UNDEFINED;
        };
    }
}
