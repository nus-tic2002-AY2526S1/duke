package moonchester_utils;

import java.util.ArrayList;

import moonchester_data.Deadline;
import moonchester_data.Event;
import moonchester_data.Task;
import moonchester_data.Todo;

public class MoonchesterParser {
    /**
     * Converts a list of strings read from a file into corresponding Task objects.
     * Each line is parsed and formatted into a Task using the formatObjects helper method.
     *
     * @param lines The list of strings representing tasks from storage
     * @return An ArrayList of Task objects corresponding to the input lines
     */
    public ArrayList<Task> retrieveObjects(ArrayList<String> lines) {
        ArrayList<Task> tasks = new ArrayList<>();
        for (String line : lines) {
            tasks.add(formatObjects(line));
        }
        return tasks;
    }

    /**
     * Converts a single line of text from storage into a corresponding Task object.
     * The line is expected to follow the format: "Type | Status | Description | [Dates]".
     * Supports Todo, Deadline, and Event tasks. The task's completion status is also set.
     *
     * @param line A string representing a task in the storage file format
     * @return A Task object corresponding to the input line
     * @throws IllegalArgumentException if the task type is unknown (likely not to happen)
     */
    private Task formatObjects(String line) {
        String[] parts = line.split(" \\| ");
        String type = parts[0];
        boolean isDone = parts[1].trim().equals("1");
        String description = parts[2].trim();
        switch (type) {
            case "T":
                Todo new_todo = new Todo(description);
                new_todo.setStatus(isDone);
                return new_todo;
            case "D":
                // Deadline : description, deadline
                Deadline new_deadline = new Deadline(description, MoonchesterDate.convertToDateTime(parts[parts.length - 1].trim(), 0));
                new_deadline.setStatus(isDone);
                return new_deadline;
            case "E":
                // Event : description, from, to
                Event new_event = new Event(description, MoonchesterDate.convertToDateTime(parts[3].trim(), 0), MoonchesterDate.convertToDateTime(parts[parts.length - 1].trim(), 0));
                new_event.setStatus(isDone);
                return new_event;
            default:
                throw new IllegalArgumentException("[!] Unknown task type: " + type);
        }
    }
    

    /**
     * Converts a Task object into a string representation for
     * saving to a file. The string format conists of the task type, completion
     * status, description, and relevant dates for Deadline and Event tasks.
     *
     * @param task The Task object to convert
     * @return A string representing the task in storage file format
     */
    public String convertObjects(Task task) {
        int status = 0;
        if (task.getStatusIcon() == "X") {
            status = 1;
        }
        if (task instanceof Todo) {
            // Format Todo
            return "T" +" | "+ status + " | " + task.getDescription();
        }

        if (task instanceof Deadline) {
            // Format Deadline
            Deadline deadlineTask = (Deadline) task;
            return "D" +" | "+ status + " | " + deadlineTask.getDescription() + " | " + MoonchesterDate.saveDateFormat(deadlineTask.getByObject());
        }

        if (task instanceof Event) {
            // Format Event
            Event eventTask = (Event) task;
            return "E" + " | " + status + " | " + eventTask.getDescription() + " | " + MoonchesterDate.saveDateFormat(eventTask.getFromObject()) + " | " + MoonchesterDate.saveDateFormat(eventTask.getToObject());
        }
        
        return "";
    }

}
