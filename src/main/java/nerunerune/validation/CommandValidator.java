package nerunerune.validation;

import nerunerune.exception.NeruneruneException;

/**
 * Provides validation methods for user commands and input in the Nerunerune application.
 * Ensures commands contain necessary details and user inputs are not empty.
 */
public class CommandValidator {
    /**
     * Validates that the details following a command are not empty.
     * For deadline and event commands, also ensures the description part is not empty.
     *
     * @param command    the command word (e.g. "todo", "mark", "deadline", "event")
     * @param taskString the string containing task details after the command
     * @throws NeruneruneException if task details are empty, or if deadline/event descriptions are empty
     */
    public static void validateCommandDetails(String command, String taskString) throws NeruneruneException {
        if (taskString.isEmpty()) {
            throw new NeruneruneException("Details after '" + command + "' cannot be empty.");
        }

        if (command.equals("deadline")) {
            int findIndex = taskString.indexOf("/by");
            if (findIndex == 0) {
                throw new NeruneruneException("Details after deadline cannot be empty");
            }
        } else if (command.equals("event")) {
            int findIndexFrom = taskString.indexOf("/from");
            if (findIndexFrom == 0) {
                throw new NeruneruneException("Details after event  cannot be empty");
            }
        }
    }

    /**
     * Validates that a command does not have any arguments.
     * Used for commands that should not accept user input (e.g. "list", "bye", "command").
     *
     * @param command    the command word (e.g. "list", "bye", "command")
     * @param taskString the string containing any arguments after the command
     * @throws NeruneruneException if the command has arguments when it should not
     */
    public static void validateNoArguments(String command, String taskString) throws NeruneruneException {
        if (!taskString.isEmpty()) {
            throw new NeruneruneException("Command '" + command + "' does not take any arguments.");
        }
    }


    /**
     * Validates that the user input is not empty or null.
     *
     * @param userInput the full input string entered by the user
     * @throws NeruneruneException if input is empty or only whitespace
     */
    public static void validateUserInputNotEmpty(String userInput) throws NeruneruneException {
        if (userInput == null || userInput.trim().isEmpty()) {
            throw new NeruneruneException("Huh! You didn't type anything\n");
        }
    }

}
