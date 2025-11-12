package storage;

import java.time.LocalDateTime;
import java.util.List;

import common.Priority;
import task.Deadline;
import task.Event;
import task.Task;
import task.TaskList;
import task.Todo;

/**
 * Decodes data from storage file into TaskList.
 */
public class DataDecoder {

    private static Integer index = 1;

    /**
     * Decodes the list of encoded task strings into a TaskList.
     * @param encodedData list of encoded task strings
     * @return decoded TaskList
     */
    public static TaskList decodeData(List<String> encodedData) {
        TaskList tl = new TaskList();
        for (String encodedTask : encodedData) {
            tl.add(decodeTaskFromString(encodedTask));
        }
        return tl;
    }

    /**
     * Decodes a single encoded task string into a Task.
     * @param input encoded task string
     * @return decoded Task
     */
    private static Task decodeTaskFromString(String input) {
        Task t = null;
        String[] parts = input.split(" \\| ");
        String taskType = parts[0];
        boolean isDone = parts[1].equals("1");
        Priority priority = null;
        if (!parts[2].equals("NIL")) {
            priority = Priority.valueOf(parts[2]);
        }
        String description = parts[3];
        switch (taskType) {
        case "T":
            t = new Todo(description, index);
            break;
        case "D":
            String deadlineIso = parts[4];
            t = new Deadline(description, index, LocalDateTime.parse(deadlineIso));
            break;
        case "E":
            String fromDateIso = parts[4];
            String toDateIso = parts[5];
            t = new Event(description, index, LocalDateTime.parse(fromDateIso), LocalDateTime.parse(toDateIso));
            break;
        default:
            break;
        }
        if (priority != null) {
            t.setPriority(priority);
        }
        t.setStatus(isDone);
        index += 1;
        return t;
    }
}
