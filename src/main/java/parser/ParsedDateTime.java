package parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Wrapper class for LocalDateTime with time metadata.
 * When time is omitted from the input, it defaults to 00:00 (midnight).
 */
public record ParsedDateTime(LocalDateTime dateTime, boolean hasTime) {
    private static final DateTimeFormatter DISPLAY_DATETIME =
            DateTimeFormatter.ofPattern("dd MMM yyyy, h:mm a");
    private static final DateTimeFormatter DISPLAY_DATE =
            DateTimeFormatter.ofPattern("dd MMM yyyy");

    /**
     * Formats a LocalDateTime to display String format.
     *
     * @param dateTime the LocalDateTime to format
     * @param hasTime  whether to include time in the output
     * @return formatted string in "dd MMM yyyy, h:mm a" or "dd MMM yyyy" format
     */
    public static String format(LocalDateTime dateTime, boolean hasTime) {
        return hasTime ? dateTime.format(DISPLAY_DATETIME) : dateTime.format(DISPLAY_DATE);
    }
}
