package exceptions;

public class UpdateException extends GrootException {
    public UpdateException(String message) {
        super(message);
    }

    public static class InvalidTaskNumberException extends UpdateException {
        public InvalidTaskNumberException() {
            super("Invalid task number." + " Usage: update <task number>, <update-field>: <update-info>");
        }
    }

    public static class MissingTaskNumberException extends UpdateException {
        public MissingTaskNumberException() {
            super("Missing task number." + " Usage: update <task number>, <update-field>: <update-info>");
        }
    }

    /**
     * Thrown when task is not in tasklist
     */
    public static class TaskNotFoundException extends UpdateException {
        public TaskNotFoundException() {
            super("Task not found in task list.");
        }
    }

    public static class InvalidUpdateTodoFieldException extends UpdateException {
        public InvalidUpdateTodoFieldException() {
            super("Invalid update field for todo task." + " Usage: update <task number>, taskName: <update-info>");
        }
    }

    public static class InvalidUpdateTodoInfoException extends UpdateException {
        public InvalidUpdateTodoInfoException() {
            super("Invalid update info for todo task." + " Usage: update <task number>, taskName: <update-info>");
        }
    }

    public static class InvalidUpdateFormatException extends UpdateException {
        public InvalidUpdateFormatException() {
            super("Invalid update format." + " Usage: update <task number>, <update-field>: <update-info>");
        }
    }

    public static class InvalidUpdateDeadlineFieldException extends UpdateException {
        public InvalidUpdateDeadlineFieldException() {
            super("Invalid update field for deadline task." + " Usage: update <task number>, taskName: <update-info>," +
                    " by: <dd/MM/yyyy> <HH:mm (24-hour)>");
        }
    }

    public static class InvalidUpdateDeadlineInfoException extends UpdateException {
        public InvalidUpdateDeadlineInfoException() {
            super("Invalid update info for deadline task." + " Usage: update <task number>, taskName: <update-info>," +
                    " by: <dd/MM/yyyy> <HH:mm (24-hour)>");
        }
    }

    public static class InvalidUpdateDeadlineDateException extends UpdateException {
        public InvalidUpdateDeadlineDateException() {
            super("Invalid date for deadline task. Usage: update <task number>, taskName: <update-info>, by: " +
                    "<dd/MM/yyyy> <HH:mm (24-hour)>");
        }
    }

    public static class InvalidUpdateEventFieldException extends UpdateException {
        public InvalidUpdateEventFieldException() {
            super("Invalid update field for event task." + " Usage: update <task number>, taskName: <update-info>, " +
                    "start: <dd/MM/yyyy> <HH:mm (24-hour)>, end: <dd/MM/yyyy> <HH:mm (24-hour)>");
        }
    }

    public static class InvalidUpdateEventInfoException extends UpdateException {
        public InvalidUpdateEventInfoException() {
            super("Invalid update info for event task." + " Usage: update <task number>, taskName: <update-info>, " +
                    "start: <dd/MM/yyyy> <HH:mm (24-hour)>, end: <dd/MM/yyyy> <HH:mm (24-hour)>");
        }
    }

    public static class InvalidUpdateEventDateException extends UpdateException {
        public InvalidUpdateEventDateException() {
            super("Invalid date for event task. Usage: update <task number>, taskName: <update-info>, start: " +
                    "<dd/MM/yyyy> <HH:mm (24-hour)>, end: <dd/MM/yyyy> <HH:mm (24-hour)>");
        }
    }
}
