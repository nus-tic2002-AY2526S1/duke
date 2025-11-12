package ui;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static common.Messages.MESSAGE_ADDED;
import static common.Messages.MESSAGE_BYE;
import static common.Messages.MESSAGE_DELETED;
import static common.Messages.MESSAGE_ERROR;
import static common.Messages.MESSAGE_FOUND_TASKS;
import static common.Messages.MESSAGE_MARKED;
import static common.Messages.MESSAGE_PRIORITY_SET;
import static common.Messages.MESSAGE_SHOW_TASKLIST;
import static common.Messages.MESSAGE_UNMARKED;
import static common.Messages.MESSAGE_WELCOME;

/**
 * UI of the application.
 */
public class Ui {

    private static final String LINE = "____________________________________________________________";
    private final Scanner in;
    private final PrintStream out;

    /**
     * Constructor for Ui.
     *
     * @param in InputStream
     * @param out PrintStream
     */
    public Ui(InputStream in, PrintStream out) {
        this.in = new Scanner(in);
        this.out = out;
    }

    public Ui() {
        this(System.in, System.out);
    }

    /**
     * Shows one or more messages to the user, each encapsulated with lines.
     *
     * @param message Messages to display.
     */
    public void showToUser(String... message) {
        System.out.println(); //Ensure cursor is reset to prevent print issues
        out.println(LINE);
        for (String m : message) {
            out.println(m);
        }
        out.println(LINE);
    }

    /**
     * Prints welcome message on the start of the application.
     */
    public void showWelcomeMessage() {
        showToUser(MESSAGE_WELCOME);
    }

    /**
     * Prints bye message before exit of the application.
     */
    public void showByeMessage() {
        showToUser(MESSAGE_BYE);
    }

    /**
     * Reads input from user.
     */
    public String getInput() {
        String input = in.nextLine();
        out.println();
        out.flush();
        return input;
    }

    /**
     * Displays messages whenever task has been added.
     *
     * @param taskString details of task; formatted in task objects.
     * @param size size of tasklist.
     */
    public String showAddMessage(String taskString, Integer size) {
        return (String.format(MESSAGE_ADDED, taskString, size));
    }

    /**
     * Displays list of tasks.
     *
     * @param taskListString details of tasks; formatted in taskList object.
     */
    public String showTaskListMessage(String taskListString) {
        return MESSAGE_SHOW_TASKLIST + "\n" + taskListString;
    }

    /**
     * Displays message to show that task is marked, after mark command has been
     * executed.
     *
     * @param taskString details of task; formatted in task objects.
     */
    public String showMarkMessage(String taskString) {
        return MESSAGE_MARKED + "\n" + taskString;
    }

    /**
     * Displays message to show that task is unmarked, after unmark command has
     * been executed.
     *
     * @param taskString details of task; formatted in task objects.
     */
    public String showUnmarkMessage(String taskString) {
        return MESSAGE_UNMARKED + "\n" + taskString;
    }

    /**
     * Displays error message according to which exception has been thrown
     *
     * @param errorMsg describes the error faced.
     */
    public String showErrorMessage(String errorMsg) {
        return MESSAGE_ERROR + "\n" + errorMsg;
    }

    /**
     * Displays message to show that task has been deleted, after delete command
     * has been executed.
     *
     * @param taskString details of task; formatted in task objects.
     * @param size size of tasklist.
     */
    public String showDeleteMessage(String taskString, Integer size) {
        return String.format(MESSAGE_DELETED, taskString, size);
    }

    /**
     * Displays message to show found tasks after find command has been
     * executed.
     *
     * @param foundTaskString details of found tasks
     */
    public String showFoundTasksMessage(String foundTaskString) {
        return MESSAGE_FOUND_TASKS + "\n" + foundTaskString;
    }

    /**
     * Displays message to show that priority has been set for a task.
     *
     * @param taskString details of task
     */
    public String showPriorityMessage(String taskString) {
        return String.format(MESSAGE_PRIORITY_SET, taskString);
    }
}
