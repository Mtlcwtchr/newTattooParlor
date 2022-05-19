package by.bsuir.tattooparlor.util.exception;

public class NoOrderPresentedException extends UtilException {
    public NoOrderPresentedException() {
    }

    public NoOrderPresentedException(String message) {
        super(message);
    }

    public NoOrderPresentedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoOrderPresentedException(Throwable cause) {
        super(cause);
    }
}
