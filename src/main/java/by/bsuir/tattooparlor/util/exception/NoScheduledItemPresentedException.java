package by.bsuir.tattooparlor.util.exception;

public class NoScheduledItemPresentedException extends UtilException {
    public NoScheduledItemPresentedException() {
    }

    public NoScheduledItemPresentedException(String message) {
        super(message);
    }

    public NoScheduledItemPresentedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoScheduledItemPresentedException(Throwable cause) {
        super(cause);
    }
}
