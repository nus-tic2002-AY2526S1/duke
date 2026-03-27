package kev;

import kev.command.*;
import kev.exception.KevException;

/**
 * responsible for parsing user input into executable commands.
 */
public class Parser {

    /**
     * parses the user input and returns the corresponding command.
     * throws an exception if the input is invalid or incomplete.
     *
     * @param input The raw user input string.
     * @return The corresponding Command object.
     * @throws KevException If the input is invalid or unrecognized.
     */
    public static Command parse(String input) throws KevException {
        String[] words = input.split(" ", 2);
        String command = words[0];

        switch (command) {
            case "bye":
                return new ExitCommand();

            case "list":
                return new ListCommand();

            case "mark":
                return parseMarkCommand(words);

            case "unmark":
                return parseUnmarkCommand(words);

            case "delete":
                return parseDeleteCommand(words);

            case "todo":
            case "deadline":
            case "event":
                return parseAddCommand(input, words);

            case "on":
                return parseOnCommand(words);

            case "snooze":
                return parseSnoozeCommand(words);

            case "find":
                return parseFindCommand(words);

            case "remind":
                return new RemindCommand();

            default:
                throw new KevException("Invalid command, please try again with any of the following commands\n" +
                        "todo\ndeadline\nevent\non\nsnooze\nfind\nremind\nlist\nmark\nunmark\ndelete\nbye");
        }
    }

    private static Command parseMarkCommand(String[] words) throws KevException {
        if (words.length < 2) {
            throw new KevException("Please review tasklist and provide a valid task index." +
                    "Use command list to view tasklist.");
        }
        try {
            int index = Integer.parseInt(words[1]) - 1;
            return new MarkCommand(index);
        } catch (NumberFormatException e) {
            throw new KevException("Please review tasklist and provide a valid task index." +
                    "Use command list to view tasklist.");
        }
    }

    private static Command parseUnmarkCommand(String[] words) throws KevException {
        if (words.length < 2) {
            throw new KevException("Please review tasklist and provide a valid task index." +
                    "Use command list to view tasklist.");
        }
        try {
            int index = Integer.parseInt(words[1]) - 1;
            return new UnmarkCommand(index);
        } catch (NumberFormatException e) {
            throw new KevException("Please review tasklist and provide a valid task index." +
                    "Use command list to view tasklist.");
        }
    }

    // ================= Helper methods for parsing specific commands =================
    private static Command parseDeleteCommand(String[] words) throws KevException {
        if (words.length < 2) {
            throw new KevException("You must provide the task index to delete.");
        }
        try {
            int index = Integer.parseInt(words[1]) - 1;
            return new DeleteCommand(index);
        } catch (NumberFormatException e) {
            throw new KevException("The task index must be a valid integer.");
        }
    }

    private static Command parseAddCommand(String input, String[] words) throws KevException {
        if (words.length < 2 || words[1].trim().isEmpty()) {
            throw new KevException("The description of a task cannot be empty.");
        }
        return new AddCommand(input);
    }

    private static Command parseOnCommand(String[] words) throws KevException {
        if (words.length < 2 || words[1].trim().isEmpty()) {
            throw new KevException("Please provide a date: on YYYY-MM-DD");
        }
        return new OnCommand(words[1].trim());
    }

    private static Command parseSnoozeCommand(String[] words) throws KevException {
        if (words.length < 2 || words[1].trim().isEmpty()) {
            throw new KevException("Please provide task number and new date/time.");
        }
        String[] args = words[1].split(" ", 2);
        if (args.length < 2) throw new KevException("Provide both task number and new date/time.");
        int snoozeIndex;
        try {
            snoozeIndex = Integer.parseInt(args[0]) - 1;
        } catch (NumberFormatException e) {
            throw new KevException("Task number must be an integer.");
        }

        // args[1] contains either:
        // - for Deadline: YYYY-MM-DD
        // - for Event: YYYY-MM-DD HH:mm YYYY-MM-DD HH:mm
        return new SnoozeCommand(snoozeIndex, args[1].trim());
    }


    private static Command parseFindCommand(String[] words) throws KevException {
        if (words.length < 2 || words[1].trim().isEmpty()) {
            throw new KevException("You must provide a keyword to find.");
        }
        return new FindCommand(words[1].trim());
    }
}


