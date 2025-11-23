package model.task.factory;

import java.util.regex.Pattern;

import common.exception.FileContentException;
import common.exception.InvalidDateTimeException;
import common.exception.InvalidTaskFormatException;
import common.exception.InvalidTaskFormatException.ErrorType;
import common.exception.InvalidTaskOperationException;
import logic.parser.RecurrenceParser;
import logic.parser.commandargs.ArgTokenizer;
import logic.parser.datetime.DateTimeParser;
import logic.parser.datetime.ParsedDateTime;
import logic.parser.json.SimpleJsonObject;
import model.task.EventTask;
import model.task.Recurrence;
import model.task.Task;

/**
 * Factory for creating event task instances from user input or JSON data.
 * <p>
 * This creator handles the parsing and validation of event-specific input formats,
 * including task descriptions, from and to dates/times, and recurrence patterns.
 *
 * @see TaskCreator
 * @see EventTask
 */
public final class EventCreator implements TaskCreator {

    /**
     * Creates an event task from user command arguments.
     *
     * @param args the command arguments in format {@code description /from dateTime /to dateTime [recurrence]"}
     * @return a new {@link EventTask} instance
     * @throws InvalidTaskFormatException    if the input doesn't match the expected format
     * @throws InvalidDateTimeException      if the date/time string cannot be parsed
     * @throws InvalidTaskOperationException if recurrence validation fails
     */
    @Override
    public Task createFromArgs(String args)
            throws InvalidTaskFormatException, InvalidDateTimeException, InvalidTaskOperationException {

        /* Pattern generated with AI assistance for optimal matching */
        final Pattern EVENT_PATTERN = Pattern.compile(
                "(.+?)\\s*/from\\s*(.+?)\\s*/to\\s*(.+)", Pattern.CASE_INSENSITIVE
        );
        String[] tokens = ArgTokenizer.tokenize(
                args, EVENT_PATTERN, 3, ErrorType.EVENT);

        assert tokens[0] != null && !tokens[0].isBlank() : "Description must not be null or empty";
        assert tokens[1] != null && !tokens[1].isBlank() : "Date must not be null or empty";
        assert tokens[2] != null && !tokens[2].isBlank() : "Recurrence must not be null or empty";

        ParsedDateTime start = DateTimeParser.parse(tokens[1]);
        ParsedDateTime end = DateTimeParser.parse(tokens[2]);
        Recurrence recurrence = RecurrenceParser.parse(
                tokens[tokens.length - 1],
                end.dateTime().toLocalDate(),
                ErrorType.RECURRENCE
        );

        return new EventTask(tokens[0], start, end, recurrence);
    }

    /**
     * Creates an event task from a JSON object representation.
     *
     * @param obj the JSON object containing event task data
     * @return a new {@link EventTask} instance
     * @throws FileContentException     if required fields are missing or invalid
     * @throws InvalidDateTimeException if the datetime string cannot be parsed
     */
    @Override
    public Task createFromJson(SimpleJsonObject obj)
            throws FileContentException, InvalidDateTimeException {

        String desc = obj.requireNonEmpty("description");
        ParsedDateTime start = DateTimeParser.parse(obj.requireNonEmpty("start"));
        ParsedDateTime end = DateTimeParser.parse(obj.requireNonEmpty("end"));
        Recurrence rec = RecurrenceParser.parse(obj, end.dateTime().toLocalDate());

        assert desc != null && !desc.isEmpty()
                : "Description must not be null or empty";

        return new EventTask(desc, start, end, rec);
    }
}
