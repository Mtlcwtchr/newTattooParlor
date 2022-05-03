package by.bsuir.tattooparlor.util.exception;

public class NoClientPresentedException extends UtilException {
    public NoClientPresentedException() {
    }

    public NoClientPresentedException(String message) {
        super(message);
    }

    public NoClientPresentedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoClientPresentedException(Throwable cause) {
        super(cause);
    }
}
