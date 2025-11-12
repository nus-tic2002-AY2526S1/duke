package storage;

import task.Deadline;
import task.Event;
import task.Task;
import task.TaskList;
import task.Todo;

/**
 * Encodes TaskList data into strings for storage.
 */
public class DataEncoder {

    private static Integer index = 1;

    /**
     * Encodes the TaskList into a string for storage.
     * @param tasks TaskList to be encoded
     * @return encoded string representation of TaskList
     */
    public static String encodeData(TaskList tasks) {
        String encodedData = "";
        for (Task task : tasks) {
            encodedData += encodeTask(task);
        }
        return (encodedData);
    }

    /**
     * Encodes a single Task into a string for storage.
     * @param task Task to be encoded
     * @return encoded string representation of the Task
     */
    private static String encodeTask(Task task) {
        String taskString = "";
        if (task instanceof Todo) {
            taskString = "T | ";
        } else if (task instanceof Deadline) {
            taskString = "D | ";
        } else if (task instanceof Event) {
            taskString = "E | ";
        }

        if (task.getStatus()) {
            taskString += "1 | ";
        } else {
            taskString += "0 | ";
        }

        if (task.getPriority() != null) {
            taskString += task.getPriority().toString() + " | ";
        } else {
            taskString += "NIL | ";
        }

        taskString += task.getDescription();

        if (task instanceof Deadline) {
            taskString += " | " + ((Deadline) task).getDeadline();
        } else if (task instanceof Event) {
            taskString += " | " + ((Event) task).getFromDate() + " | " + ((Event) task).getToDate();
        }

        taskString += "\n";
        return taskString;
    }
}
