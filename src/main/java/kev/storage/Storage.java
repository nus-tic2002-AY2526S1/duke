package kev.storage;

import kev.task.*;
import kev.exception.KevException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * storage component of the application that handles loading and saving tasks.
 */
public class Storage {

    private String filePath;

    /**
     * creates a Storage instance for loading and saving tasks from a specified file.
     *
     * @param filePath The path of the file to load/save tasks.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * loads the tasks from the specified file.
     * if the file does not exist, an empty list is returned.
     *
     * @return A list of tasks loaded from the file.
     * @throws KevException If there is an error while reading the file.
     * */
    public ArrayList<Task> loadTasks() throws KevException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            return tasks;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" \\| ");
                String type = parts[0];
                boolean isDone = parts[1].equals("1");
                String desc = parts[2];

                switch (type) {
                    case "T":
                        Task todo = new Todo(desc);
                        if (isDone) todo.markAsDone();
                        tasks.add(todo);
                        break;
                    case "D":
                        if (parts.length < 4) throw new KevException("Invalid file format for deadline");
                        Task deadline = new Deadline(desc, parts[3]);
                        if (isDone) deadline.markAsDone();
                        tasks.add(deadline);
                        break;
                    case "E":
                        if (parts.length < 4) throw new KevException("Invalid file format for event");

                        String[] dateTimes = parts[3].split(" "); // ["2025-11-16","14:00","2025-11-16","16:00"]
                        if (dateTimes.length != 4) throw new KevException("Invalid date/time format for event");
                        Task event = new Event(desc,
                                dateTimes[0], dateTimes[1], // start date/time
                                dateTimes[2], dateTimes[3]  // end date/time
                        );
                        if (isDone) event.markAsDone();
                        tasks.add(event);
                        break;

                }
            }
        } catch (IOException e) {
            throw new KevException("Failed to read file: " + e.getMessage());
        }

        return tasks;
    }

    /**
     * saves the list of tasks to the specified file.
     *
     * @param tasks The list of tasks to save.
     * @throws IOException If there is an error while saving the tasks.
     */
    public void saveTasks(List<Task> tasks) throws IOException {
        File file = new File(filePath);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        try (FileWriter fw = new FileWriter(file)) {
            for (Task task : tasks) {
                fw.write(task.toFileString() + System.lineSeparator());
            }
        }
    }
}