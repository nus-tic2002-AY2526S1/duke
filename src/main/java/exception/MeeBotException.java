package exception;

import common.ErrorMessage;

/**
 * Abstract base class for all MeeBot-specific exceptions.
 * Provides a consistent exception hierarchy and standardizes error message handling.
 *
 * <p>All MeeBot exceptions must extend this class and implement the
 * {@link #toErrorMessage()} method to provide user-friendly error messages
 * that can be displayed in the user interface.</p>
 *
 * <p>This class extends {@link Exception}, making it a checked exception that
 * must be explicitly handled or declared in method signatures.</p>
 *
 * @see ErrorMessage
 */
public abstract class MeeBotException extends Exception {
    public MeeBotException(String message) {
        super(message);
    }

    public MeeBotException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract ErrorMessage toErrorMessage();
}
