package exceptions;

/**
 * Thrown for any error regarding todo command
 */
public class TodoException extends TaskException {
    public TodoException(String message) {
        super(message + " Usage: todo <task name>");
    }

    /**
     * Thrown when task name is missing
     */
    public static class MissingTodoTaskNameException extends TodoException {
        public MissingTodoTaskNameException() {
            super("Missing task name in todo command.");
        }
    }
}

