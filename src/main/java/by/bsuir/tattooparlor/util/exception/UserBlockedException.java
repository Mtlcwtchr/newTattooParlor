package by.bsuir.tattooparlor.util.exception;

public class UserBlockedException extends UtilException {
    public UserBlockedException() {
    }

    public UserBlockedException(String message) {
        super(message);
    }

    public UserBlockedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserBlockedException(Throwable cause) {
        super(cause);
    }
}
