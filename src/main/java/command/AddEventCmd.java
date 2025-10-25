package command;

import manager.TaskManager;
import task.EventTask;
import task.factory.EventCreator;

/**
 * Command for adding event tasks to the task manager.
 * <p>
 * Delegates task creation to {@link EventCreator}, which handles parsing
 * of event-specific input format and validation.
 *
 * @see EventTask
 * @see EventCreator
 * @see AddTaskCmd
 */
public final class AddEventCmd extends AddTaskCmd {
    public AddEventCmd(TaskManager taskManager, String args) {
        super(taskManager, args, new EventCreator());
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
