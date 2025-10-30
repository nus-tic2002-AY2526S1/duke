package command;

import manager.TaskManager;
import task.TodoTask;
import task.factory.TodoCreator;

/**
 * Command to add a {@link TodoTask} for activities that need to be completed but
 * have no specific timing requirements. Delegates task creation to {@link TodoCreator}.
 *
 * @see TodoTask
 * @see TodoCreator
 * @see AddTaskCmd
 */
public final class AddTodoCmd extends AddTaskCmd {
    public AddTodoCmd(TaskManager taskManager, String args) {
        super(taskManager, args, new TodoCreator());
    }

    @Override
    protected CommandType getCommandType() {
        return CommandType.TODO;
    }
}
