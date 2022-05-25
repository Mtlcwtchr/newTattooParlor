package by.bsuir.tattooparlor.entity.helpers;

public enum DiscountStatus {
    ACTIVE("Активна"),
    EXPIRED("Истекла");

    private final String description;

    DiscountStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
