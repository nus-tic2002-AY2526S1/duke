package exceptions;

/**
 * Thrown for all errors in program
 */
public class GrootException extends Exception {
    GrootException(String message) {
        super(message);
    }

    /**
     * Thrown when command given by user is not available
     */
    public static class InvalidCommandException extends GrootException {
        public InvalidCommandException(String message) {
            super("Invalid command: " + message);
        }
    }

    /**
     * Thrown when tasklist is empty
     */
    public static class EmptyListException extends GrootException {
        public EmptyListException() {
            super("There are no tasks yet.");
        }
    }
}




