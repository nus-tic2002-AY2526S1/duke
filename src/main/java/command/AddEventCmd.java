package command;

import exception.InvalidDateTimeException;
import exception.InvalidTaskFormatException;
import exception.InvalidTaskOperationException;
import exception.MeeBotException;
import manager.TaskManager;
import message.Message;
import message.TaskAddedMessage;
import task.EventTask;
import task.Task;
import task.factory.EventCreator;
import task.factory.TaskCreator;

/**
 * Command to add a new event task with a time period and optional recurrence.
 *
 * @see TaskManager#addTask(Task)
 */
public class AddEventCmd extends BaseTaskCommand {
    private final TaskCreator creator = new EventCreator();

    public AddEventCmd(TaskManager taskManager, String args) {
        super(taskManager, args);
    }

    /**
     * Executes the event task creation command.
     * <p>
     * Parses user input in the format {@code "description /from dateTime /to dateTime [recurrence]"}
     * to extract the task description, date/time and recurrence pattern.
     * Creates a new {@link EventTask}, and adds it to the {@link TaskManager}.
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

        Task event = creator.createFromArgs(args);
        boolean wasSorted = taskManager.isSorted();
        taskManager.addTask(event);
        return new TaskAddedMessage(event, taskManager, wasSorted);
    }

    @Override
    protected CommandType getCommandType() {
        return CommandType.EVENT;
    }

    @Override
    protected boolean requiresNonEmptyList() {
        return false;
    }
}
