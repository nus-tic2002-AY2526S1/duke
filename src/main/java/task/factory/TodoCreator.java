package task.factory;

import java.util.regex.Pattern;

import exception.FileContentException;
import exception.InvalidTaskFormatException;
import exception.InvalidTaskFormatException.ErrorType;
import parser.commandargs.StringTokenizer;
import parser.json.SimpleJsonObject;
import task.Recurrence;
import task.Task;
import task.TodoTask;

public class TodoCreator implements TaskCreator {

    /**
     * Simple capture-all regular expression pattern since todo tasks have minimal parsing
     * requirements. Unlike other task commands, todo tasks have no special syntax or delimiters.
     */
    private static final Pattern TODO_PATTERN = Pattern.compile("(.+)");

    /**
     * Creates a todo task from user command arguments.
     *
     * @param args the command arguments in format {@code description}
     * @return a new {@link TodoTask} instance
     * @throws InvalidTaskFormatException if the input doesn't match the expected format
     */
    @Override
    public Task createFromArgs(String args) throws InvalidTaskFormatException {
        String[] tokens = StringTokenizer.tokenize(
                args, TODO_PATTERN, 1, null);
        if (!"none".equalsIgnoreCase(tokens[tokens.length - 1])) {
            throw ErrorType.TODO.createException();
        }
        return new TodoTask(tokens[0], Recurrence.none(null));
    }

    /**
     * Creates a todo task from a JSON object representation.
     *
     * @param obj the JSON object containing todo task data
     * @return a new {@link TodoTask} instance
     * @throws FileContentException if required fields are missing or invalid
     */
    @Override
    public Task createFromJson(SimpleJsonObject obj) throws FileContentException {
        String desc = obj.requireNonEmpty("description");
        return new TodoTask(desc, Recurrence.none(null));
    }
}
