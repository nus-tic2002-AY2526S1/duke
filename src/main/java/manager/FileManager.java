package manager;

import exceptions.FileException;
import parser.fileParser.FileParser;
import tasks.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Manages all functions to be used for file access
 */
public class FileManager {
    private static File folder = new File("data");
    private static File file = new File(folder, "tasklist.txt");

    /**
     * For testing only
     **/
    protected static void setFolderAndFile(File newFolder, File newFile) {
        folder = newFolder;
        file = newFile;
    }

    /**
     * Creates directory and file if it does not exist
     *
     * @throws FileException if directory and/or file cannot be created
     */
    protected static void createTasklistFile() throws FileException {
        createFolder();
        createFile();
    }

    private static void createFolder() throws FileException {
        boolean folderExists = folder.exists();

        if (!folderExists) {
            boolean folderCreated = folder.mkdir();

            if (!folderCreated) {
                throw new FileException.UnableToCreateDirectoryException();
            }
        }
    }

    private static void createFile() throws FileException {
        boolean fileExists = file.exists();

        try {
            if (!fileExists) {
                boolean fileCreated = file.createNewFile();
                if (!fileCreated) {
                    throw new FileException.UnableToCreateFileException();
                }
            }
        } catch (IOException e) {
            throw new FileException.UnableToCreateFileException();
        }
    }

    /**
     * Populate tasklist of program with file content
     *
     * @return populated tasklist
     * @throws FileException if there are any errors during populating
     */
    protected static ArrayList<Task> readFile() throws FileException {
        try (Scanner scanTasklist = new Scanner(file)) {
            ArrayList<Task> tasklist = new ArrayList<>();

            while (scanTasklist.hasNextLine()) {
                String line = scanTasklist.nextLine();

                if (line.isBlank()) {
                    break;
                }

                Task task = FileParser.parseTaskFile(line);
                tasklist.add(task);
            }

            return tasklist;
        } catch (FileNotFoundException e) {
            throw new FileException.FileDoesNotExistException();
        }
    }

    /**
     * Update file with tasklist content in program
     *
     * @param tasklist tasklist of program
     * @throws FileException if there are any errors in updating
     */
    public static void saveFile(ArrayList<Task> tasklist) throws FileException {
        assert tasklist != null;

        if (tasklist.isEmpty()) {
            return;
        }

        try (FileWriter fileWriter = new FileWriter(file)) {
            for (Task task : tasklist) {
                fileWriter.write(task.toString());
                fileWriter.write(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new FileException.UnableToWriteFileException();
        }
    }

    /**
     * Empty the tasklist file when file is corrupted
     *
     * @throws FileException if it cannot empty the file
     */
    public static void emptyFile() throws FileException { //empty contents of file
        try{
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.close();
        }catch (IOException e) {
            throw new FileException.UnableToWriteFileException();
        }
    }
}
