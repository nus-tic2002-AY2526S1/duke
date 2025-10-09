//AI write this to improve errot handling system in my system

public class InputSanitizer {
    private static final int MAX_INPUT_LENGTH = 2000;
    private static final int MAX_DESCRIPTION_LENGTH = 1000;
    private static final int MAX_TIME_LENGTH = 100;

    public static String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }

        // Remove control characters that could cause issues
        String sanitized = input.replaceAll("[\\x00-\\x1F\\x7F]", "");

        // Prevent overly long inputs
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

    public static boolean isValidCommand(String command) {
        if (command == null || command.trim().isEmpty()) {
            return false;
        }

        String[] validCommands = {"list", "mark", "unmark", "todo", "deadline", "event", "view", "bye", "save"};
        for (String valid : validCommands) {
            if (valid.equalsIgnoreCase(command.trim())) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsMaliciousPatterns(String input) {
        if (input == null) return false;

        // Basic patterns that might indicate injection attempts
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
}