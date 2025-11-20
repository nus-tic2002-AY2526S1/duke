package util;

/**
 * custom exception that can be thrown in the Duke application
 * This exception extends the {@link Exception} class and provides additional
 * functionality for handling and propagating custom exceptions
 */
public class DukeException extends Exception {

    /**
     * Constructs a new instance of {@code DukeException} with the specified error
     * message.
     * 
     * @param message the error message for this exception
     */
    public DukeException(String message) {
        super(message);
    }

    /**
     * Constructs a new instance of {@code DukeException} with the specified error
     * message and the cause of the exception.
     * 
     * @param message the error message for this exception
     * @param cause   the exception that caused this exception to be thrown
     */
    public DukeException(String message, Throwable cause) {
        super(message, cause);
    }
}
