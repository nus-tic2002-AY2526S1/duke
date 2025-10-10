package whisperwind.util;

import whisperwind.model.*;


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
                "event", "view", "bye", "save"
        };

        // FIXED: Changed 'command' to 'input'
        String normalizedInput = input.trim().toLowerCase();
        for (String valid : validCommands) {
            if (valid.equals(normalizedInput)) {
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
}