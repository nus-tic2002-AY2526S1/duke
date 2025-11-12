package whisperwind.controller;

import whisperwind.Whisperwind;
import whisperwind.util.*;

/**
 * Dispatches user input commands to the appropriate handlers in the Whisperwind application.
 * Handles standard commands, archive commands, and instruction commands.
 */
public class CommandDispatcher {
    private final Whisperwind app;
    private final CommandHandler commandHandler;
    private final ArchiveHandler archiveHandler;

    /**
     * Constructs a CommandDispatcher for the given Whisperwind application.
     *
     * @param app Instance of the Whisperwind application.
     */
    public CommandDispatcher(Whisperwind app) {
        this.app = app;
        this.commandHandler = new CommandHandler(app);
        this.archiveHandler = new ArchiveHandler(app);
    }

    /**
     * Dispatches the user input to the appropriate command handler.
     * Handles instruction commands, archive commands, and general commands.
     *
     * @param input User input command string.
     * @return {@code true} if the command was executed successfully; {@code false} otherwise.
     */
    public boolean dispatchCommand(String input) {
        if (input == null || input.trim().isEmpty()) {
            System.out.println("It's giving 'silent treatment'. Say something!");
            return false;
        }

        if (!app.checkRateLimit()) {
            return false;
        }

        // Handle instruction commands first
        if (input.equalsIgnoreCase("view instruction")) {
            InstructionManager.showBasicInstructions();
            return false;
        }

        if (input.equalsIgnoreCase("delete instruction")) {
            InstructionManager.showDeleteInstructions();
            return false;
        }

        String[] parts = input.split(" ", 2);
        String command = parts[0].toLowerCase();

        // Route to appropriate handler
        if (command.equals("archive")) {
            return handleArchiveCommand(parts);
        } else {
            return commandHandler.handleCommand(command, parts);
        }
    }

    /**
     * Handles archive-related commands.
     * If a specific archive command is provided, delegates to {@link ArchiveHandler}.
     * Otherwise, shows archive help instructions.
     *
     * @param parts Array of input command parts, where the first element is "archive" and
     *              the second element (if present) is the archive sub-command.
     * @return Always returns {@code false} as archive commands do not terminate the program.
     */
    private boolean handleArchiveCommand(String[] parts) {
        if (parts.length > 1) {
            archiveHandler.handleArchiveCommand(parts[1]);
        } else {
            System.out.println("❓ What would you like to archive?");
            archiveHandler.showArchiveHelp();
        }
        return false;
    }
}
