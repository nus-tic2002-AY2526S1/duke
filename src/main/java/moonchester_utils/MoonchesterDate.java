package moonchester_utils;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;

public class MoonchesterDate {

/**
 * Converts a user-provided date/time string into a LocalDateTime object.
 *
 * This method supports the following types of inputs:
 * 1. Full date and time in the format "dd/MM/yyyy HHmm"
 * 2. Day shortcut (e.g., "mon", "monday"), (optional) followed by a 24-hour time (e.g., "mon 1200")
 *
 * If a day is provided, the resulting LocalDateTime will correspond to the following date of the day (e.g. Monday -> next monday). If no time is 
 * provided,it will set to 00:00 (midnight)
 * If the input cannot be parsed, the method prints an error message and returns null
 *
 * @param dateTimeString The date/time string entered by the user
 * @param option An integer indicating whether time is explicitly provided:
 *               1 - time is not provided; default to 00:00
 *               0 - time is given
 *               other - time is included in the input
 * @return A LocalDateTime representing the parsed date and time, or null if parsing fails
 */

public static LocalDateTime convertToDateTime(String dateTimeString, int option) {
    try {
        // Better to normalise input
        String input = dateTimeString.trim().toLowerCase();
        String[] parts = input.split("\\s+");
        String dayPart = parts[0];
        String timePart;
        if (parts.length == 1) {
            timePart = "0000";
        }
        else {
            timePart = parts[1];
        }

        DayOfWeek dayOfWeek = parseDayOfWeek(dayPart);
        if (dayOfWeek != null) {
            LocalDate nextDate = LocalDate.now().with(TemporalAdjusters.next(dayOfWeek));
            
            // Parse the time
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");
            LocalTime time = LocalTime.parse(timePart, timeFormatter);
            
            return LocalDateTime.of(nextDate, time);
        }

        // If dd/MM/yyyy HHmm format is used
        if (option == 1) {
            dateTimeString = dateTimeString + " 0000";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");
        return LocalDateTime.parse(dateTimeString, formatter);

    } catch (DateTimeParseException e) {
        System.out.println("[!] Unable to parse datetime - Please use 'dd/MM/yyyy HHmm', or weekday + optional time (e.g., mon 1200)");
        return null;
    }
}

    /**
     * Parses a string to convert day of the week and returns the corresponding DayOfWeek object.
     *
     * The input string can be a short form ("mon", "tue") or a full day name ("monday", "tuesday")
     * This function will remove trailing whitespaces and lowercases user's input so that it is normalised
     *
     * If the input is null or does not match any valid day, the method returns null
     *
     * @param input The string representing a day of the week
     * @return The corresponding DayOfWeek object, or null if the input is invalid
     */

    private static DayOfWeek parseDayOfWeek(String input) {
        if (input == null) {
            return null; 
        }       
        String normalised = input.trim().toLowerCase();
        switch (normalised) {
            case "mon": return DayOfWeek.MONDAY;
            case "tue": return DayOfWeek.TUESDAY;
            case "wed": return DayOfWeek.WEDNESDAY;
            case "thu": return DayOfWeek.THURSDAY;
            case "fri": return DayOfWeek.FRIDAY;
            case "sat": return DayOfWeek.SATURDAY;
            case "sun": return DayOfWeek.SUNDAY;
            default:
                // If shortcut not working, try the fullnames of each day
                try {
                    return DayOfWeek.valueOf(normalised.toUpperCase());
                } catch (IllegalArgumentException e) {
                    // Not valid string
                    return null;
                }
        }
    }

    /**
     * Converts a LocalDateTime object into a readable string format
     *
     * @param dateTimeObject The LocalDateTime to be formatted
     * @return A formatted string representing the date and time
     */
    public static String readableDate(LocalDateTime dateTimeObject) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy - HHmm");
        return dateTimeObject.format(formatter);
    }

    /**
     * Converts a LocalDateTime object into a readable string format for saving purposes
     * Format used: "dd/MM/yyyy HHmm"
     *
     * @param dateTimeObject The LocalDateTime to be formatted
     * @return A string representing the date and time in saving format
     */
    public static String saveDateFormat(LocalDateTime dateTimeObject) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");
        return dateTimeObject.format(formatter);
    }

    /**
     * Compares two LocalDateTime objects to determine if the first is before the second
     *
     * @param date_1 The first LocalDateTime
     * @param date_2 The second LocalDateTime
     * @return True if date_1 is before date_2, false otherwise
     */
    public static boolean compareDateTime(LocalDateTime date_1, LocalDateTime date_2) {
        return date_1.isBefore(date_2);
    }

    /**
     * Compares two LocalDateTime objects based on the date only, no time
     *
     * @param date_1 The first LocalDateTime
     * @param date_2 The second LocalDateTime
     * @return True if both LocalDateTime objects represent the same calendar day, false otherwise
     */
    public static boolean compareDate(LocalDateTime date_1, LocalDateTime date_2) {
        return date_1.getDayOfMonth() == date_2.getDayOfMonth()
            && date_1.getMonth() == date_2.getMonth()
            && date_1.getYear() == date_2.getYear();
    }
}
