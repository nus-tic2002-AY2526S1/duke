package command;

import exception.InvalidDateTimeException;
import exception.InvalidTaskFormatException;
import exception.InvalidTaskOperationException;
import exception.MeeBotException;
import manager.TaskManager;
import message.Message;
import message.TaskAddedMessage;
import task.DeadlineTask;
import task.Task;
import task.factory.DeadlineCreator;
import task.factory.TaskCreator;

/**
 * Command to add a new deadline task with a due date and optional recurrence.
 *
 * @see TaskManager#addTask(Task)
 */
public class AddDeadlineCmd extends BaseTaskCommand {
    private final TaskCreator creator = new DeadlineCreator();

    public AddDeadlineCmd(TaskManager taskManager, String args) {
        super(taskManager, args);
    }

    /**
     * Executes the deadline task creation command.
     * <p>
     * Parses user input in the format {@code "description /by dateTime [recurrence]"}
     * to extract the task description, date/time and recurrence pattern.
     * Creates a new {@link DeadlineTask}, and adds it to the {@link TaskManager}.
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
        Task deadline = creator.createFromArgs(args);
        boolean wasSorted = taskManager.isSorted();
        taskManager.addTask(deadline);
        return new TaskAddedMessage(deadline, taskManager, wasSorted);
    }

    @Override
    protected CommandType getCommandType() {
        return CommandType.DEADLINE;
    }

    @Override
    protected boolean requiresNonEmptyList() {
        return false;
    }
}
