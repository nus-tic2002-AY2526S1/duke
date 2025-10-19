package whisperwind.util;

import whisperwind.model.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * The {@code InputSanitizer} class provides utility methods for sanitizing and validating
 * user input to prevent security issues and ensure data integrity.
 * <p>
 * This class handles input validation, length restrictions, and format checking
 * for various types of user inputs including commands, descriptions, and dates.
 * </p>
 */
public class InputSanitizer {
    private static final int MAX_INPUT_LENGTH = 2000;
    private static final int MAX_DESCRIPTION_LENGTH = 1000;
    private static final int MAX_TIME_LENGTH = 100;

    /**
     * Sanitizes general user input by removing control characters and enforcing length limits.
     *
     * @param input the raw user input
     * @return the sanitized input string
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }

        String sanitized = input.replaceAll("[\\x00-\\x1F\\x7F]", "");

        if (sanitized.length() > MAX_INPUT_LENGTH) {
            sanitized = sanitized.substring(0, MAX_INPUT_LENGTH);
            System.out.println("⚠️  Input truncated to " + MAX_INPUT_LENGTH + " characters");
        }

        return sanitized.trim();
    }

    /**
     * Sanitizes task descriptions with additional length restrictions.
     *
     * @param description the task description to sanitize
     * @return the sanitized description
     */
    public static String sanitizeDescription(String description) {
        String sanitized = sanitizeInput(description);
        if (sanitized.length() > MAX_DESCRIPTION_LENGTH) {
            sanitized = sanitized.substring(0, MAX_DESCRIPTION_LENGTH);
            System.out.println("⚠️  Description truncated to " + MAX_DESCRIPTION_LENGTH + " characters");
        }
        return sanitized;
    }

    /**
     * Sanitizes time-related input fields.
     *
     * @param time the time string to sanitize
     * @return the sanitized time string
     */
    public static String sanitizeTime(String time) {
        String sanitized = sanitizeInput(time);
        if (sanitized.length() > MAX_TIME_LENGTH) {
            sanitized = sanitized.substring(0, MAX_TIME_LENGTH);
            System.out.println("⚠️  Time field truncated to " + MAX_TIME_LENGTH + " characters");
        }
        return sanitized;
    }

    /**
     * Validates if the input represents a known command.
     *
     * @param input the command input to validate
     * @return true if the command is valid, false otherwise
     */
    public static boolean isValidCommand(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        String[] validCommands = {
                "list", "mark", "unmark", "delete", "todo", "deadline",
                "event", "view", "bye", "save", "find on", "schedule", "find"
        };

        String normalizedInput = input.trim().toLowerCase();
        for (String valid : validCommands) {
            if (normalizedInput.startsWith(valid)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validates bulk delete input format (comma-separated numbers).
     *
     * @param input the bulk delete input to validate
     * @return true if the input format is valid, false otherwise
     */
    public static boolean isValidBulkDeleteInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        String[] parts = input.split(",");
        for (String part : parts) {
            if (!isValidTaskNumber(part.trim(), Integer.MAX_VALUE)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates pattern input for pattern-based deletion.
     *
     * @param input the pattern input to validate
     * @return true if the pattern is valid, false otherwise
     */
    public static boolean isValidPatternInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        String pattern = input.trim();
        if (pattern.equals("*") || pattern.equals("**")) {
            return false;
        }

        return true;
    }

    /**
     * Checks for potentially malicious patterns in input.
     *
     * @param input the input to check
     * @return true if suspicious patterns are found, false otherwise
     */
    public static boolean containsMaliciousPatterns(String input) {
        if (input == null) return false;

        String[] suspiciousPatterns = {
                "../", "\\..\\", "file://", "http://", "https://",
                "script", "javascript:", "<script", "<?php", "exec("
        };

        String lowerInput = input.toLowerCase();
        for (String pattern : suspiciousPatterns) {
            if (lowerInput.contains(pattern)) {
                System.out.println("⚠️  Suspicious input detected and filtered");
                return true;
            }
        }
        return false;
    }

    /**
     * Validates if a string represents a valid task number.
     *
     * @param input the input to validate
     * @param maxTasks the maximum number of tasks allowed
     * @return true if the input is a valid task number, false otherwise
     */
    public static boolean isValidTaskNumber(String input, int maxTasks) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        try {
            int taskNumber = Integer.parseInt(input.trim());
            return taskNumber > 0 && taskNumber <= maxTasks;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Extracts a safe pattern string by removing potentially dangerous characters.
     *
     * @param input the input pattern to sanitize
     * @return a safe pattern string
     */
    public static String extractSafePattern(String input) {
        if (input == null) return "";

        String sanitized = sanitizeInput(input);
        sanitized = sanitized.replaceAll("[\\$\\.\\+\\?\\^\\{\\}\\[\\]\\(\\)\\|\\\\]", "");
        return sanitized;
    }

    /**
     * Validates if a string represents a valid date-time format.
     *
     * @param dateTimeStr the date-time string to validate
     * @return true if the format is valid, false otherwise
     */
    public static boolean isValidDateTimeFormat(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return false;
        }

        try {
            // Try format like "2/12/2019 1800"
            LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("d/M/yyyy HHmm"));
            return true;
        } catch (DateTimeParseException e1) {
            try {
                // Try format "yyyy-MM-dd HHmm"
                LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
                return true;
            } catch (DateTimeParseException e2) {
                return false;
            }
        }
    }

    /**
     * Validates if a string represents a valid date format (for searching).
     *
     * @param dateStr the date string to validate
     * @return true if the format is valid, false otherwise
     */
    public static boolean isValidDateFormat(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return false;
        }

        try {
            // Try format "yyyy-MM-dd" for date search
            java.time.LocalDate.parse(dateStr);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Validates that a date-time string represents a future date-time.
     *
     * @param dateTimeStr the date-time string to validate
     * @return true if the date-time is in the future, false otherwise
     */
    public static boolean isFutureDateTime(String dateTimeStr) {
        if (!isValidDateTimeFormat(dateTimeStr)) {
            return false;
        }

        try {
            LocalDateTime inputDateTime = parseDateTime(dateTimeStr);
            return inputDateTime.isAfter(LocalDateTime.now());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validates and ensures a date-time is in the future.
     *
     * @param dateTimeStr the date-time string to validate
     * @return the sanitized date-time string
     * @throws IllegalArgumentException if the date-time is in the past
     */
    public static String validateFutureDateTime(String dateTimeStr) {
        if (!isValidDateTimeFormat(dateTimeStr)) {
            throw new IllegalArgumentException(
                    "Invalid date format. Use either:\n" +
                            "• d/M/yyyy HHmm (e.g., 2/12/2019 1800)\n" +
                            "• yyyy-MM-dd HHmm (e.g., 2019-12-02 1800)"
            );
        }

        if (!isFutureDateTime(dateTimeStr)) {
            throw new IllegalArgumentException("Date and time cannot be in the past! Please choose a future date and time.");
        }

        return sanitizeTime(dateTimeStr);
    }

    /**
     * Validates and formats a date-time string, throwing an exception if invalid.
     *
     * @param dateTimeStr the date-time string to validate
     * @return the sanitized date-time string
     * @throws IllegalArgumentException if the date-time format is invalid
     */
    public static String validateAndFormatDateTime(String dateTimeStr) {
        if (!isValidDateTimeFormat(dateTimeStr)) {
            throw new IllegalArgumentException(
                    "Invalid date format. Use either:\n" +
                            "• d/M/yyyy HHmm (e.g., 2/12/2019 1800)\n" +
                            "• yyyy-MM-dd HHmm (e.g., 2019-12-02 1800)"
            );
        }
        return sanitizeTime(dateTimeStr);
    }

    /**
     * Validates and formats a date string for search operations.
     *
     * @param dateStr the date string to validate
     * @return the sanitized date string
     * @throws IllegalArgumentException if the date format is invalid
     */
    public static String validateAndFormatDate(String dateStr) {
        if (!isValidDateFormat(dateStr)) {
            throw new IllegalArgumentException(
                    "Invalid date format for search. Use: yyyy-MM-dd (e.g., 2019-12-02)"
            );
        }
        return sanitizeInput(dateStr);
    }

    private static LocalDateTime parseDateTime(String dateTimeStr) {
        try {
            // Try format like "2/12/2019 1800"
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("d/M/yyyy HHmm"));
        } catch (DateTimeParseException e1) {
            // Try format "yyyy-MM-dd HHmm"
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        }
    }

    /**
     * Provides examples of supported date-time formats.
     *
     * @return a string containing format examples
     */
    public static String getDateTimeFormatExamples() {
        return "📅 Date formats supported:\n" +
                "• 2/12/2019 1800 (2nd Dec 2019, 6:00 PM)\n" +
                "• 2019-12-02 1800 (2nd Dec 2019, 6:00 PM)\n" +
                "• 25/12/2023 0900 (25th Dec 2023, 9:00 AM)";
    }

    /**
     * Provides examples of future date-time formats.
     *
     * @return a string containing future date format examples
     */
    public static String getFutureDateTimeFormatExamples() {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
        String exampleDate = tomorrow.format(formatter);

        return "📅 Future date formats supported:\n" +
                "• " + exampleDate + " (Tomorrow at current time)\n" +
                "• 25/12/2024 0900 (25th Dec 2024, 9:00 AM)\n" +
                "• 2024-12-25 1800 (25th Dec 2024, 6:00 PM)";
    }

    /**
     * Validates schedule command arguments.
     *
     * @param argument the schedule argument to validate
     * @return true if the schedule argument is valid, false otherwise
     */
    public static boolean isValidScheduleArgument(String argument) {
        if (argument == null || argument.trim().isEmpty()) {
            return false;
        }

        String scheduleArg = argument.trim().toLowerCase();

        // Check for predefined schedule types
        if (scheduleArg.equals("today") || scheduleArg.equals("tomorrow") || scheduleArg.equals("upcoming")) {
            return true;
        }

        // Check for date range
        if (scheduleArg.contains(" to ")) {
            String[] dates = scheduleArg.split(" to ");
            if (dates.length == 2) {
                return isValidDateFormat(dates[0].trim()) && isValidDateFormat(dates[1].trim());
            }
            return false;
        }

        // Check for single date
        return isValidDateFormat(scheduleArg);
    }

    /**
     * Extracts and validates schedule arguments.
     *
     * @param argument the raw schedule argument
     * @return an array containing validated schedule parameters
     * @throws IllegalArgumentException if the schedule argument is invalid
     */
    public static String[] parseScheduleArgument(String argument) {
        if (!isValidScheduleArgument(argument)) {
            throw new IllegalArgumentException(
                    "Invalid schedule argument. Use:\n" +
                            "• today, tomorrow, or upcoming\n" +
                            "• YYYY-MM-DD for a specific date\n" +
                            "• YYYY-MM-DD to YYYY-MM-DD for a date range"
            );
        }

        String scheduleArg = argument.trim().toLowerCase();

        if (scheduleArg.contains(" to ")) {
            String[] dates = scheduleArg.split(" to ");
            return new String[]{"range", dates[0].trim(), dates[1].trim()};
        } else if (scheduleArg.equals("today") || scheduleArg.equals("tomorrow") || scheduleArg.equals("upcoming")) {
            return new String[]{scheduleArg};
        } else {
            return new String[]{"date", scheduleArg};
        }
    }
}