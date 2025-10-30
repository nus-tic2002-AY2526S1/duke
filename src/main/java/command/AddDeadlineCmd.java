package command;

import manager.TaskManager;
import task.DeadlineTask;
import task.factory.DeadlineCreator;

/**
 * Command for adding deadline tasks to the task manager.
 * <p>
 * Delegates task creation to {@link DeadlineCreator}, which handles parsing
 * of deadline-specific input format and validation.
 *
 * @see DeadlineTask
 * @see DeadlineCreator
 * @see AddTaskCmd
 */
public final class AddDeadlineCmd extends AddTaskCmd {
    public AddDeadlineCmd(TaskManager taskManager, String args) {
        super(taskManager, args, new DeadlineCreator());
    }

    @Override
    protected CommandType getCommandType() {
        return CommandType.DEADLINE;
    }
}
