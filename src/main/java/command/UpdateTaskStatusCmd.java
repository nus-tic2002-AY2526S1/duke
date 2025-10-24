package command;

import exception.InvalidTaskOperationException;
import manager.TaskManager;
import message.Message;
import message.TaskMarkedMessage;
import message.TaskUnmarkedMessage;
import parser.commandargs.TaskIndexParser;
import task.ReadOnlyTask;

/**
 * Command to update the completion status of a task by its index number.
 * <p>
 * This command toggles a task's completion state between done and not done,
 * depending on the {@code markDone} parameter provided during construction.
 * <p>
 * The index number refers to the task's position in the currently displayed list,
 * which may vary depending on the active sort order. The task is identified
 * by its position at the time this command executes, not by any permanent ID.
 *
 * @see TaskManager#markTaskDone(int)
 * @see TaskManager#unmarkTask(int)
 */
public class UpdateTaskStatusCmd extends BaseTaskCommand {
    private final boolean markDone;

    public UpdateTaskStatusCmd(TaskManager taskManager, String args, boolean markDone) {
        super(taskManager, args);
        this.markDone = markDone;
    }

    /**
     * Validates input, task existence, checks current state to prevent redundant
     * operations, and updates the completion status of the specified task.
     *
     * @return {@link TaskMarkedMessage} on successful mark done, or
     *         {@link TaskUnmarkedMessage} on successful unmark
     * @throws InvalidTaskOperationException if the task index is invalid, out of bounds
     *                                       or invalid task status
     */
    @Override
    public Message executes() throws InvalidTaskOperationException {
        assert args != null : "Arguments must not be null";

        int taskNumber = TaskIndexParser.parseTaskIndex(args);
        boolean wasSorted = taskManager.isSorted();
        if (markDone) {
            taskManager.markTaskDone(taskNumber);
        } else {
            taskManager.unmarkTask(taskNumber);
        }

        ReadOnlyTask task = taskManager.getTask(taskNumber);
        return markDone
                ? new TaskMarkedMessage(task, wasSorted)
                : new TaskUnmarkedMessage(task, wasSorted);
    }

    @Override
    protected CommandType getCommandType() {
        return markDone ? CommandType.MARK : CommandType.UNMARK;
    }
}
