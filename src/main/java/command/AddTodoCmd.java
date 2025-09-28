package command;

import common.ErrorMessage;
import exception.InvalidTaskFormatException.ErrorType;
import exception.MeeBotException;
import manager.TaskManager;
import message.Message;
import message.TaskAddedMessage;
import parser.taskops.StringTokenizer;
import task.Recurrence;
import task.Task;
import task.TodoTask;

import java.util.regex.Pattern;

/**
 * Command to add a todo without any date constraints or recurrence.
 *
 * @see TaskManager#addTask(Task)
 */
public class AddTodoCmd extends BaseTaskCommand {

    /**
     * Simple capture-all regular expression pattern since todo tasks have minimal parsing
     * requirements. Unlike other task commands, todo tasks have no special syntax or delimiters.
     */
    private static final Pattern TODO_PATTERN = Pattern.compile("(.+)");

    public AddTodoCmd(TaskManager taskManager, String args) {
        super(taskManager, args);
    }

    /**
     * This command creates basic {@link TodoTask} instances for activities that need to be
     * completed but have no specific timing requirements. Unlike deadline and event tasks,
     * todo tasks cannot be configured with recurrence patterns.
     *
     * @return {@link TaskAddedMessage} on successful task creation, or
     *         {@link ErrorMessage} if no description provided or invalid recurrence specified
     * @apiNote Todos are designed for simple, timeless activities. Use {@code Deadline} or
     *         {@code Event} for time-sensitive tasks.
     */
    @Override
    public Message execute() {
        Message help = showHelpText(CommandType.TODO);
        if (help != null) return help;

        try {
            String[] tokens = StringTokenizer.tokenize(
                    args, TODO_PATTERN, 1, null
            );
            if (!"none".equalsIgnoreCase(tokens[tokens.length - 1])) {
                throw ErrorType.TODO.createException();
            }
            Recurrence recurrence = Recurrence.none(null);

            Task todo = new TodoTask(tokens[0], recurrence);
            boolean wasSorted = taskManager.isSorted();
            taskManager.addTask(todo);
            return new TaskAddedMessage(todo, taskManager, wasSorted);
        } catch (MeeBotException e) {
            return e.toErrorMessage();
        }
    }
}
