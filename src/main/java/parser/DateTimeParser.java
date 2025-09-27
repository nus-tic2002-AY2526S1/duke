package parser;

import com.zoho.hawking.HawkingTimeParser;
import com.zoho.hawking.datetimeparser.configuration.HawkingConfiguration;
import com.zoho.hawking.language.english.model.DatesFound;
import exception.InvalidDateTimeException;
import exception.InvalidDateTimeException.ErrorType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;


/**
 * Utility class for parsing various date and time String formats into LocalDateTime objects.
 */
public final class DateTimeParser {

    private DateTimeParser() {}

    /**
     * Enum representing supported date/time input patterns.
     * Reference on how to nest enum in another class:
     * @see <a href="https://www.tutorialspoint.com/can-we-define-an-enum-inside-a-class-in-java">...</a>
     */
    private enum DateTimePattern {
        // Date with 24-hour time patterns
        SLASH_DMYHM("d/M/yyyy HHmm", true),     // 9/9/2025 2210
        DASH_DMYHM("d-M-yyyy HHmm", true),      // 9-9-2025 2210
        LONG_DMYHM("d MMM yyyy HHmm", true),    // 9 Sep 2025 2210
        ISO_8601("yyyy-MM-dd'T'HH:mm", true),   // 2025-09-09T22:10

        // Date only patterns
        SLASH_DMY("d/M/yyyy", false),           // 9/9/2025
        DASH_DMY("d-M-yyyy", false),            // 9-9-2025
        LONG_DMY("d MMM yyyy", false),          // 9 Sep 2025
        ISO_DATE("yyyy-MM-dd", false);          // 2025-09-09

        private final DateTimeFormatter formatter;
        private final boolean hasTime;

        DateTimePattern(String pattern, boolean hasTime) {
            this.formatter = DateTimeFormatter.ofPattern(pattern);
            this.hasTime = hasTime;
        }

        public DateTimeFormatter getFormatter() {
            return formatter;
        }

        public boolean hasTime() {
            return hasTime;
        }
    }

    // Hawking parser + config reused across calls
    private static final HawkingTimeParser HAWKING = new HawkingTimeParser();
    private static final HawkingConfiguration CONFIG;

    static {
        CONFIG = new HawkingConfiguration();
        CONFIG.setTimeZone(ZoneId.systemDefault().toString());
    }

    /**
     * Parses a date/time string into a LocalDateTime object using a two-stage parsing approach.
     * <p><strong>Strict Pattern Matching:</strong><br>
     * Attempts to parse the input using predefined DateTimeFormatter patterns for exact format matches.
     * If time is omitted from the input, it defaults to 00:00 (midnight).
     *
     * <p><strong>Natural Language Processing:</strong><br>
     * If strict parsing fails, falls back to Hawking NLP parser which can handle:
     * <ul>
     *   <li>Relative expressions: "tomorrow", "next week", "in 2 hours"</li>
     *   <li>Natural language: "next Monday at 3pm", "last Friday morning"</li>
     *   <li>Contextual parsing based on current date/time as reference</li>
     * </ul>
     * <p>The Hawking parser uses the system's default timezone and current timestamp as the
     * reference point for relative date calculations.
     *
     * @param dateTimeString the date/time string to parse
     * @return ParsedDateTime object containing the parsed LocalDateTime and time metadata
     * @throws InvalidDateTimeException if input does not match any supported format (strict/NLP)
     *                                  or if input is not valid date time value
     * @see <a href="https://github.com/zoho/hawking">Hawking NLP Date Parser</a>
     */
    public static ParsedDateTime parse(String dateTimeString)
            throws InvalidDateTimeException {

        // Try strict format first
        for (DateTimePattern p : DateTimePattern.values()) {
            try {
                if (p.hasTime()) {
                    LocalDateTime dt = LocalDateTime.parse(dateTimeString, p.getFormatter());
                    return new ParsedDateTime(dt, true);
                } else {
                    LocalDate date = LocalDate.parse(dateTimeString, p.getFormatter());
                    // Defaults to 00:00 if time is omitted
                    return new ParsedDateTime(date.atStartOfDay(), false);
                }
            } catch (DateTimeParseException e) {
                if (isInvalidDateTimeValue(e)) {
                    throw new InvalidDateTimeException(
                            ErrorType.INVALID_DATETIME_VALUE,
                            dateTimeString,
                            e
                    );
                }   // Otherwise, continue to next pattern
            }
        }
        // Hawking NLP fallback
        try {
            Date referenceDate = new Date();
            DatesFound found = HAWKING.parse(dateTimeString, referenceDate, CONFIG, "eng");

            if (found != null && found.getParserOutputs() != null && !found.getParserOutputs().isEmpty()) {
                var output = found.getParserOutputs().get(0);
                if (output.getDateRange() != null && output.getDateRange().getEnd() != null) {
                    Date date = output.getDateRange().getEnd().toDate();
                    LocalDateTime dt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
                    boolean hasTime = Boolean.TRUE.equals(output.getIsExactTimePresent());
                    return new ParsedDateTime(dt, hasTime);
                }
            }
        } catch (Exception ignored) {
        }
        // All formats fail
        throw new InvalidDateTimeException(ErrorType.UNSUPPORTED_FORMAT, dateTimeString);
    }


    /**
     * Determines if a DateTimeParseException is caused by invalid date-time value,
     * e.g. day out of range, hour out of range.
     */
    private static boolean isInvalidDateTimeValue(DateTimeParseException e) {
        if (e.getMessage() == null) return false;
        String msg = e.getMessage();
        return msg.contains("MonthOfYear") || msg.contains("DayOfMonth") ||
                msg.contains("HourOfDay") || msg.contains("MinuteOfHour");
    }
}
