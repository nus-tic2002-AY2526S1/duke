package command;

import common.ErrorMessage;
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
     * to extract the task description, date/time and optional recurrence pattern.
     * Creates a new {@link EventTask}, and adds it to the {@link TaskManager}.
     *
     * @return {@link TaskAddedMessage} on successful task creation, or
     *         {@link ErrorMessage} on invalid command format or date parsing
     * @implNote Captures task manager's sorted state before modification to provide
     *         accurate context in return messages
     */
    @Override
    public Message execute() {
        Message help = showHelpText(CommandType.EVENT);
        if (help != null) return help;
        try {
            Task event = creator.createFromArgs(args);
            boolean wasSorted = taskManager.isSorted();
            taskManager.addTask(event);
            return new TaskAddedMessage(event, taskManager, wasSorted);
        } catch (MeeBotException e) {
            return e.toErrorMessage();
        }
    }
}
