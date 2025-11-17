// Uncomment this when need to run from Moonchester.java 
package moonchester_utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import moonchester_data.Task;
import moonchester_data.UserList;

public class MoonchesterStorage {

    private static final String filePath = "moonchester_data/Task_List.txt";
    private static final File taskListFile = new File(filePath);
    MoonchesterParser parser = new MoonchesterParser();
    /**
     * Static initialisation block that ensures the task list file and its
     * parent directory exist when the class is loaded. If the parent
     * directory does not exist, create a new one. If the task list file does
     * not exist, create a new one. Errors during file creation are printed.,
     */
    static {
        try {
            // Check for parent directory
            File parentDir = taskListFile.getParentFile();
            if (parentDir != null && parentDir.exists() == false) {
                parentDir.mkdirs();
            }

            // Create file if it doesn't exist
            if (taskListFile.exists() == false) {
                taskListFile.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("[!] Unable to create the task list file: " + e.getMessage());
        }
    }

    /**
     * Reads all lines from the task list file and returns them as an ArrayList of strings.
     * Each line represents a Task object. If the file is not found, an error
     * message is printed.
     *
     * @return An ArrayList containing each line from the task list file
     */
    public static ArrayList<String> readLines() {
        ArrayList<String> lines = new ArrayList<>();
        try (Scanner scanner = new Scanner(taskListFile)) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        } catch (java.io.FileNotFoundException e) {
            //System.out.println(System.getProperty("user.dir"));
            System.out.println("File not found: " + e.getMessage());
        }
        return lines;
    }

    /**
     * Updates the task list file with the current tasks from the userList.
     * This method overwrites the existing file content and writes each Task object
     * in its string format. Called when the user exits the application.
     *
     * @param finalUserList The UserList containing all current tasks
     * @throws IOException If an I/O error occurs while writing to the file
     */
    public void updateActiveTasks(UserList finalUserList) throws IOException {
        new FileWriter(filePath, false).close();
        for (Task item : finalUserList.getList()) {
            String convertedItem = parser.convertObjects(item);
            FileWriter fileWriter = new FileWriter(filePath, true);
            fileWriter.write(convertedItem + System.lineSeparator());
            fileWriter.close();
        }
    }

}
