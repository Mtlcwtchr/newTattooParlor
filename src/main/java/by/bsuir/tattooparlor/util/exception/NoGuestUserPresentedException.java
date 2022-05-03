package by.bsuir.tattooparlor.util.exception;

public class NoGuestUserPresentedException extends UtilException {
    public NoGuestUserPresentedException() {
    }

    public NoGuestUserPresentedException(String message) {
        super(message);
    }

    public NoGuestUserPresentedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoGuestUserPresentedException(Throwable cause) {
        super(cause);
    }
}
