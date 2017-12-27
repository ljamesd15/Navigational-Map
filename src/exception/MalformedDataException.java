package exception;

/**
 * A checked exception class for bad data files
 */
@SuppressWarnings("serial")
public class MalformedDataException extends Exception {
    public MalformedDataException() { }

    public MalformedDataException(String message) {
        super(message);
    }

    public MalformedDataException(Throwable cause) {
        super(cause);
    }

    public MalformedDataException(String message, Throwable cause) {
        super(message, cause);
    }
}