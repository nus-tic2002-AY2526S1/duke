package command;

import common.ErrorMessage;
import exception.MeeBotException;
import manager.TaskManager;
import message.Message;
import message.TaskDeletedMessage;
import parser.TaskIndexParser;
import task.Task;

/**
 * Command to remove a task from the task list by its index number.
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
     * @return {@link TaskDeletedMessage} on success, or
     *         {@link ErrorMessage} on invalid input or task not found
     */
    @Override
    public Message execute() {
        Message help = showHelpText(CommandType.DELETE);
        if (help != null) {
            return help;
        }
        try {
            int taskNumber = TaskIndexParser.parseTaskIndex(args, taskManager);
            Task task = taskManager.getTask(taskNumber);
            taskManager.deleteTask(taskNumber);
            return new TaskDeletedMessage(task, taskManager);
        } catch (MeeBotException e) {
            return e.toErrorMessage();
        }
    }
}
