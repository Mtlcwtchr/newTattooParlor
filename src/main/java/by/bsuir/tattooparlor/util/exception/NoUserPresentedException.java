package by.bsuir.tattooparlor.util.exception;

public class NoUserPresentedException extends UtilException {
    public NoUserPresentedException() {
    }

    public NoUserPresentedException(String message) {
        super(message);
    }

    public NoUserPresentedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoUserPresentedException(Throwable cause) {
        super(cause);
    }
}
