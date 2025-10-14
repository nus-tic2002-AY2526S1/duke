package whisperwind.exceptions;

/**
 * Exception thrown for task-related errors.
 */
public class TaskException extends WhisperwindException {
    public TaskException(String message) {
        super(message);
    }

    public TaskException(String message, Throwable cause) {
        super(message, cause);
    }
}