package exceptions;

/**
 * Thrown for all errors regarding the different tasks
 */
public class TaskException extends GrootException {
    public TaskException(String message) {
        super(message);
    }
}