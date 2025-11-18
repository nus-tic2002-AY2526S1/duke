package chatbot;

import java.time.*;
import java.time.format.*;
import java.util.Locale;

public class DateTimeParser {
    // Supported input formats
    private static final DateTimeFormatter[] SUPPORTED_FORMATS = new DateTimeFormatter[]{
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("d/M/yyyy HH:mm"),
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("d-M-yyyy HH:mm"),
            DateTimeFormatter.ofPattern("d-M-yyyy")
    };

    /** Parse flexible date/time input into LocalDateTime */
    public static LocalDateTime parse(String input) throws QianException {
        input = input.trim().toLowerCase();

        // 1️⃣ handle relative phrases
        if (input.equals("today")) return LocalDate.now().atStartOfDay();
        if (input.equals("tomorrow")) return LocalDate.now().plusDays(1).atStartOfDay();
        if (input.startsWith("this ")) return parseRelativeWeekday(input.substring(5).trim(), false);
        if (input.startsWith("next ")) return parseRelativeWeekday(input.substring(5).trim(), true);

        // 2️⃣ try known formats
        for (DateTimeFormatter fmt : SUPPORTED_FORMATS) {
            try {
                if (fmt.toString().contains("H")) {
                    return LocalDateTime.parse(input, fmt);
                } else {
                    // if only date given, use midnight
                    return LocalDate.parse(input, fmt).atStartOfDay();
                }
            } catch (DateTimeParseException ignored) {}
        }

        throw new QianException("I couldn't understand the date/time format. Try 'yyyy-MM-dd HH:mm' or 'next Monday 8:00'.");
    }

    private static LocalDateTime parseRelativeWeekday(String day, boolean nextWeek) throws QianException {
        // optionally allow a time, e.g. "monday 8:00"
        String[] parts = day.split(" ", 2);
        String weekday = parts[0];
        LocalTime time = LocalTime.MIDNIGHT;
        if (parts.length == 2) {
            try {
                time = LocalTime.parse(parts[1], DateTimeFormatter.ofPattern("H:mm"));
            } catch (DateTimeParseException e) {
                throw new QianException("Invalid time format. Use HH:mm, e.g. '8:30'.");
            }
        }

        DayOfWeek target;
        try {
            target = DayOfWeek.valueOf(weekday.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new QianException("Unknown weekday: " + weekday);
        }

        LocalDate today = LocalDate.now();
        int diff = target.getValue() - today.getDayOfWeek().getValue();
        if (diff < 0 || nextWeek) diff += 7;
        if (nextWeek) diff += 7;
        return today.plusDays(diff).atTime(time);
    }
}