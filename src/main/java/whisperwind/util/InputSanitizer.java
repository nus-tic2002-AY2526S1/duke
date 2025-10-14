package whisperwind.util;

import whisperwind.model.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class InputSanitizer {
    private static final int MAX_INPUT_LENGTH = 2000;
    private static final int MAX_DESCRIPTION_LENGTH = 1000;
    private static final int MAX_TIME_LENGTH = 100;

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

    public static String sanitizeDescription(String description) {
        String sanitized = sanitizeInput(description);
        if (sanitized.length() > MAX_DESCRIPTION_LENGTH) {
            sanitized = sanitized.substring(0, MAX_DESCRIPTION_LENGTH);
            System.out.println("⚠️  Description truncated to " + MAX_DESCRIPTION_LENGTH + " characters");
        }
        return sanitized;
    }

    public static String sanitizeTime(String time) {
        String sanitized = sanitizeInput(time);
        if (sanitized.length() > MAX_TIME_LENGTH) {
            sanitized = sanitized.substring(0, MAX_TIME_LENGTH);
            System.out.println("⚠️  Time field truncated to " + MAX_TIME_LENGTH + " characters");
        }
        return sanitized;
    }

    public static boolean isValidCommand(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        String[] validCommands = {
                "list", "mark", "unmark", "delete", "todo", "deadline",
                "event", "view", "bye", "save", "find on"  // Added new command
        };

        String normalizedInput = input.trim().toLowerCase();
        for (String valid : validCommands) {
            if (normalizedInput.startsWith(valid)) {
                return true;
            }
        }
        return false;
    }

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

    public static String extractSafePattern(String input) {
        if (input == null) return "";

        String sanitized = sanitizeInput(input);
        sanitized = sanitized.replaceAll("[\\$\\.\\+\\?\\^\\{\\}\\[\\]\\(\\)\\|\\\\]", "");
        return sanitized;
    }

    // New methods for date/time validation
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

    public static String validateAndFormatDate(String dateStr) {
        if (!isValidDateFormat(dateStr)) {
            throw new IllegalArgumentException(
                    "Invalid date format for search. Use: yyyy-MM-dd (e.g., 2019-12-02)"
            );
        }
        return sanitizeInput(dateStr);
    }

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

    private static LocalDateTime parseDateTime(String dateTimeStr) {
        try {
            // Try format like "2/12/2019 1800"
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("d/M/yyyy HHmm"));
        } catch (DateTimeParseException e1) {
            // Try format "yyyy-MM-dd HHmm"
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        }
    }

    public static String getDateTimeFormatExamples() {
        return "📅 Date formats supported:\n" +
                "• 2/12/2019 1800 (2nd Dec 2019, 6:00 PM)\n" +
                "• 2019-12-02 1800 (2nd Dec 2019, 6:00 PM)\n" +
                "• 25/12/2023 0900 (25th Dec 2023, 9:00 AM)";
    }
}