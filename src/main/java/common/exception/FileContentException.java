package common.exception;

import common.ErrorMessage;

/**
 * Exception thrown when file content is invalid, malformed, or cannot be processed.
 * This exception is typically thrown during file loading operations when:
 * <li>JSON format is invalid or malformed</li>
 * <li>Required fields are missing from the file content</li>
 * <li>Input contains unknown task types or invalid content</li>
 */
public class FileContentException extends MeeBotException {
    private final ErrorType type;

    public FileContentException(ErrorType type) {
        super(type.getContext());
        this.type = type;
    }

    public ErrorType getType() {
        return type;
    }

    @Override
    public ErrorMessage toErrorMessage() {
        return new ErrorMessage(ErrorMessage.INVALID_JSON_FORMAT);
    }

    public enum ErrorType {
        INVALID_JSON_FORMAT("File structure is invalid, loading failed."),
        INVALID_INPUT("Unknown task type or invalid content.");

        private final String context;

        ErrorType(String context) {
            this.context = context;
        }

        public String getContext() {
            return context;
        }
    }
}
