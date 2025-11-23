package storage;

import java.util.List;

import model.StorageManager;
import model.task.ReadOnlyTask;
import model.task.Recurrence;

public class TaskSerializer {

    private TaskSerializer() {
    }

    /**
     * Utility class to serialize Task objects into JSON string representations.
     * <p>
     * The JSON format includes common task fields (type, done status, description)
     * as well as task-specific fields for different task types.
     *
     * @see StorageManager#saveTasks()
     */
    public static String tasksToJson(List<ReadOnlyTask> taskList) {
        assert taskList != null : "Task list must not be null";

        if (taskList.isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < taskList.size(); i++) {
            String taskJson = taskToJson(taskList.get(i));
            String indentedTask = taskJson.replaceAll("(?m)^", "  ");
            sb.append(indentedTask);

            // add comma between tasks
            if (i != taskList.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Converts a single task into a JSON string representation.
     */
    public static String taskToJson(ReadOnlyTask task) {
        Recurrence r = task.getRecurrence();

        return """
                {
                  "type": "%s",
                  "done": %s,
                  "description": "%s"%s,
                  "recurrence": {
                    "type": "%s",
                    "count": %d,
                    "anchorDate": "%s",
                    "recurrenceEnd": "%s"
                  }
                }
                """.formatted(
                task.getTaskType().getKeyword(),
                task.isDone(),
                task.getDescription().replace("\"", "\\\""),   //"description": "Read \"TIC\" textbook"
                task.toJsonFields(),
                r.type(),
                r.frequency(),
                r.anchorDate(),
                r.recurrenceEnd()
        );
    }
}
