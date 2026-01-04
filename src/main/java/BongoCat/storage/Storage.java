package bongocat.storage;

import bongocat.tasks.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

/**
 * Stores List Data in SavedFiles/BongoCat.txt for recurring use
 */
public class Storage {
    private final Path filePath;

    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    /**
     * Saves all tasks to disk with assertions enabled for A-Assertions
     */
    public void save(ArrayList<Task> list) throws IOException {
        assert list != null : "Tasks list should never be null when saving";
        assert list.size() >= 0 : "Task size cannot be negative";

        Files.createDirectories(filePath.getParent());
        BufferedWriter writer = Files.newBufferedWriter(filePath);

        for (Task t : list) {
            writer.write(t.toSaveFormat());
            writer.newLine();
        }

        writer.close();
    }

    /**
     * Loads tasks from disk and returns them as an ArrayList
     */
    public ArrayList<Task> load() {
        ArrayList<Task> list = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return list;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Task t = parseLine(line);
                    list.add(t);
                } catch (Exception malformed) {
                    System.out.println("⚠ Corrupted line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("⚠ Error reading save file.");
        }

        return list;
    }

    /**
     * Parses a saved task line and reconstructs the Task object.
     */
    private Task parseLine(String line) {
        String[] parts = line.split("\\s*\\|\\s*");
        String type = parts[0];
        boolean isDone = parts[1].equals("[X]");
        String name = parts[2];

        switch (type) {
            case "T": {
                ToDo todo = new ToDo(name);
                if (isDone) todo.markDone();
                return todo;
            }

            case "D": {
                String by = parts[3];
                Deadline d = new Deadline(name, by);
                if (isDone) d.markDone();
                return d;
            }

            case "E": {
                String from = parts[3];
                String to = parts[4];
                Event e = new Event(name, from, to);
                if (isDone) e.markDone();
                return e;
            }

            default:
                throw new IllegalArgumentException("Unknown task type: " + type);
        }
    }
}
