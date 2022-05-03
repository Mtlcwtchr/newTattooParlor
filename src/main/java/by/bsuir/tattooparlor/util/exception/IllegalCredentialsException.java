package by.bsuir.tattooparlor.util.exception;

public class IllegalCredentialsException extends UtilException {
    public IllegalCredentialsException() {
    }

    public IllegalCredentialsException(String message) {
        super(message);
    }

    public IllegalCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalCredentialsException(Throwable cause) {
        super(cause);
    }
}
