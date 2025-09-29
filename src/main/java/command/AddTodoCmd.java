package command;

import common.ErrorMessage;
import exception.MeeBotException;
import manager.TaskManager;
import message.Message;
import message.TaskAddedMessage;
import task.Task;
import task.TodoTask;
import task.factory.TaskCreator;
import task.factory.TodoCreator;

/**
 * Command to add a todo without any date constraints or recurrence.
 *
 * @see TaskManager#addTask(Task)
 */
public class AddTodoCmd extends BaseTaskCommand {
    private final TaskCreator creator = new TodoCreator();

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
            Task todo = creator.createFromArgs(args);
            boolean wasSorted = taskManager.isSorted();
            taskManager.addTask(todo);
            return new TaskAddedMessage(todo, taskManager, wasSorted);
        } catch (MeeBotException e) {
            return e.toErrorMessage();
        }
    }
}
