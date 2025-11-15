package com.golden.parser;

import com.golden.commands.Command;
import com.golden.commands.TodoCommand;
import com.golden.commands.DeadlineCommand;
import com.golden.commands.EventCommand;
import com.golden.commands.ListCommand;
import com.golden.commands.ExitCommand;
import com.golden.commands.EchoCommand;
import com.golden.commands.MarkCommand;
import com.golden.commands.UnmarkCommand;
import com.golden.commands.DeleteCommand;
import com.golden.commands.FindCommand;
import com.golden.exceptions.BotException;
import com.golden.exceptions.parseErrors.MissingArgumentException;
import com.golden.exceptions.parseErrors.UnknownCommandException;
import com.golden.util.ParseHelper;

public final class CommandParser {
    // overrides default public constructor method and makes it private
    // blocks anyone outside the class from instantiating new LineParser()
    private CommandParser(){}

    /**
     *  Parses an input line and returns the relevant Command object, if any, that is
     *  detected at the head of the raw string.
     *
     *  @param line The raw form of the full command.
     *
     *  @return     the Command object created from parsing the first word of the line,
     *              or a {@code Throwable} Exception.
     */
    public static Command parseCommand(String line) throws BotException {

        final String trimmedLine = ParseHelper.isNonBlank(line,
                "...Wait a minute!\nI can't read whitespace! But don't worry, I'm still here...");
        final String[] parts = trimmedLine.split("\\s+", 2);
        final String command = parts[0].trim().toLowerCase();

        switch (command) {
            case "hello":
            case "hi":
                return new EchoCommand(command);
            case "bye":
            case "goodbye":
                // check no further arguments are passed in after command, else suggest corrective actions.
                ParseHelper.requireArgs(parts, 1, "Try 'bye' or 'goodbye'.");
                return new ExitCommand();
            case "list":
                // check no further arguments are passed in after command, else suggest corrective actions.
                ParseHelper.requireArgs(parts, 1, "Try 'list'.");
                return new ListCommand();
            case "mark":
                // guard against empty argument being passed in after command when at least one is expected.
                ParseHelper.requireArgs(parts, 2, "task number");
                return new MarkCommand(TaskNumberParser.parseNumber(parts[1].trim()));
            case "unmark":
                ParseHelper.requireArgs(parts, 2, "task number");
                return new UnmarkCommand(TaskNumberParser.parseNumber(parts[1].trim()));
            case "delete":
                ParseHelper.requireArgs(parts, 2, "task number");
                return new DeleteCommand(TaskNumberParser.parseNumber(parts[1].trim()));
            case "todo":
                // guard against case where command is passed when more args are expected.
                ParseHelper.requireArgs(parts, 2, "task description");
                return new TodoCommand(TaskParser.parseTodoCommand(parts[1].trim()));
            case "deadline":
                ParseHelper.requireArgs(parts, 2, "task description");
                String[] deadlineArgs = TaskParser.parseDeadlineCommand(parts[1].trim());
                return new DeadlineCommand(deadlineArgs);
            case "event":
                ParseHelper.requireArgs(parts, 2, "task description");
                String[] eventArgs = TaskParser.parseEventCommand(parts[1].trim());
                return new EventCommand(eventArgs);
            case "find":
                ParseHelper.requireArgs(parts, 2, "search details");
                return new FindCommand(parts[1].trim());
            default:
                throw new UnknownCommandException(command);
        }
    }
}