package echo;

import java.io.*;
import java.util.ArrayList;

/**
 * Handles reading and writing tasks to the storage file.
 * Responsible for saving tasks in a consistent format and loading them back into memory.
 */
public class Storage {

    /** Path to the storage file */
    private String filepath;

    /**
     * Constructs a Storage object for the given file path.
     * Creates parent directories if they do not exist.
     *
     * @param filepath The path to the storage file
     */
    public Storage(String filepath) {
        this.filepath = filepath;
        File file = new File(filepath);
        file.getParentFile().mkdirs();
    }

    /**
     * Loads tasks from the storage file into an ArrayList.
     * Skips any lines that cannot be parsed.
     *
     * @return ArrayList of Task objects loaded from the file
     */
    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filepath);

        try {
            if (!file.exists()) {
                file.createNewFile();
                return tasks;
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    tasks.add(Parser.parseTask(line));
                } catch (Exception e) {
                    System.out.println("Skipping corrupted line: " + line);
                }
            }

        } catch (IOException e) {
            System.out.println("Error: Could not load tasks: " + e.getMessage());
        }

        return tasks;
    }

    /**
     * Saves the given list of tasks to the storage file.
     *
     * @param tasks ArrayList of Task objects to be saved
     */
    public void save(ArrayList<Task> tasks) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath))) {
            for (Task task : tasks) {
                bw.write(task.toSaveFormat());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error: Could not save tasks: " + e.getMessage());
        }
    }
}
