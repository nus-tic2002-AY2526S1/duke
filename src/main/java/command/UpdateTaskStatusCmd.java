package command;

import common.ErrorMessage;
import exception.MeeBotException;
import manager.TaskManager;
import message.Message;
import message.TaskMarkedMessage;
import message.TaskUnmarkedMessage;
import parser.commandargs.TaskIndexParser;
import task.ReadOnlyTask;

/**
 * Command to update the completion status of a task by its index number.
 * <p>
 * This command can either mark a task as completed or mark it as pending,
 * depending on the {@code markDone} parameter provided during construction.
 * <p>
 * The index number refers to the task's position in the currently displayed list,
 * which may vary depending on the active sort order or filter. The task is identified
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
     *         {@link TaskUnmarkedMessage} on successful unmark, or
     *         {@link ErrorMessage} if validation fails or task doesn't exist
     */
    @Override
    public Message execute() {
        CommandType type = markDone ? CommandType.MARK : CommandType.UNMARK;
        Message help = showHelpText(type);
        if (help != null) return help;

        try {
            int taskNumber = TaskIndexParser.parseTaskIndex(args, taskManager);
            boolean wasSorted = taskManager.isSorted();
            ReadOnlyTask task;
            if (markDone) {
                taskManager.markTaskDone(taskNumber);
                task = taskManager.getTask(taskNumber);
                return new TaskMarkedMessage(task, wasSorted);
            } else {
                taskManager.unmarkTask(taskNumber);
                task = taskManager.getTask(taskNumber);
                return new TaskUnmarkedMessage(task, wasSorted);
            }
        } catch (MeeBotException e) {
            return e.toErrorMessage();
        }
    }
}
