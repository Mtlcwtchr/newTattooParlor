package by.bsuir.tattooparlor.util.exception;

public class NoDiscountPresentedException extends UtilException {
    public NoDiscountPresentedException() {
    }

    public NoDiscountPresentedException(String message) {
        super(message);
    }

    public NoDiscountPresentedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoDiscountPresentedException(Throwable cause) {
        super(cause);
    }
}
