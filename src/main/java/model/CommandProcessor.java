package model;

import java.util.Objects;

import logic.command.Command;
import logic.command.CommandType;
import common.ErrorMessage;

/**
 * Central processor for parsing and routing user commands into executable {@link Command} objects.
 * <p>
 * This class serves as the main entry point for command processing, handling the conversion
 * from raw user input strings to properly configured command instances. It implements a
 * clean separation between input parsing and command execution.
 * <p>
 * <b>Responsibilities:</b>
 * <ul>
 *     <li>Parse raw user input into command keywords and arguments</li>
 *     <li>Route commands to appropriate {@link Command} implementations</li>
 *     <li>Provide dependency injection for command creation</li>
 *
 * @implNote This class is marked final as it represents a complete implementation
 *         that should not be extended. Command-specific logic belongs in the individual
 *         Command implementations.
 */
public final class CommandProcessor {
    private final TaskManager taskManager;

    public CommandProcessor(TaskManager taskManager) {
        this.taskManager = Objects.requireNonNull(taskManager, "Task Manager must not be null");
    }

    /**
     * Splits user input on first whitespace to separate command keyword from arguments,
     * then delegates to {@link CommandType} for command instantiation.
     * <p>
     * Examples:
     * <ul>
     *     <li>{@code "list"} → {@code command="list", args=""}</li>
     *     <li>{@code "deadline finish /by 11/11/2011"} → {@code command="deadline", args="finish /by 11/11/2011"}</li>
     * </ul>
     *
     * @param input raw user input string
     * @return {@link Command} object ready to execute, or {@link ErrorMessage} if parsing fails
     * @apiNote The returned command is guaranteed to be non-null and executable.
     */
    public Command parseCommand(String input) {
        if (input == null || input.isBlank()) {
            return () -> new ErrorMessage(
                    String.format(ErrorMessage.INVALID_COMMAND_KEYWORD, input));
        }
        String[] tokens = input.trim().split("\\s+", 2);
        String command = tokens[0].toLowerCase();
        String args = tokens.length > 1 ? tokens[1] : "";

        CommandType cmdType = CommandType.fromKeyword(command);
        if (cmdType == null) {
            return () -> new ErrorMessage(
                    String.format(ErrorMessage.INVALID_COMMAND_KEYWORD, command));
        }
        return cmdType.createCommand(taskManager, args);
    }
}
