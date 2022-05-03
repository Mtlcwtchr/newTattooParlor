package by.bsuir.tattooparlor.util.exception;

public class IllegalLoginException extends IllegalCredentialsException {
    public IllegalLoginException() {
    }

    public IllegalLoginException(String message) {
        super(message);
    }

    public IllegalLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalLoginException(Throwable cause) {
        super(cause);
    }
}
