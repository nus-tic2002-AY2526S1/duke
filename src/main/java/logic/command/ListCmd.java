package logic.command;

import common.ErrorMessage;
import model.TaskManager;
import common.message.ListTaskMessage;
import common.message.Message;

/**
 * Command to display all tasks in MeeBot.
 */
public class ListCmd implements Command {
    private final TaskManager taskManager;

    public ListCmd(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public Message execute() {
        if (taskManager.isEmpty()) {
            return new ErrorMessage(ErrorMessage.EMPTY_LIST);
        }
        return new ListTaskMessage(taskManager);
    }
}
