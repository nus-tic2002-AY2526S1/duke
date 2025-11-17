package nerunerune.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import nerunerune.exception.NeruneruneException;

/**
 * Provides utility methods to parse and format date-time strings
 * used in the Nerunerune application.
 * Handles conversion between user input, storage format,
 * and display format for date and time.
 */
public class DateTimeParser {

    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HHmm");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy h:mm a");
    private static final DateTimeFormatter STORAGE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");
    private static final DateTimeFormatter DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter LONG_DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy");

    /**
     * Parses a date-time string from user input.
     * Accepts strings either with just a date (dd-MM-yyyy) or with date and time (dd-MM-yyyy HHmm).
     * Returns a LocalDateTime object with time set to start of day if only date is given (dd-MM-yyyy 0000).
     *
     * @param dateTimeString the input date-time string
     * @return LocalDateTime parsed from the input string
     * @throws NeruneruneException if input format is invalid or time is out of range
     */
    public static LocalDateTime parseDateTime(String dateTimeString) throws NeruneruneException {
        try {
            if (dateTimeString.length() <= 10) {
                LocalDate ld = LocalDate.parse(dateTimeString, DATE_ONLY_FORMATTER);
                return ld.atStartOfDay();
            } else {
                return LocalDateTime.parse(dateTimeString, INPUT_FORMATTER);
            }
        } catch (DateTimeParseException e) {
            throw new NeruneruneException("""
                    Invalid date and time detected.
                    
                    Requirements:
                    - Date must be within valid range
                    - Hours must be between 00 and 23
                    - Minutes must be between 00 and 59
                    - Time format must be 24-hour (HHmm)
                    
                    Example: 01-01-2025 1830""");

        }
    }

    /**
     * Parses a date-time string from the storage format.
     *
     * @param dateTimeString the stored date-time string
     * @return LocalDateTime parsed from the storage string
     * @throws NeruneruneException if the string is not in the correct storage format
     */
    public static LocalDateTime parseStorageDateTime(String dateTimeString) throws NeruneruneException {
        try {
            return LocalDateTime.parse(dateTimeString, STORAGE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new NeruneruneException("Invalid storage date/time format");
        }
    }

    /**
     * Formats a LocalDateTime object into the display format used in the UI.
     *
     * @param dateTime the LocalDateTime to format
     * @return formatted date-time string for display
     */
    public static String formatForDisplay(LocalDateTime dateTime) {
        return dateTime.format(OUTPUT_FORMATTER);
    }

    /**
     * Formats a LocalDateTime object into the storage format used for saving to file.
     *
     * @param dateTime the LocalDateTime to format
     * @return formatted date-time string for storage
     */
    public static String formatForStorage(LocalDateTime dateTime) {
        return dateTime.format(STORAGE_FORMATTER);
    }

    /**
     * Formats a LocalDate for display in schedule views.
     * Converts the date to a long, readable format (e.g., "Sunday, 26 Oct 2025").
     *
     * @param date the date to format
     * @return the formatted date string in long format (EEEE, dd MMM yyyy)
     */
    public static String formatForSchedule(LocalDate date) {
        return date.format(LONG_DATE_FORMATTER);
    }

    /**
     * Parses a date string from user input with keyword support.
     * Supports shortcuts "today", "tomorrow", "yesterday", "next week", "next month",
     * as well as dates in DD-MM-YYYY format.
     *
     * @param dateString the date string to parse (e.g., "today", "25-10-2025")
     * @return the parsed LocalDateTime at start of day
     * @throws NeruneruneException if the date format is invalid or cannot be parsed
     */
    public static LocalDateTime parseDateWithKeywords(String dateString) throws NeruneruneException {
        try {
            return switch (dateString.toLowerCase()) {
                case "today" -> LocalDate.now().atStartOfDay();
                case "tomorrow" -> LocalDate.now().plusDays(1).atStartOfDay();
                case "yesterday" -> LocalDate.now().minusDays(1).atStartOfDay();
                case "next week" -> LocalDate.now().plusWeeks(1).atStartOfDay();
                case "next month" -> LocalDate.now().plusMonths(1).atStartOfDay();
                default -> parseDateTime(dateString);
            };
        } catch (DateTimeParseException e) {
            throw new NeruneruneException("Invalid date format! Use DD-MM-YYYY or today/tomorrow/yesterday");
        }
    }
}
