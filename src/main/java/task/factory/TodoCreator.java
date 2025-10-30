package task.factory;

import java.util.regex.Pattern;

import exception.FileContentException;
import exception.InvalidTaskFormatException;
import exception.InvalidTaskFormatException.ErrorType;
import parser.commandargs.ArgTokenizer;
import parser.json.SimpleJsonObject;
import task.Recurrence;
import task.Task;
import task.TodoTask;

/**
 * Simple capture-all regular expression pattern since todo tasks have minimal parsing
 * requirements. Unlike other task commands, todo tasks have no special syntax or delimiters.
 */
public final class TodoCreator implements TaskCreator {

    /**
     * Creates a todo task from user command arguments.
     *
     * @param args the command arguments in format {@code description}
     * @return a new {@link TodoTask} instance
     * @throws InvalidTaskFormatException if the input doesn't match the expected format
     */
    @Override
    public Task createFromArgs(String args) throws InvalidTaskFormatException {

        /* Pattern generated with AI assistance for optimal matching */
        final Pattern TODO_PATTERN = Pattern.compile("(.+)");
        String[] tokens = ArgTokenizer.tokenize(
                args, TODO_PATTERN, 1, null);

        if (!"none".equalsIgnoreCase(tokens[tokens.length - 1])) {
            throw ErrorType.TODO.createException();
        }

        assert tokens[0] != null && !tokens[0].isBlank() : "Description must not be null or empty";
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
        assert desc != null && !desc.isEmpty() : "Description must not be null or empty";
        return new TodoTask(desc, Recurrence.none(null));
    }
}
