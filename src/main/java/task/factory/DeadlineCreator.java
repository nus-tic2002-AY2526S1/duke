package task.factory;

import exception.InvalidTaskFormatException.ErrorType;
import exception.MeeBotException;
import parser.RecurrenceParser;
import parser.commandargs.StringTokenizer;
import parser.datetime.DateTimeParser;
import parser.datetime.ParsedDateTime;
import parser.json.SimpleJsonObject;
import task.DeadlineTask;
import task.Recurrence;
import task.Task;

import java.util.regex.Pattern;

public class DeadlineCreator implements TaskCreator {

    /**
     * Regular expression pattern to parse deadline command input.
     *
     * @apiNote Pattern generated with AI assistance for optimal matching
     */
    private static final Pattern DEADLINE_PATTERN = Pattern.compile(
            "(.+?)\\s*/by\\s*(.+)",
            Pattern.CASE_INSENSITIVE
    );

    @Override
    public Task createFromArgs(String args) throws MeeBotException {
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

    @Override
    public Task createFromJson(SimpleJsonObject obj) throws MeeBotException {
        String desc = obj.requireNonEmpty("description");
        ParsedDateTime dl = DateTimeParser.parse(obj.requireNonEmpty("deadline"));
        Recurrence rec = RecurrenceParser.parse(obj, dl.dateTime().toLocalDate());
        return new DeadlineTask(desc, dl, rec);
    }
}
