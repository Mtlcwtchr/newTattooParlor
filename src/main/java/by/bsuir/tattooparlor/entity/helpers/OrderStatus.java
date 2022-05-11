package by.bsuir.tattooparlor.entity.helpers;

public enum OrderStatus {
    REQUESTED("Ожидает подтверждения"),
    ACCEPTED("Подтвержден"),
    COMPLETED("Выполнен"),
    REJECTED("Отклонен");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
