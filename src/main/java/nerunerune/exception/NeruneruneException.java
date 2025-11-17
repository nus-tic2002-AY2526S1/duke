package nerunerune.exception;

/**
 * Simple custom exception class for errors specific to Nerunerune application.
 * Used to indicate issues encountered during program execution.
 */
public class NeruneruneException extends Exception {
    /**
     * Constructs a NeruneruneException with the specified error message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public NeruneruneException(String message) {
        super(message);
    }
}
