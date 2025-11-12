package parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import commands.AddDeadlineCommand;
import commands.AddEventCommand;
import commands.AddTodoCommand;
import commands.Command;
import commands.DeleteCommand;
import commands.ErrorCommand;
import commands.FindTaskCommand;
import commands.ListCommand;
import commands.MarkCommand;
import commands.SetPriorityCommand;
import commands.UnmarkCommand;
import static common.Messages.ERROR_EMPTY_DESC;
import static common.Messages.ERROR_INVALID_FROM_TO;
import static common.Messages.ERROR_INVALID_INTEGER;
import static common.Messages.ERROR_INVALID_PRIORITY;
import static common.Messages.ERROR_OUT_OF_BOUNDS;
import static common.Messages.ERROR_TO_BEFORE_FROM;
import static common.Messages.ERROR_UNKNOWN_COMMAND;
import static common.Messages.ERROR_WRONG_DATE_FORMAT;
import common.Priority;

/**
 * Makes sense of the user's input.
 */
public class Parser {

    /**
     * Parses the user input into a Command.
     *
     * @param input user input string
     * @return the parsed Command
     */
    public Command parse(String input) {

        String commandWord = input.split(" ")[0];
        String arguments = input.substring(commandWord.length()).strip();

        switch (commandWord) {
            case "todo":
                return (prepareAddTodo(arguments));
            case "deadline":
                return (prepareAddDeadline(arguments));
            case "event":
                return (prepareAddEvent(arguments));
            case "list":
                return (prepareList());
            case "mark":
                return (prepareMark(arguments));
            case "unmark":
                return (prepareUnmark(arguments));
            case "delete":
                return (prepareDelete(arguments));
            case "find":
                return (prepareFind(arguments));
            case "priority":
                return (preparePriority(arguments));
            case "bye":
                System.exit(0);
            default:
                return new ErrorCommand(String.format(ERROR_UNKNOWN_COMMAND, input));
        }
    }

    /**
     * Parses arguments in the context of the AddTodoCommand. Returns error if
     * arguments are blank.
     *
     * @param arguments input except for first word
     * @return the prepared command
     */
    private Command prepareAddTodo(String arguments) {
        if (arguments.isBlank()) {
            return new ErrorCommand(ERROR_EMPTY_DESC);
        }
        return new AddTodoCommand(arguments);
    }

    /**
     * Parses arguments in the context of the AddDeadlineCommand. Returns error
     * if arguments are blank. Returns error if /by is missing. Returns error if
     * deadline is not formatted in dd/MM/yyyy HHmm
     *
     * @param arguments input except for first word
     * @return the prepared command
     */
    private Command prepareAddDeadline(String arguments) {
        assert arguments != null : "Arguments should not be null";

        if (!arguments.contains(" /by ")) {
            return new ErrorCommand(ERROR_EMPTY_DESC);
        }

        String[] result = arguments.split(" /by ");
        assert result.length >= 2 : "Split result should have at least 2 parts";

        String description = result[0].trim(); // Add trim() here
        String deadline = result[1].trim();     // And here

        // Check for empty description BEFORE trying to parse the date
        if (description.isBlank()) {
            return new ErrorCommand(ERROR_EMPTY_DESC);
        }

        assert deadline != null : "Deadline string should not be null";

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");
            LocalDateTime parsedDateTime = LocalDateTime.parse(deadline, formatter);
            assert parsedDateTime != null : "Parsed datetime should not be null";

            Command command = new AddDeadlineCommand(description, parsedDateTime);
            assert command != null : "Command should not be null";
            return command;
        } catch (DateTimeParseException dtpe) {
            return new ErrorCommand(ERROR_WRONG_DATE_FORMAT);
        }
    }

    /**
     * Parses arguments in the context of the AddEventCommand. Returns error if
     * arguments are blank. Returns error if /from or /to is missing. Returns
     * error if deadline is not formatted in dd/MM/yyyy HHmm
     *
     * @param arguments input except for first word
     * @return the prepared command
     */
    private Command prepareAddEvent(String arguments) {
        // Remove any leading indexes or accidental numbers
        arguments = arguments.trim().replaceFirst("^\\d+\\s*", "");

        // Regex to capture: description, fromDate, toDate
        Pattern p = Pattern.compile("^(.*?)\\s*/from\\s*(.*?)\\s*/to\\s*(.*)$");
        Matcher m = p.matcher(arguments);

        if (!m.matches()) {
            return new ErrorCommand(ERROR_INVALID_FROM_TO);
        }

        String description = m.group(1).trim();
        String fromDate = m.group(2).trim();
        String toDate = m.group(3).trim();

        if (description.isBlank()) {
            return new ErrorCommand(ERROR_EMPTY_DESC);
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");
            LocalDateTime parsedFrom = LocalDateTime.parse(fromDate, formatter);
            LocalDateTime parsedTo = LocalDateTime.parse(toDate, formatter);
            if (parsedTo.isBefore(parsedFrom)) {
                return new ErrorCommand(ERROR_TO_BEFORE_FROM);
            }

            assert parsedFrom != null : "Parsed from datetime should not be null";
            assert parsedTo != null : "Parsed to datetime should not be null";
            assert !parsedTo.isBefore(parsedFrom) : "End time should not be before start time";

            Command command = new AddEventCommand(description, parsedFrom, parsedTo);
            assert command != null : "Command should not be null";
            return command;
        } catch (DateTimeParseException e) {
            return new ErrorCommand(ERROR_WRONG_DATE_FORMAT);
        }
    }

    private Command prepareList() {
        return new ListCommand();
    }

    /**
     * Parses arguments in the context of the MarkCommand. Returns error if
     * arguments are blank. Returns error if target index is out of bounds.
     *
     * @param arguments input except for first word.
     * @return the prepared command.
     */
    private Command prepareMark(String arguments) {
        Integer index;
        if (arguments.isBlank()) {
            return new ErrorCommand(ERROR_EMPTY_DESC);
        }
        try {
            index = Integer.parseInt(arguments) - 1;
        } catch (NumberFormatException e) {
            return new ErrorCommand(ERROR_INVALID_INTEGER);
        }
        return new MarkCommand(index);
    }

    /**
     * Parses arguments in the context of the UnmarkCommand. Returns error if
     * arguments are blank. Returns error if target index is out of bounds.
     *
     * @param arguments input except for first word.
     * @return the prepared command.
     */
    private Command prepareUnmark(String arguments) {
        Integer index;
        if (arguments.isBlank()) {
            return new ErrorCommand(ERROR_EMPTY_DESC);
        }
        try {
            index = Integer.parseInt(arguments) - 1;
        } catch (NumberFormatException e) {
            return new ErrorCommand(ERROR_INVALID_INTEGER);
        }
        return new UnmarkCommand(index);
    }

    /**
     * Parses arguments in the context of the DeleteCommand. Returns error if
     * arguments are blank. Returns error if target index is out of bounds.
     *
     * @param arguments input except for first word.
     * @return the prepared command.
     */
    private Command prepareDelete(String arguments) {
        Integer index;
        if (arguments.isBlank()) {
            return new ErrorCommand(ERROR_EMPTY_DESC);
        }
        try {
            index = Integer.parseInt(arguments) - 1;
        } catch (NumberFormatException e) {
            return new ErrorCommand(ERROR_INVALID_INTEGER);
        }
        return new DeleteCommand(index);
    }

    /**
     * Parses arguments in the context of the FindTaskCommand. Returns error if
     * arguments are blank.
     *
     * @param arguments input except for first word.
     * @return the prepared command.
     */
    private Command prepareFind(String arguments) {

        if (arguments.isBlank()) {
            return new ErrorCommand(ERROR_EMPTY_DESC);
        }

        return new FindTaskCommand(arguments);
    }

    /**
     * Parses arguments in the context of the PriorityCommand. Returns error if
     * arguments are blank.
     *
     * @param arguments input except for first word.
     * @return the prepared command.
     */
    private Command preparePriority(String arguments) {
        Integer index;
        Priority priority;
        if (arguments.isBlank()) {
            return new ErrorCommand(ERROR_EMPTY_DESC);
        }
        try {
            String indexString = arguments.split(" ")[0];
            index = Integer.parseInt(indexString) - 1;
            priority = Priority.valueOf(arguments.split(" ")[1].toUpperCase());
        } catch (NumberFormatException e) {
            return new ErrorCommand(ERROR_INVALID_INTEGER);
        } catch (ArrayIndexOutOfBoundsException e) {
            return new ErrorCommand(ERROR_OUT_OF_BOUNDS);
        } catch (IllegalArgumentException e) {
            return new ErrorCommand(ERROR_INVALID_PRIORITY);
        }

        return new SetPriorityCommand(index, priority);
    }
}
