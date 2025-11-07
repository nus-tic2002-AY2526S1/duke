package common.message;

import model.TaskManager;
import model.task.Task;

/**
 * Confirmation message when a task is successfully added.
 * Shows the description of the newly added task and current task count.
 */
public class TaskAddedMessage implements Message {
    private final Task task;
    private final int taskCount;
    private final boolean showSortWarning;

    public TaskAddedMessage(Task task, TaskManager tm, boolean showSortWarning) {
        this.task = task;
        this.taskCount = tm.getTotalTasks();
        this.showSortWarning = showSortWarning;
    }

    @Override
    public String message() {
        String warning = """
                
                ⚠️ List may not be sorted now, sort again to put it in order.
                """;

        return String.format("""
                        Mee-rvelous! I've added that to your bowl... I mean, list!
                        '%s' is now one of the %d tasks simmering.%s
                        """,
                task.toString(), taskCount,
                showSortWarning
                        ? warning
                        : ""
        );
    }
}
