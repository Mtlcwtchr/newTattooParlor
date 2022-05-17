package by.bsuir.tattooparlor.entity.helpers;

public enum UserRole {
    CLIENT("Клиент"),
    MODERATOR("Модератор"),
    ADMIN("Админ");

    private String description;

    UserRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPrior(UserRole other) {
        return switch (this) {
            case CLIENT -> false;
            case MODERATOR -> other == CLIENT;
            case ADMIN -> other != ADMIN;
        };
    }
}
