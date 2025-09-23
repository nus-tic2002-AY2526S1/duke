package command;

import exception.InvalidTaskFormatException.ErrorType;
import exception.MeeBotException;
import manager.TaskManager;
import message.ErrorMessage;
import message.Message;
import message.TaskAddedMessage;
import task.Recurrence;
import task.Task;
import task.TodoTask;
import util.RecurrenceParser;
import util.TokenizerUtil;

import java.util.regex.Pattern;

/**
 * Command to add a simple todo task without any date constraints.
 */
public class AddTodoCmd extends BaseTaskCommand {

    private static final Pattern TODO_PATTERN = Pattern.compile("(.+)");

    public AddTodoCmd(TaskManager taskManager, String args) {
        super(taskManager, args);
    }

    /**
     * Creates a todo task with the provided String input.
     *
     * @return a {@link TaskAddedMessage} on success, {@link ErrorMessage} if no description is provided
     */
    @Override
    public Message execute() {
        try {
            String[] tokens = TokenizerUtil.tokenize(
                    args, TODO_PATTERN, 1, ErrorType.MISSING_DESCRIPTION
            );
            Recurrence recurrence = RecurrenceParser.parse(
                    tokens[tokens.length - 1], ErrorType.RECURRENCE
            );

            Task todo = new TodoTask(tokens[0], recurrence);
            boolean wasSorted = taskManager.isSorted();
            taskManager.addTask(todo);
            return new TaskAddedMessage(todo, taskManager, wasSorted);
        } catch (MeeBotException e) {
            return e.toErrorMessage();
        }
    }
}
