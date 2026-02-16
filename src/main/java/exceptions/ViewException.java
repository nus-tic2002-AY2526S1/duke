package exceptions;

/**
 * Thrown for any errors regarding view command
 */
public class ViewException extends GrootException {
    public ViewException(String message) {
        super(message);
    }

    /**
     * Thrown when date given is invalid
     */
    public static class InvalidViewDateException extends ViewException {
        public InvalidViewDateException() {
            super("Invalid date. Usage: view <dd/MM/yyyy>");
        }
    }

    /**
     * Thrown when date is not given
     */
    public static class MissingViewDateException extends ViewException {
        public MissingViewDateException() {
            super("Missing date. Usage: view <dd/MM/yyyy>");
        }
    }

    /**
     * Thrown when no tasks fit the date given
     */
    public static class NoTaskForViewException extends ViewException {
        public NoTaskForViewException() {
            super("No task available for given date.");
        }
    }
}
