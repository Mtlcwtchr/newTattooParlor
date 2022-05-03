package by.bsuir.tattooparlor.util.exception;

public class NoProductPresentedException extends UtilException {
    public NoProductPresentedException() {
    }

    public NoProductPresentedException(String message) {
        super(message);
    }

    public NoProductPresentedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoProductPresentedException(Throwable cause) {
        super(cause);
    }
}
