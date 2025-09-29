package task.factory;

import exception.InvalidTaskFormatException.ErrorType;
import exception.MeeBotException;
import parser.RecurrenceParser;
import parser.commandargs.StringTokenizer;
import parser.datetime.DateTimeParser;
import parser.datetime.ParsedDateTime;
import parser.json.SimpleJsonObject;
import task.EventTask;
import task.Recurrence;
import task.Task;

import java.util.regex.Pattern;

public class EventCreator implements TaskCreator {

    /**
     * Regular expression pattern to parse event command input.
     *
     * @apiNote Pattern generated with AI assistance for optimal matching
     */
    private static final Pattern EVENT_PATTERN = Pattern.compile(
            "(.+?)\\s*/from\\s*(.+?)\\s*/to\\s*(.+)",
            Pattern.CASE_INSENSITIVE
    );

    @Override
    public Task createFromArgs(String args) throws MeeBotException {
        String[] tokens = StringTokenizer.tokenize(
                args, EVENT_PATTERN, 3, ErrorType.EVENT);
        ParsedDateTime start = DateTimeParser.parse(tokens[1]);
        ParsedDateTime end = DateTimeParser.parse(tokens[2]);
        Recurrence recurrence = RecurrenceParser.parse(
                tokens[tokens.length - 1], end.dateTime().toLocalDate(), ErrorType.RECURRENCE
        );
        return new EventTask(tokens[0], start, end, recurrence);
    }

    @Override
    public Task createFromJson(SimpleJsonObject obj) throws MeeBotException {
        String desc = obj.requireNonEmpty("description");
        ParsedDateTime start = DateTimeParser.parse(obj.requireNonEmpty("start"));
        ParsedDateTime end = DateTimeParser.parse(obj.requireNonEmpty("end"));
        Recurrence rec = RecurrenceParser.parse(obj, end.dateTime().toLocalDate());
        return new EventTask(desc, start, end, rec);
    }
}
