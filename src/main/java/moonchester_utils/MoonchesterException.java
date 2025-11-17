package moonchester_utils;

public class MoonchesterException extends RuntimeException {
    /**
     * Creates a new MoonchesterException with the specified detail message
     *
     * @param exceptionMessage The detail message for this exception
     */
    public MoonchesterException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
