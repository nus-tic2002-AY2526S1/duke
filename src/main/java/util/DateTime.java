package util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;

public final class DateTime {

    private DateTime() {}

    public static final DateTimeFormatter OUTPUT_DATE =
            DateTimeFormatter.ofPattern("MMM d yyyy");
    private static final DateTimeFormatter OUTPUT_TIME_12H =
            DateTimeFormatter.ofPattern("h:mma");

    private static final String DATE_ERROR_MSG =
            "Invalid date format. Use yyyy-MM-dd (e.g. 2020-12-02).";
    private static final String DATETIME_ERROR_MSG =
            "Invalid datetime format. Use yyyy-MM-dd HHmm (e.g. 2020-12-02 1800).";

    public static LocalDate parseDate(String s) {
        s = s.trim();
        String[] parts = s.split("-");
        if (parts.length != 3) {
            throw new IllegalArgumentException(DATE_ERROR_MSG);
        }

        try {
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);
            return LocalDate.of(year, month, day);
        } catch (NumberFormatException | DateTimeException e) {
            throw new IllegalArgumentException(DATE_ERROR_MSG);
        }
    }

    public static LocalDateTime parseDateTime(String s) {
        s = s.trim();
        int spaceIdx = s.lastIndexOf(' ');
        if (spaceIdx < 0) {
            throw new IllegalArgumentException(DATETIME_ERROR_MSG);
        }

        String datePart = s.substring(0, spaceIdx).trim();
        String timePart = s.substring(spaceIdx + 1).trim();

        if (timePart.length() != 4
                || !Character.isDigit(timePart.charAt(0))
                || !Character.isDigit(timePart.charAt(1))
                || !Character.isDigit(timePart.charAt(2))
                || !Character.isDigit(timePart.charAt(3))) {
            throw new IllegalArgumentException(DATETIME_ERROR_MSG);
        }

        int hour;
        int minute;
        try {
            hour = Integer.parseInt(timePart.substring(0, 2));
            minute = Integer.parseInt(timePart.substring(2, 4));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(DATETIME_ERROR_MSG);
        }

        if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
            throw new IllegalArgumentException(DATETIME_ERROR_MSG);
        }

        LocalDate date = parseDate(datePart);

        return date.atTime(hour, minute);
    }

    public static String outputDate(LocalDate d) {
        return d.format(OUTPUT_DATE);
    }

    public static String outputDateTime(LocalDateTime dt) {
        String date = dt.format(OUTPUT_DATE);
        String time = dt.format(OUTPUT_TIME_12H).replace(":00", "").toLowerCase();
        return date + ", " + time;
    }

    public static String outputTime(LocalDateTime dt) {
        return dt.format(OUTPUT_TIME_12H).replace(":00", "").toLowerCase();
    }

    public static String formatRawDateTimeSafe(String raw) {
        try {
            return outputDateTime(parseDateTime(raw));
        } catch (IllegalArgumentException ex) {
            return raw;
        }
    }
}
