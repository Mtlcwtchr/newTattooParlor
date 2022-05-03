package by.bsuir.tattooparlor.util.exception;

public class IllegalPasswordException extends IllegalCredentialsException {
    public IllegalPasswordException() {
    }

    public IllegalPasswordException(String message) {
        super(message);
    }

    public IllegalPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalPasswordException(Throwable cause) {
        super(cause);
    }
}
