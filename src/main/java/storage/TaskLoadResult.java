package storage;

import java.util.List;

import model.task.Task;

/**
 * Result of a task loading operation from storage.
 * Tracks successfully loaded tasks  and failure count.
 * Maintains a singleton reference to the current load result for application-wide access.
 *
 * @param tasks           the list of successfully loaded tasks
 * @param failedTaskCount the number of tasks that failed to load
 * @param errorLogFile    the path to the error log file, or null if no errors occurred
 */
public record TaskLoadResult(List<Task> tasks, int failedTaskCount, String errorLogFile) {
    private static TaskLoadResult current = TaskLoadResult.empty();

    /**
     * Sets the current load result for application-wide access.
     *
     * @param result the load result to set as current
     * @throws NullPointerException if result is null
     */
    public static void setCurrent(TaskLoadResult result) {
        current = java.util.Objects.requireNonNull(result);
    }

    /**
     * Returns the current load result, or an empty result if none has been set.
     *
     * @return the current TaskLoadResult
     */
    public static TaskLoadResult current() {
        return current == null ? empty() : current;
    }

    // --- Convenience methods ---
    /**
     * Convenience method to return an empty load result with no tasks and no failures.
     */
    public static TaskLoadResult empty() {
        return new TaskLoadResult(List.of(), 0, null);
    }

    /**
     * Returns the number of successfully loaded tasks.
     *
     * @return the count of loaded tasks
     */
    public int loadedTaskCount() {
        return tasks.size();
    }

    /**
     * Checks if this represents a fresh start with no existing tasks or failures.
     *
     * @return true if no tasks were loaded and no failures occurred
     */
    public boolean isFreshStart() {
        return loadedTaskCount() == 0 && failedTaskCount == 0;
    }

    /**
     * Checks if all tasks were loaded successfully with no failures.
     *
     * @return true if at least one task was loaded and no failures occurred
     */
    public boolean allSuccessful() {
        return loadedTaskCount() > 0 && failedTaskCount == 0;
    }

    /**
     * Checks if any tasks failed to load.
     *
     * @return true if one or more tasks failed to load
     */
    public boolean hasFailures() {
        return failedTaskCount > 0;
    }
}
