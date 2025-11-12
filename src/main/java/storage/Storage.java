package storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static common.Messages.ERROR_INVALID_STORAGE;
import task.TaskList;

/**
 * Handles loading and saving of tasks to storage.
 */
public class Storage {

    public static final String DEFAULT_STORAGE_PATH = "data.txt";
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public static boolean isValid(File filePath) {
        return (filePath.toString().endsWith(".txt"));
    }

    /**
     * Loads the tasks from storage file.
     *
     * @return TaskList loaded from storage
     * @throws IOException if reading the file fails
     */
    public TaskList load() throws IOException {
        return DataDecoder.decodeData(Files.readAllLines(Paths.get(filePath)));
    }

    /**
     * Saves the tasks to storage file.
     *
     * @param tasks TaskList to be saved
     * @throws IOException if writing to the file fails
     */
    public void save(TaskList tasks) throws IOException {
        File f = new File(filePath);
        String encodedData = DataEncoder.encodeData(tasks);
        FileWriter fw = new FileWriter(f);
        fw.write(encodedData);
        fw.close();
    }

    /**
     * Initialises the storage file.
     *
     * @throws IOException if file operations fail
     * @throws InvalidStoragePathException if the storage path is invalid
     */
    public void initFile() throws IOException, InvalidStoragePathException {
        File f = new File(filePath);
        if (!isValid(f)) {
            throw new InvalidStoragePathException(ERROR_INVALID_STORAGE);
        }

        //create new file if doesnt exist
        if (!f.exists()) {
            String[] pathBreakDown = filePath.split("/");
            String fileName = pathBreakDown[pathBreakDown.length - 1];
            String dirString = "";
            for (int i = 0; i < pathBreakDown.length - 1; i += 1) {
                dirString += pathBreakDown[i] + "/";
            }
            File dir = new File(dirString);
            dir.mkdirs();
            File newFile = new File(dir, fileName);
            newFile.createNewFile();
        }
    }

    /**
     * Exception thrown for invalid storage file paths.
     */
    public static class InvalidStoragePathException extends Exception {

        public InvalidStoragePathException(String message) {
            super(message);
        }
    }
}
