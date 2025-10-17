package storage;

import java.util.List;

import task.Task;

public record TaskLoadResult(List<Task> tasks, int failedTaskCount, String errorLogFile) {

    public static TaskLoadResult empty() {
        return new TaskLoadResult(List.of(), 0, null);
    }

    private static TaskLoadResult current = TaskLoadResult.empty();

    public static void setCurrent(TaskLoadResult result) {
        current = java.util.Objects.requireNonNull(result);
    }

    public static TaskLoadResult current() {
        return current == null ? empty() : current;
    }

    // --- Convenience methods ---
    public int loadedTaskCount() {
        return tasks.size();
    }

    public boolean isFreshStart() {
        return loadedTaskCount() == 0 && failedTaskCount == 0;
    }

    public boolean allSuccessful() {
        return loadedTaskCount() > 0 && failedTaskCount == 0;
    }

    public boolean hasFailures() {
        return failedTaskCount > 0;
    }
}
