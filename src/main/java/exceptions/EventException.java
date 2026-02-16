package exceptions;

/**
 * Thrown for any errors regarding event command
 */
public class EventException extends TaskException {
    /**
     * Main message for all the sub EventExceptions
     *
     * @param message message from sub EventExceptions
     */
    public EventException(String message) {
        super(message + " Usage: event <task name> /from <dd/MM/yyyy> <HH:mm (24-hour)> " +
                "/to <dd/MM/yyyy> <HH:mm (24-hour)>");
    }

    /**
     * Thrown when /from and /to keywords are missing in user's input
     */
    public static class MissingEventKeywordsException extends EventException {
        public MissingEventKeywordsException() {
            super("Missing /from and /to keywords in event command.");
        }
    }

    /**
     * Thrown when /from is missing in user's input
     */
    public static class MissingEventFromKeywordException extends EventException {
        public MissingEventFromKeywordException() {
            super("Missing /from keyword in event command.");
        }
    }

    /**
     * Thrown when /to is missing in user's input
     */
    public static class MissingEventToKeywordException extends EventException {
        public MissingEventToKeywordException() {
            super("Missing /to keyword in event command.");
        }
    }

    /**
     * Thrown when task name is missing in user's input
     */
    public static class MissingEventTaskNameException extends EventException {
        public MissingEventTaskNameException() {
            super("Missing task name in event command.");
        }
    }

    /**
     * Thrown when date and time for form section is missing
     */
    public static class MissingEventFromException extends EventException {
        public MissingEventFromException() {
            super("Missing from-date and time in event command.");
        }
    }

    /**
     * Thrown when date and time for to section is missing
     */
    public static class MissingEventToException extends EventException {
        public MissingEventToException() {
            super("Missing to-date and time in event command.");
        }
    }

    /**
     * Thrown when date and time given by user is not a valid date and/or time
     */
    public static class InvalidEventDateTimeException extends EventException {
        public InvalidEventDateTimeException() {
            super("Invalid date/time.");
        }
    }
}

