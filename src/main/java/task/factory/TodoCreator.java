package task.factory;

import exception.InvalidTaskFormatException.ErrorType;
import exception.MeeBotException;
import parser.commandargs.StringTokenizer;
import parser.json.SimpleJsonObject;
import task.Recurrence;
import task.Task;
import task.TodoTask;

import java.util.regex.Pattern;

public class TodoCreator implements TaskCreator {

    /**
     * Simple capture-all regular expression pattern since todo tasks have minimal parsing
     * requirements. Unlike other task commands, todo tasks have no special syntax or delimiters.
     */
    private static final Pattern TODO_PATTERN = Pattern.compile("(.+)");

    @Override
    public Task createFromArgs(String args) throws MeeBotException {
        String[] tokens = StringTokenizer.tokenize(
                args, TODO_PATTERN, 1, null);
        if (!"none".equalsIgnoreCase(tokens[tokens.length - 1])) {
            throw ErrorType.TODO.createException();
        }
        return new TodoTask(tokens[0], Recurrence.none(null));
    }

    @Override
    public Task createFromJson(SimpleJsonObject obj) throws MeeBotException {
        String desc = obj.requireNonEmpty("description");
        return new TodoTask(desc, Recurrence.none(null));
    }
}
