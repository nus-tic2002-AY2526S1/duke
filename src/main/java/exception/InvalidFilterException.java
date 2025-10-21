package exception;

import common.ErrorMessage;

/**
 * Exception thrown when task filter operations encounter invalid input or configuration.
 * <p>This exception extends {@link MeeBotException} and categorizes filter parsing errors
 * into specific types by converting itself to appropriate {@link ErrorMessage} objects.</p>
 *
 * @see MeeBotException
 * @see ErrorMessage
 */
public class InvalidFilterException extends MeeBotException {

    private final ErrorType type;

    public InvalidFilterException(ErrorType type) {
        super(type.getContext());
        this.type = type;
    }

    public ErrorType getType() {
        return type;
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
                ErrorMessage.FILTER_FORMAT,
                type.getContext()
        ));
    }

    /**
     * Enumeration of possible filter error types with associated context messages.
     */
    public enum ErrorType {
        TOO_MANY_FILTERS("Too many criteria, maximum 3 - you think this is buffet?"),
        INVALID_FILTER_FORMAT("Wrong filter format."),
        UNKNOWN_FILTER_KEY("Unknown filter key.");

        private final String context;

        ErrorType(String context) {
            this.context = context;
        }

        public String getContext() {
            return context;
        }
    }
}
