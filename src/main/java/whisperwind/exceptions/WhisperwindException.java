package whisperwind.exceptions;

/**
 * Base exception class for all Whisperwind application exceptions.
 */
public class WhisperwindException extends Exception {
    public WhisperwindException(String message) {
        super(message);
    }

    public WhisperwindException(String message, Throwable cause) {
        super(message, cause);
    }
}
