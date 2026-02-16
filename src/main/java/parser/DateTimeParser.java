package parser;

import exceptions.DateTimeException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parsing of date and time
 */
public class DateTimeParser {
    /*
     * All accepted formats for parsing
     */
    private static final String[] acceptedDateTimeFormats = {
            // '-' separator
            // single digit days and months
            "d-M-yy HH:mm",
            "d-M-yy HHmm",
            "d-M-yyyy HH:mm",
            "d-M-yyyy HHmm",
            // single digit days
            "d-MM-yy HH:mm",
            "d-MM-yy HHmm",
            "d-MM-yyyy HH:mm",
            "d-MM-yyyy HHmm",
            // single digit months
            "dd-M-yy HH:mm",
            "dd-M-yy HHmm",
            "dd-M-yyyy HH:mm",
            "dd-M-yyyy HHmm",
            //standard
            "dd-MM-yy HH:mm",
            "dd-MM-yy HHmm",
            "dd-MM-yyyy HH:mm",
            "dd-MM-yyyy HHmm",

            // '/' separator
            // single digit days and months
            "d/M/yy HH:mm",
            "d/M/yy HHmm",
            "d/M/yyyy HH:mm",
            "d/M/yyyy HHmm",
            // single digit days
            "d/MM/yy HH:mm",
            "d/MM/yy HHmm",
            "d/MM/yyyy HH:mm",
            "d/MM/yyyy HHmm",
            // single digit months
            "dd/M/yy HH:mm",
            "dd/M/yy HHmm",
            "dd/M/yyyy HH:mm",
            "dd/M/yyyy HHmm",
            //standard
            "dd/MM/yy HH:mm",
            "dd/MM/yy HHmm",
            "dd/MM/yyyy HH:mm",
            "dd/MM/yyyy HHmm",
            // month in 3 letters
            "dd MMM yyyy HHmm", //used in file
    };
    private static final String[] acceptedDateFormats = {
            // '-' separator
            "d-M-yy",
            "d-M-yyyy",
            "d-MM-yy",
            "d-MM-yyyy",
            "dd-M-yy",
            "dd-M-yyyy",
            "dd-MM-yy",
            "dd-MM-yyyy",

            // '/' separator
            "d/M/yy",
            "d/M/yyyy",
            "d/MM/yy",
            "d/MM/yyyy",
            "dd/M/yy",
            "dd/M/yyyy",
            "dd/MM/yy",
            "dd/MM/yyyy",
    };

    /**
     * Convert into DateTime object from string
     *
     * @param date from file or user input
     * @return DateTime object
     * @throws DateTimeException if date given is not of accepted format
     */
    public static LocalDateTime parseDateTime(String date) throws DateTimeException {
        assert date != null;

        for (String format : acceptedDateTimeFormats) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

                return LocalDateTime.parse(date, formatter);
            } catch (DateTimeParseException ignored) {
            }
        }
        throw new DateTimeException();
    }

    /**
     * Convert into Date object
     * Used for viewing
     *
     * @param date user input
     * @return Date object
     * @throws DateTimeException if date is of not accepted format
     */
    public static LocalDate parseDate(String date) throws DateTimeException {
        assert date != null;

        for (String format : acceptedDateFormats) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

                return LocalDate.parse(date, formatter);
            } catch (DateTimeParseException ignored) { //ignore if exception thrown
            }
        }
        throw new DateTimeException();
    }

    /**
     * Convert back to String
     * Used to convert dates for printing
     *
     * @param dateTime date in task
     * @return dateTime in string
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        assert dateTime != null;

        return dateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy HHmm"));
    }
}
