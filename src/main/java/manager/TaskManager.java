package manager;

import commands.Command;
import enums.CommandType;
import exceptions.FileException;
import exceptions.GrootException;
import parser.userInputParser.UserInputParser;
import tasks.Task;
import ui.UserInteraction;

import java.util.AbstractMap;
import java.util.ArrayList;

/**
 * Manages all tasks to be done by the program
 */
public class TaskManager {
    private static final ArrayList<Task> tasklist = new ArrayList<>();

    /**
     * Initialise tasklist from tasklist file
     */
    public TaskManager() throws FileException {
        FileManager.createTasklistFile(); //create tasklist file if not exist

        clearTasklist(); //ensure tasklist is empty before updating with file content

        tasklist.addAll(FileManager.readFile()); //load tasklist content
    }

    /**
     * clear all contents in tasklist. Used when file is corrupted and initialisation
     */
    public static void clearTasklist() { //clear tasklist for initial file read and if error in parsing
        tasklist.clear();
    }

    /**
     * Getter function to get tasklist
     *
     * @return tasklist
     */
    public static ArrayList<Task> getTasklist() {
        return tasklist;
    }

    /**
     * Manage task given and update tasklist file after each operation
     *
     * @param userInput input given by user
     */
    public void manageTask(String userInput) {
        assert userInput != null;

        try {
            AbstractMap.SimpleEntry<CommandType, ArrayList<String>> commandLine =
                    UserInputParser.parseUserInput(userInput);
            Command cmd = Command.createCommand(commandLine, tasklist);

            if (cmd == null) { //No command given
                return;
            }

            cmd.execute();

            FileManager.saveFile(tasklist);
        } catch (GrootException e) {
            UserInteraction.printMessage(e.getMessage());
        }
    }
}
