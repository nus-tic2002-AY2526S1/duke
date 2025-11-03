package echo;

/**
 * Provides methods for parsing lines from the storage file into Task objects.
 */
public class Parser {

    /**
     * Parses a line from the storage file and returns the corresponding Task object.
     *
     * @param line The line from the storage file in format: Type | isDone | Description | Date(s)
     * @return The Task object represented by the line
     * @throws IllegalArgumentException If the task type is unknown
     */
    public static Task parseTask(String line) {
        String[] parts = line.split(" \\| ");
        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task = switch (type) {
            case "T" -> new Todo(description);
            case "D" -> new Deadline(description, parts[3].trim());
            case "E" -> new Event(description, parts[3].trim(), parts[4].trim());
            default -> throw new IllegalArgumentException("Unknown task type: " + type);
        };

        if (isDone) {
            task.markAsDone();
        }

        return task;
    }
}
