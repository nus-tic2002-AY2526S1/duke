package common;

import common.message.Message;

/**
 * Immutable message record containing error information for display to users.
 * <p>
 * This record encapsulates error messages with contextual, user-friendly text
 * that follows MeeBot's Singaporean's conversational tone and is descriptive enough
 * to help users understand what went wrong and how to correct their input.
 */
public record ErrorMessage(String message) implements Message {

    // Command and input errors
    public static final String INVALID_COMMAND_KEYWORD = """
            Sorry, I'm mee-xed up by '%s'. I can help with:
            todo, deadline, event, list, (un)mark, delete, filter, sort.
            Type the command keywords (e.g. sort) for details.
            """;
    public static final String INVALID_NUMBER_FORMAT = """
            Eh friend, '%s' is not a number lah. Numbers are whole numbers like 1, 2, 3.
            """;
    public static final String TASK_FORMAT = """
            Your command is messier than mee goreng!
            %s
            """;
    public static final String FILTER_FORMAT = """
            %s Pick at least 1 from these 3 criteria:
            • task:todo | deadline | event
            • done:true | false
            • date:YYYY-MM-DD
            Try: filter task:deadline
            Or : filter task:deadline & done:false
            Or : filter task:deadline & done:false & date:2024-01-15
            """;

    // Task existence and state errors
    public static final String EMPTY_LIST = """
            Your task list is emptier than kopitiam during circuit breaker. \
            Add a task before using this command!
            """;
    public static final String TASK_NOT_FOUND = """
            Task %d doesn't exist.
            Type 'list' to see what's actually in your task list lah!
            """;
    public static final String TASK_STATE = """
            Task is already %s, no point doing it again what!
            Try '%s %d' instead.
            """;
    public static final String DUPLICATE_TASK = """
            I am getting déjà vu!
            '%s' is already in your list.
            Type 'list' to see what's actually in your task list lah!
            """;

    // Date time errors
    public static final String INVALID_DATETIME_VALUE = """
            '%s' doesn't exist.
            Your date/time more fictional than Korean drama plot!
            Double check:
            • Dates : 1-31 for day, 1-12 for month
            • Time  : 0000-2359 (Use 24-hour format)
            • Format: day/month/year (not American style!)
            """;
    public static final String INVALID_DATETIME_FORMAT = """
            Incorrect date/time format, please use one of these:
            • Day/Month/Year: 1/1/2025 or 1-1-2025
            • Long format: 1 Jan 2025
            • ISO format: 2025-01-01
            • Optionally add time in 24-hour format: 0000-2359
            """;
    public static final String END_BEFORE_START = """
            End time cannot be earlier than start time. That's like eating dessert before the main course lah!
            Make sure both dates have proper timing, or end time is after start time!
            """;

    // File errors
    public static final String INVALID_JSON_FORMAT = """
            The file is corrupted lah, did a cat walk over the keyboard when you are saving the file?
            """;

    // Catch-all
    public static final String CATCH_ALL = """
            Mee-stakes happen lah, try again!
            """;
}
