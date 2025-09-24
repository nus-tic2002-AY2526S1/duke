package exception;

import common.ErrorMessage;

/**
 * Custom exception for task format validation failures in MeeBot.
 * Provides detailed categorization of task formatting errors to enable
 * precise error reporting and user guidance.
 *
 * @see MeeBotException
 * @see ErrorMessage
 */
public class InvalidTaskFormatException extends MeeBotException {

    /**
     * Enumeration of task format parsing error types.
     * Each type represents a distinct category of parsing failure.
     */
    public enum ErrorType {
        DEADLINE("""
                Deadline needs a description and '/by' date.
                Try: deadline submit report /by 1/11/2025
                Or : deadline submit report /by 1/11/2025 2359
                """),

        EVENT("""            
                Event needs a description, '/from' start and '/to' end.
                Try: event meeting /from 1/11/2025 /to 1/11/2025
                Or : event meeting /from 1/11/2025 1400 /to 1/11/2025 1500
                """),

        SORT("""
                Try: sort /by date
                Or : sort /by status
                """),

        RECURRENCE("""
                Try: /repeat <daily|weekly|monthly|yearly> <number of times>
                """);

        private final String context;

        ErrorType(String context) {
            this.context = context;
        }

        public String getContext() {
            return context;
        }

        /**
         * Factory method to create an InvalidTaskFormatException of this error type.
         * This provides a convenient way to create exceptions without explicitly
         * passing the error type to the constructor.
         *
         * @return a new InvalidTaskFormatException configured with this error type
         */
        public InvalidTaskFormatException createException() {
            return new InvalidTaskFormatException(this);
        }
    }

    private final ErrorType type;

    public InvalidTaskFormatException(ErrorType type) {
        super(type.getContext());
        this.type = type;
    }

    /**
     * Converts exception to a user-friendly error message. The returned message is mapped
     * from the error type to an appropriate standardized error message.
     *
     * @return an {@link ErrorMessage} containing a formatted error for display to end users
     */
    @Override
    public ErrorMessage toErrorMessage() {
        return new ErrorMessage(String.format(
                ErrorMessage.TASK_FORMAT,
                type.getContext()
        ));
    }
}
