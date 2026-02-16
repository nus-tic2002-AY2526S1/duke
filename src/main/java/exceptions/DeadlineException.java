package exceptions;

/**
 * Thrown for any errors regarding deadline command
 */
public class DeadlineException extends TaskException {
    /**
     * Main message for all the sub DeadlineExceptions
     *
     * @param message message from sub DeadlineExceptions
     */
    public DeadlineException(String message) {
        super(message + " Usage: deadline <task name> /by <dd/MM/yyyy> <HH:mm (24-hour)>");
    }

    /**
     * Thrown when the date and time required is not in user's input
     */
    public static class MissingDeadlineByException extends DeadlineException {
        public MissingDeadlineByException() {
            super("Missing by-date and time in deadline command.");
        }
    }

    /**
     * Thrown when /by is missing in user's input
     */
    public static class MissingDeadlineByKeywordException extends DeadlineException {
        public MissingDeadlineByKeywordException() {
            super("Missing /by keyword in deadline command.");
        }
    }

    /**
     * Thrown when task name is missing in user's input
     */
    public static class MissingDeadlineTaskNameException extends DeadlineException {
        public MissingDeadlineTaskNameException() {
            super("Missing task name in deadline command.");
        }
    }

    /**
     * Thrown when date and time given by user is not a valid date and/or time
     */
    public static class InvalidDeadlineDateTimeException extends DeadlineException {
        public InvalidDeadlineDateTimeException() {
            super("Invalid date/time.");
        }
    }
}





