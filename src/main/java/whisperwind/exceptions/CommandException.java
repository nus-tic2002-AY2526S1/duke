package whisperwind.exceptions;

/**
 * Exception thrown for command-related errors.
 */
public class CommandException extends WhisperwindException {
    public CommandException(String message) {
        super(message);
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }
}