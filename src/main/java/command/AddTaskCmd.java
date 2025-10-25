package command;

import java.util.Objects;

import exception.InvalidDateTimeException;
import exception.InvalidTaskFormatException;
import exception.InvalidTaskOperationException;
import exception.MeeBotException;
import manager.TaskManager;
import message.Message;
import message.TaskAddedMessage;
import task.Task;
import task.factory.TaskCreator;

/**
 * This command delegates task creation to a {@link TaskCreator} implementation,
 * allowing different subclasses to handle different task types (e.g., todos, deadlines, events)
 * with their own specific input formats and validation rules.
 * <p>
 * Subclasses should provide a concrete {@link TaskCreator} that defines how to parse
 * the command arguments and construct the appropriate task type.
 *
 * @see TaskCreator
 * @see BaseTaskCommand
 */
public abstract class AddTaskCmd extends BaseTaskCommand {
    private final TaskCreator creator;

    protected AddTaskCmd(TaskManager taskManager, String args, TaskCreator creator) {
        super(taskManager, args);
        this.creator = Objects.requireNonNull(creator, "creator must not be null");
    }

    /**
     * Executes the task creation command by parsing the user input, creating a task,
     * and adding it to the {@link TaskManager}.
     * <p>
     * The specific input format and task type are determined by the {@link TaskCreator}
     * implementation provided to this command.
     *
     * @return {@link TaskAddedMessage} on successful task creation
     * @throws InvalidTaskFormatException    if the input format is invalid
     * @throws InvalidDateTimeException      if the date/time cannot be parsed
     * @throws InvalidTaskOperationException if the task creation fails
     * @implNote Captures task manager's sorted state before modification to provide
     *         accurate context in return messages
     */
    @Override
    public Message executes() throws MeeBotException {
        assert args != null : "Arguments must not be null";

        Task task = creator.createFromArgs(args);
        boolean wasSorted = taskManager.isSorted();
        taskManager.addTask(task);

        return new TaskAddedMessage(task, taskManager, wasSorted);
    }
}
