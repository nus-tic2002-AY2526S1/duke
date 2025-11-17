package nerunerune.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;

import nerunerune.exception.NeruneruneException;
import nerunerune.parser.Parser;
import nerunerune.task.Task;

/**
 * Manages reading and writing tasks to persistent storage.
 * Handles file creation, loading tasks from file, saving tasks to file,
 * and recovering from corrupted storage files by archiving or deletion.
 */
public class Storage {
    private final String filepath;

    /**
     * Constructs a Storage handler with the specified file path.
     *
     * @param filepath the path to the local tasks storage file
     */
    public Storage(String filepath) {
        this.filepath = filepath;
    }

    /**
     * Creates a storage file and necessary parent directories if they don't exist.
     *
     * @param f the file to create
     * @throws NeruneruneException if file creation fails
     */
    public static void createStorageFile(File f) throws NeruneruneException {
        try {
            File parent = f.getParentFile();
            if (!parent.exists()) parent.mkdirs();
            if (!f.exists()) f.createNewFile();
            System.out.println("Created new storage file: " + f.getName());
        } catch (IOException e) {
            throw new NeruneruneException("An error occurred while creating storage file: " + e.getMessage());
        }
    }

    /**
     * Handles corrupted storage files by renaming them to an archive,
     * or deleting if renaming fails, then recreating the storage file and clearing the task list.
     *
     * @param f        the corrupted storage file
     * @param taskList the task list to clear after corruption
     * @throws NeruneruneException if file recreation fails
     */
    private static void handleCorruptedFile(File f, ArrayList<Task> taskList) throws NeruneruneException {
        File archiveFile = new File(f.getParent(), "tasksArchive.txt");

        try {
            Files.move(f.toPath(), archiveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Renamed corrupted file to: " + archiveFile.getName());
        } catch (IOException e) {
            System.out.println("Rename failed: " + e.getMessage());
            if (f.delete()) {
                System.out.println("Deleted corrupted file.");
            } else {
                System.out.println("Failed to delete corrupted file.");
            }
        }
        createStorageFile(f);
        taskList.clear(); // clear the task list as loading failed
    }

    /**
     * Loads tasks from storage file if it exists, or creates a new storage file otherwise.
     *
     * @param taskList the list to populate with loaded tasks
     * @throws NeruneruneException if creating or reading storage files fails
     * @throws IOException         if an IO error occurs reading the file
     */
    public void handleStorage(ArrayList<Task> taskList) throws NeruneruneException, IOException {
        File f = new File(filepath);
        if (f.exists()) {
            System.out.println("Storage file found. saved tasks loaded");
            readStorageFile(f, taskList);
        } else {
            createStorageFile(f);
        }
    }

    /**
     * Writes the given text content to the specified file.
     *
     * @param f         the file to write to
     * @param textToAdd the text content to write
     * @throws NeruneruneException if writing to file fails
     */
    public void writeStorageFile(File f, String textToAdd) throws NeruneruneException {
        try (FileWriter fw = new FileWriter(f)) { // close file automatically
            fw.write(textToAdd);
        } catch (IOException e) {
            throw new NeruneruneException("An error occurred while writing to the file: " + e.getMessage());
        }
    }


    /**
     * Saves the current task list to the storage file.
     *
     * @param taskList the list of tasks to save
     * @throws NeruneruneException if writing to file fails
     */
    public void saveTasksToStorage(ArrayList<Task> taskList) throws NeruneruneException {
        File f = new File(filepath);
        StringBuilder sb = new StringBuilder();
        for (Task task : taskList) {
            sb.append(task.toStorageString()).append(System.lineSeparator());
        }
        writeStorageFile(f, sb.toString());
    }

    /**
     * Reads tasks from the storage file and adds them to the task list.
     * On encountering corrupted lines, archives or deletes the file and clears the task list.
     *
     * @param f        storage file to read from
     * @param taskList task list to populate
     * @throws NeruneruneException if corrupted file handling fails
     * @throws IOException         if reading the file fails
     */
    public void readStorageFile(File f, ArrayList<Task> taskList) throws NeruneruneException, IOException {
        try (Scanner s = new Scanner(f)) { // close scanner after done
            while (s.hasNext()) {
                String line = s.nextLine().trim();
                try {
                    Task task = Parser.parseTaskLine(line);
                    taskList.add(task);
                } catch (IOException e) {
                    System.out.println("Storage file appears corrupted: " + e.getMessage());
                    handleCorruptedFile(f, taskList);
                    return;
                }
            }
        }

    }

}
