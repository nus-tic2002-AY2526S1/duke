package common.message;

import java.util.List;

import model.TaskManager;
import model.task.ReadOnlyTask;

/**
 * Dynamic task listing with 1-based numbering for user display.
 */
public class ListTaskMessage implements Message {
    private final TaskManager taskManager;

    public ListTaskMessage(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    /**
     * Returns a formatted numbered task list or empty list message if no tasks exist.
     */
    @Override
    public String message() {
        List<ReadOnlyTask> tasks = taskManager.getReadOnlyList();

        // Builds numbered task list with 1-based indexing
        StringBuilder content = new StringBuilder("Here's your current mee-x of responsibilities:\n");
        for (int i = 0; i < tasks.size(); i++) {
            ReadOnlyTask task = tasks.get(i);
            content.append(String.format("%d. %s\n", i + 1, task.toString()));
        }
        return content.toString().trim();
    }
}
