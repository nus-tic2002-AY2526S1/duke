package com.golden.parser;

import com.golden.task.Task;
import com.golden.task.Todo;
import com.golden.task.Deadline;
import com.golden.task.Event;
import com.golden.task.Priority;
import com.golden.util.ParseHelper;
import com.golden.util.ValidationHelper;
import com.golden.exceptions.BotException;
import com.golden.exceptions.ParseException;
import com.golden.exceptions.storageErrors.StorageFileParseException;
import com.golden.exceptions.validationErrors.IllegalArgumentException;

import java.time.LocalDate;
import java.util.Locale;

public class TaskParser {
    private TaskParser(){}

    /** Parses a line in the following format: '[Task type] | [isDone] | [Task Description] | [Date 1] | [Date 2]'
     *  into a task (Todo, Deadline, Event)
     *
     * @return  a Task object that could be a 'Todo', 'Deadline' or 'Event' or a {@code Throwable} Exception.
     *  */
    public static Task parseStorageLine(String rawLine, int lineNo) throws BotException {
        String line = processRawLine(rawLine, lineNo);
        if (line == null) {
            return null; // caller treats this as "no task on this line"
        }

        // safe: line is non-null and non-empty here
        // Split by pipe; allow spaces around '|'   →  "T | 1 | read book | LOW"
        String[] parts = line.split("\\s*\\|\\s*");
        int minimumParts = 4;
        if (parts.length < minimumParts) {
            throw lineError(lineNo,
                    String.format("expects at least %d fields. Got: %d", minimumParts, parts.length));
        }
        String type = parts[0].trim().toUpperCase(Locale.ROOT);
        boolean isDone = isDone(parts[1].trim(), lineNo);
        String desc = parts[2].trim();
        if (desc.isEmpty()) {
            throw lineError(lineNo, "description cannot be empty.");
        }
        Priority priority = ParseHelper.convertStringToPriority(parts[parts.length - 1]);

        switch (type) {
            case "T":
                if (parts.length != 4) {
                    throw lineError(lineNo, "Todo must have this format: 'T | isDone | task | priority'.");
                }
                return new Todo(desc, isDone, priority);

            case "D":
                if (parts.length != 5) {
                    throw lineError(lineNo, "Deadline must have this format: 'D | isDone | task | by | priority'.");
                }
                LocalDate byDate = ParseHelper.convertStringToDate(parts[3].trim());
                return new Deadline(desc, isDone, byDate, priority);

            case "E":
                if (parts.length != 6) {
                    throw lineError(lineNo, "Event must have this format: 'E | isDone | task | from | to | priority'.");
                }
                LocalDate from = ParseHelper.convertStringToDate(parts[3].trim());
                LocalDate to = ParseHelper.convertStringToDate(parts[4].trim());
                ValidationHelper.validateDateOrder(from, to);
                return new Event(desc, isDone, from, to, priority);

            default:
                throw lineError(lineNo, String.format(
                "unknown task type '%s'. \nValid task types: 'T', 'D', or 'E'.", rawLine));
        }
    }

    /** Parses a String and returns its parts: '[String before /by]' and '[String from /by]', '[String from /priority]'.
     *
     * @return  a String array or a {@code Throwable} Exception.
     *  */
    public static String[] parseDeadlineCommand(String rawArgs) throws ParseException {
        String[] deadlineArgs = ParseHelper.splitOnSlashSections(rawArgs.trim());
        ParseHelper.requireArgs(deadlineArgs, 3, "task description, deadline or priority");
        return deadlineArgs;
    }

    /** Parses a String and returns its parts: '[String before /from]', '[String from /from],
     * '[String from /to], '[String from /priority]'.
     *
     * @return  a String array or a {@code Throwable} Exception.
     *  */
    public static String[] parseEventCommand(String rawArgs) throws ParseException{
        String[] eventArgs = ParseHelper.splitOnSlashSections(rawArgs.trim());
        ParseHelper.requireArgs(eventArgs, 4, "task description, from, to, or priority");
        return eventArgs;
    }

    /** Parses a String and returns its parts: '[String before /priority]' and '[String from /priority]'.
     *
     * @return  a String array or a {@code Throwable} Exception.
     *  */
    public static String[] parseTodoCommand(String rawArgs) throws ParseException{
        String[] todoArgs = ParseHelper.splitOnSlashSections(rawArgs.trim());
        ParseHelper.requireArgs(todoArgs, 2, "task description or priority");
        return todoArgs;
    }

    private static boolean isDone(String doneStr, int lineNo) throws StorageFileParseException {
        if ("1".equals(doneStr)) {
            return true;
        }
        else if ("0".equals(doneStr)) {
            return false;
        } else {
            throw new StorageFileParseException(String.format(
                    "Line: %d: done flag must be 0 or 1. Got '%s'.", lineNo, doneStr));
        }
    }

    private static String processRawLine(String rawLine, int lineNo) throws StorageFileParseException {
        if (rawLine == null) {
            throw lineError(lineNo, "null line.");
        }

        String line = rawLine.trim();
        // check for empty/comment lines
        if (line.isEmpty() || line.startsWith("#")){
            return null;    // signal "skip"
        }
        return line;
    }

    private static StorageFileParseException lineError(int lineNo, String message) {
        return new StorageFileParseException(String.format("Line %d: %s", lineNo, message));
    }
}


// stop here: parseXXCommand always checks for required arguments- mandates that priority is given. how to develop
// a system where /priority can be an optional input?