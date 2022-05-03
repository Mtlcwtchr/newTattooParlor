package by.bsuir.tattooparlor.util.exception;

public class NoMasterPresentedException extends UtilException {
    public NoMasterPresentedException() {
    }

    public NoMasterPresentedException(String message) {
        super(message);
    }

    public NoMasterPresentedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoMasterPresentedException(Throwable cause) {
        super(cause);
    }
}
