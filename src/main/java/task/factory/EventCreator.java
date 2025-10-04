package task.factory;

import java.util.regex.Pattern;

import exception.FileContentException;
import exception.InvalidDateTimeException;
import exception.InvalidTaskFormatException;
import exception.InvalidTaskFormatException.ErrorType;
import exception.InvalidTaskOperationException;
import parser.RecurrenceParser;
import parser.commandargs.StringTokenizer;
import parser.datetime.DateTimeParser;
import parser.datetime.ParsedDateTime;
import parser.json.SimpleJsonObject;
import task.EventTask;
import task.Recurrence;
import task.Task;

/**
 * Factory for creating event task instances from user input or JSON data.
 * <p>
 * This creator handles the parsing and validation of event-specific input formats,
 * including task descriptions, from and to dates/times, and recurrence patterns.
 *
 * @see TaskCreator
 * @see EventTask
 */
public class EventCreator implements TaskCreator {

    /**
     * Regular expression pattern to parse event command input.
     * <p>
     * Matches input in the format: {@code description /from dateTime /to dateTime}
     * where the {@code /from} and {@code /to} separators are case-insensitive.
     *
     * @apiNote Pattern generated with AI assistance for optimal matching
     */
    private static final Pattern EVENT_PATTERN = Pattern.compile(
            "(.+?)\\s*/from\\s*(.+?)\\s*/to\\s*(.+)",
            Pattern.CASE_INSENSITIVE
    );

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

        String[] tokens = StringTokenizer.tokenize(
                args, EVENT_PATTERN, 3, ErrorType.EVENT);
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
    public Task createFromJson(SimpleJsonObject obj) throws FileContentException, InvalidDateTimeException {
        String desc = obj.requireNonEmpty("description");
        ParsedDateTime start = DateTimeParser.parse(obj.requireNonEmpty("start"));
        ParsedDateTime end = DateTimeParser.parse(obj.requireNonEmpty("end"));
        Recurrence rec = RecurrenceParser.parse(obj, end.dateTime().toLocalDate());
        return new EventTask(desc, start, end, rec);
    }
}
