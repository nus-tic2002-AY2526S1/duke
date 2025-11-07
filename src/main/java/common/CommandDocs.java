package common;

import logic.command.CommandType;

/**
 * Immutable record providing centralized command documentation and help text.
 * This record serves as a single source of truth for all user-facing command
 * documentation, including summary descriptions and detailed usage instructions.
 * <p>
 * <b>Documentation Types:</b>
 * <ul>
 *   <li><strong>Summary:</strong> Brief one-line descriptions for command overview</li>
 *   <li><strong>Details:</strong> Comprehensive usage instructions with examples</li>
 * </ul>
 *
 * @see CommandType
 */
public record CommandDocs() {

    /**
     * Returns a brief summary description for the specified command type.
     * <p>
     * Summaries are designed for display in command overview or quick reference
     * scenarios where space is limited. Each summary is a concise phrase describing
     * the command's primary purpose.
     *
     * @param type the command type to get summary for
     * @return brief description string, or empty string if type is null or unsupported
     */
    public static String getSummary(CommandType type) {
        return switch (type) {
            case HELP -> "Display help instructions";
            case BYE -> "Exit the chatbot";
            case TODO -> "Add a todo task";
            case DEADLINE -> "Add a deadline task";
            case EVENT -> "Add an event task";
            case LIST -> "Display all tasks";
            case FILTER -> "Filter tasks by criteria";
            case SEARCH -> "Find tasks by keywords";
            case SORT -> "Display tasks in sorted order";
            case DELETE -> "Delete a task by number";
            case MARK -> "Mark a task as done";
            case UNMARK -> "Mark a task as pending";
            default -> "";
        };
    }

    /**
     * Returns detailed usage instructions for the specified command type.
     * <p>
     * Details include command format specifications, parameter descriptions,
     * usage examples, and any important notes or constraints. This information
     * is designed for display when users request specific help for a command.
     *
     * @param type the command type to get detailed help for
     * @return comprehensive usage instructions, or empty string if type is unsupported
     */
    public static String getDetails(CommandType type) {
        return switch (type) {
            case TODO -> """
                    📝 To add a todo task.
                    Format: todo <description>
                    Try:    todo water plants
                    """;
            case DEADLINE -> """
                    ⏳ To add a task with due date, with [OPTIONAL] time and repeat.
                    Format: deadline <description> /by <date-time> [/repeat <interval> <count>]
                    Try:    deadline pay bills /by 1/11/2025 2359
                    Or :    deadline pay bills /by 1/11/2025 /repeat monthly 12
                    """;
            case EVENT -> """
                    📅 To add an event with start and end date, with [OPTIONAL] time and repeat.
                    Format: event <description> /from <date-time> /to <date-time> [/repeat <interval> <count>]
                    Try:    event yoga /from 1/11/2025 1000 /to 1/11/2025 1100
                    Or :    event birthday /from 1/11/2025 /to 1/11/2025 /repeat yearly 6
                    """;
            case DELETE -> """
                    🗑️ To delete a task, type 'list' to see your tasks, then use the number.
                    Format: delete <index>
                    Try:    delete 1
                    ⚠️ Note: Task cannot be recovered once deleted.
                    """;
            case MARK -> """
                    ✅ To mark a task as completed.
                    Format: mark <index>
                    Try:    mark 1
                    Tip:    Use 'unmark' to undo action
                    """;
            case UNMARK -> """
                    ⭕ To mark a task as not completed.
                    Format: unmark <index>
                    Try:    unmark 1
                    Tip:    Use 'mark' to undo action
                    """;
            case SEARCH -> """
                    🔍 To find tasks whose description contains one or more keywords.
                    Format: search <keyword1> <keyword2> ...
                    Try:    search day yoga plants
                    """;
            case SORT -> """
                    📅 Sort by date:   earliest first
                    🗂️ Sort by status: pending tasks first
                    Format: sort /by <date|status>
                    Try:    sort /by date
                    Or:     sort /by status
                    """;
            case FILTER -> """
                    🔎 To filter tasks — pick 1 to 3 conditions.
                    Format: filter task:<type> & done:<t|f> & date:<DD/MM/YYYY>
                    
                    Supported filters:
                    • task: todo|deadline|event
                    • done: true|false
                    • date: DD/MM/YYYY
                    Try: filter task:deadline
                    Or : filter task:deadline & done:false
                    Or : filter task:deadline & done:false & date:1/11/2025
                    """;
            default -> "";
        };
    }

    /**
     * Extracts only the "Format:" line from a command's detailed help.
     * Used by GUI to display inline syntax hints in the chat bar.
     *
     * @param type the command type
     * @return the format string (without the word "Format:"), or empty if none found
     */
    public static String getFormatHint(CommandType type) {
        String details = getDetails(type);
        for (String line : details.split("\n")) {
            if (line.trim().toLowerCase().startsWith("format:")) {
                return line.replaceFirst("(?i)format:\\s*", "").trim();
            }
        }
        return "";
    }
}
