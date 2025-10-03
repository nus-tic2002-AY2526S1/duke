package task.factory;

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
import task.DeadlineTask;
import task.Recurrence;
import task.Task;

import java.util.regex.Pattern;

/**
 * Factory for creating deadline task instances from user input or JSON data.
 * <p>
 * This creator handles the parsing and validation of deadline-specific input formats,
 * including task descriptions, due dates/times, and recurrence patterns.
 *
 * @see TaskCreator
 * @see DeadlineTask
 */
public class DeadlineCreator implements TaskCreator {

    /**
     * Regular expression pattern to parse deadline command input.
     * <p>
     * Matches input in the format: {@code description /by dateTime}
     * where the {@code /by} separator is case-insensitive.
     *
     * @apiNote Pattern generated with AI assistance for optimal matching
     */
    private static final Pattern DEADLINE_PATTERN = Pattern.compile(
            "(.+?)\\s*/by\\s*(.+)",
            Pattern.CASE_INSENSITIVE
    );

    /**
     * Creates a deadline task from user command arguments.
     *
     * @param args the command arguments in format {@code "description /by dateTime [recurrence]"}
     * @return a new {@link DeadlineTask} instance
     * @throws InvalidTaskFormatException    if the input doesn't match the expected format
     * @throws InvalidDateTimeException      if the date/time string cannot be parsed
     * @throws InvalidTaskOperationException if recurrence validation fails
     */
    @Override
    public Task createFromArgs(String args)
            throws InvalidTaskFormatException, InvalidDateTimeException, InvalidTaskOperationException {

        String[] tokens = StringTokenizer.tokenize(
                args, DEADLINE_PATTERN, 2, ErrorType.DEADLINE);
        ParsedDateTime parsed = DateTimeParser.parse(tokens[1]);
        Recurrence recurrence = RecurrenceParser.parse(
                tokens[tokens.length - 1],
                parsed.dateTime().toLocalDate(),
                ErrorType.RECURRENCE
        );
        return new DeadlineTask(tokens[0], parsed, recurrence);
    }

    /**
     * Creates a deadline task from a JSON object representation.
     *
     * @param obj the JSON object containing deadline task data
     * @return a new {@link DeadlineTask} instance
     * @throws FileContentException     if required fields are missing or invalid
     * @throws InvalidDateTimeException if the datetime string cannot be parsed
     */
    @Override
    public Task createFromJson(SimpleJsonObject obj) throws FileContentException, InvalidDateTimeException {
        String desc = obj.requireNonEmpty("description");
        ParsedDateTime dl = DateTimeParser.parse(obj.requireNonEmpty("deadline"));
        Recurrence rec = RecurrenceParser.parse(obj, dl.dateTime().toLocalDate());
        return new DeadlineTask(desc, dl, rec);
    }
}
