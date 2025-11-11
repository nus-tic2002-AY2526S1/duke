package nono.exception;

/**
 * Represents exceptions that occur when the user enters invalid input.
 */
public class UserInputException extends Exception {
    /**
     * Constructs a UserInputException with the specified message.
     *
     * @param message The error message.
     */
    public UserInputException(String message) {
        super(message);
    }
}
