package by.bsuir.tattooparlor.util.exception;

public class UserPresentedException extends UtilException {
    public UserPresentedException() {
    }

    public UserPresentedException(String message) {
        super(message);
    }

    public UserPresentedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserPresentedException(Throwable cause) {
        super(cause);
    }
}
