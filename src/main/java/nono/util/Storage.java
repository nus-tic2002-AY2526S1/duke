package nono.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import nono.task.Deadline;
import nono.task.Event;
import nono.task.Task;
import nono.task.ToDo;

/**
 * Handles saving and loading tasks from a local file for data persistence.
 */
public class Storage {
    private final String filePath;

    /**
     * Creates a Storage instance for a specific file path.
     *
     * @param filePath The path of the file used for storing tasks.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the data file.
     *
     * @return A list of tasks loaded from the file.
     */
    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                return tasks;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" \\| ");
                String type = parts[0];
                boolean isDone = parts[1].equals("1");
                Task task = null;

                switch (type) {
                case "T":
                    task = new ToDo(parts[2]);
                    break;
                case "D":
                    task = new Deadline(parts[2], parts[3]);
                    break;
                case "E":
                    task = new Event(parts[2], parts[3], parts[4]);
                    break;
                }

                if (task != null) {
                    task.markAsDone(isDone);
                    tasks.add(task);
                }
            }
            reader.close();

        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Saves all tasks to the data file.
     *
     * @param tasks The list of tasks to save.
     */
    public void save(ArrayList<Task> tasks) {
        try {
            FileWriter writer = new FileWriter(filePath);
            for (Task t : tasks) {
                writer.write(t.toDataString() + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}