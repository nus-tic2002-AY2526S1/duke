package model.task;

/**
 * Task type enumeration with single-character prefixes for display.
 */
public enum TaskType {
    TODO("todo", "T"),
    DEADLINE("deadline", "D"),
    EVENT("event", "E");

    private final String keyword, taskPrefix;

    TaskType(String keyword, String taskPrefix) {
        this.keyword = keyword;
        this.taskPrefix = taskPrefix;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getTaskPrefix() {
        return taskPrefix;
    }
}
