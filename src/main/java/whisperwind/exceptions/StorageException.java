package whisperwind.exceptions;

/**
 * Exception thrown for storage-related errors.
 */
public class StorageException extends WhisperwindException {
    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}