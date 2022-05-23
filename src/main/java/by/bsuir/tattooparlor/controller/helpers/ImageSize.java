package by.bsuir.tattooparlor.controller.helpers;

public enum ImageSize {
    TEN(1.0f),
    TWENTY(2.0f),
    THIRTY(3.0f),
    FORTY(3.5f),
    FIFTY(4.0f),
    SIXTY(4.5f);

    final float priceMultiplier;

    ImageSize(float priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    public float getPriceMultiplier() {
        return priceMultiplier;
    }

    public String getDescription() {
        return switch (this) {
            case TEN -> "10x10";
            case TWENTY -> "20x20";
            case THIRTY -> "30x30";
            case FORTY -> "40x40";
            case FIFTY -> "50x50";
            case SIXTY -> "60x60";
        };
    }
}
