package logic.parser.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import common.exception.InvalidDateTimeException;
import common.exception.InvalidDateTimeException.ErrorType;

/**
 * Strict pattern-matching strategy for parsing date-time strings.
 * <p>
 * This strategy uses predefined {@link DateTimeFormatter} patterns to parse inputs
 * that exactly match supported formats. It supports various date notations (slash,
 * dash, long month names, ISO 8601) with or without time components.
 * <p>
 * When time is omitted from the input, it defaults to 23:59.
 * All times are interpreted as 24-hour format.
 *
 * @see NlpDateTimeStrategy
 */
public final class StrictDateTimeStrategy implements DateTimeStrategy {

    /**
     * Determines if a {@link DateTimeParseException} was caused by invalid date-time values
     * rather than format mismatch.
     * <p>
     * Invalid values include out-of-range months, days, hours, or minutes
     * (e.g., month 13, day 32, hour 25, minute 60).
     */
    private static boolean isInvalidDateTimeValue(DateTimeParseException e) {
        if (e.getMessage() == null) return false;
        String msg = e.getMessage();
        return msg.contains("MonthOfYear") || msg.contains("DayOfMonth") ||
                msg.contains("HourOfDay") || msg.contains("MinuteOfHour");
    }

    /**
     * Parses a date/time string using strict date/time pattern matching.
     * <p>
     * Attempts to parse the input using predefined {@link DateTimePattern} for exact format matches.
     * If time is omitted from the input, it defaults to 23:59.
     *
     * @param dateTimeString the date/time string to parse
     * @return {@link ParsedDateTime} object if the input matches a supported pattern,
     *         or {@code null} if no pattern matches (allowing NLP strategy to try)
     * @throws InvalidDateTimeException if the input matches a pattern but contains
     *                                  invalid date-time values (e.g., February 30,
     *                                  hour 25, minute 60)
     */
    @Override
    public ParsedDateTime parse(String dateTimeString) throws InvalidDateTimeException {
        for (DateTimePattern pattern : DateTimePattern.values()) {
            ParsedDateTime result = tryParse(dateTimeString, pattern);
            if (result != null) {
                return result;
            }
        }
        return null; // let another strategy handle it
    }

    private ParsedDateTime tryParse(String dateTimeString, DateTimePattern pattern)
            throws InvalidDateTimeException {
        try {
            if (pattern.hasTime()) {
                LocalDateTime dt = LocalDateTime.parse(dateTimeString, pattern.getFormatter());
                return new ParsedDateTime(dt, true);
            } else {
                LocalDate date = LocalDate.parse(dateTimeString, pattern.getFormatter());
                return new ParsedDateTime(date.atTime(23, 59, 59), false);
            }
        } catch (DateTimeParseException e) {
            if (isInvalidDateTimeValue(e)) {
                throw new InvalidDateTimeException(
                        ErrorType.INVALID_DATETIME_VALUE,
                        dateTimeString,
                        e
                );
            }
            return null; // Otherwise, continue to next pattern
        }
    }

    /**
     * Enum representing supported date/time input patterns. Each pattern specifies a
     * {@link DateTimeFormatter} and whether it includes time information.
     * Patterns are tried in enum declaration order during parsing.
     * <p>
     * Reference: <em><a href="https://www.tutorialspoint.com/can-we-define-an-enum-inside-a-class-in-java">
     * Tutorials Points: Can we define an enum inside a class in Java</a></em>
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
}
