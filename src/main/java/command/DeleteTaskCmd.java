package command;

import exception.InvalidTaskOperationException;
import manager.TaskManager;
import message.Message;
import message.TaskDeletedMessage;
import parser.commandargs.TaskIndexParser;
import task.ReadOnlyTask;

/**
 * Command to remove a task from the task list by its index number.
 * <p>
 * The index number refers to the task's position in the currently displayed list,
 * which may vary depending on the active sort order. The task is identified
 * by its position at the time this command executes, not by any permanent ID.
 *
 * @see TaskManager#deleteTask(int)
 */
public class DeleteTaskCmd extends BaseTaskCommand {

    public DeleteTaskCmd(TaskManager taskManager, String args) {
        super(taskManager, args);
    }

    /**
     * Validates user input and removes the specified task from the list.
     *
     * @return {@link TaskDeletedMessage} containing the deleted task and updated list size
     * @throws InvalidTaskOperationException if the task index is invalid or out of bounds
     */
    @Override
    public Message executes() throws InvalidTaskOperationException {
        int taskNumber = TaskIndexParser.parseTaskIndex(args, taskManager);
        ReadOnlyTask task = taskManager.getTask(taskNumber);
        taskManager.deleteTask(taskNumber);
        return new TaskDeletedMessage(task, taskManager);
    }

    @Override
    protected CommandType getCommandType() {
        return CommandType.DELETE;
    }
}
