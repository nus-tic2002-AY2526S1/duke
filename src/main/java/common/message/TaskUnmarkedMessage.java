package common.message;

import model.task.ReadOnlyTask;

/**
 * Message displayed when a completed task is marked as pending (unmarked).
 */
public class TaskUnmarkedMessage implements Message {
    private final ReadOnlyTask task;
    private final boolean isSorted;

    public TaskUnmarkedMessage(ReadOnlyTask task, boolean isSorted) {
        this.task = task;
        this.isSorted = isSorted;
    }

    @Override
    public String message() {
        String warning = """
                
                ⚠️ List may not be sorted now, sort again to put it in order.
                """;

        return String.format("""
                        '%s' is back to pending - just like when hawker uncle changes his mind about closing time.%s
                        """,
                task.toString(),
                isSorted
                        ? warning
                        : ""
        );
    }
}
