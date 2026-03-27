package kev.ui;

import kev.task.Task;
import java.util.Scanner;

/**
 * handles all interactions with the user.
 * displays messages, reads commands, and prints task information.
 */
public class Ui {

    /** scanner used to read user input from standard input. */
    private Scanner sc;

    /** stores the last output message for GUI display. */
    private StringBuilder lastOutput;

    /** creates a new UI object and initializes the input scanner. */
    public Ui() {
        sc = new Scanner(System.in);
        lastOutput = new StringBuilder();
    }

    /**
     * reads the next user command from input.
     *
     * @return The trimmed user input string.
     */
    public String readCommand() {
        String input = sc.nextLine().trim();
        assert input != null : "Input read from scanner should not be null";
        return input;
    }

    /** displays the welcome message shown when the program starts. */
    public void showWelcome() {
        lastOutput.setLength(0);
        lastOutput.append("Hello! I'm Kev\nWhat can I do for you?");
        System.out.println(lastOutput);
    }

    /** prints the horizontal separator line. */
    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    /**
     * displays a generic message.
     *
     * @param message The message to display.
     */
    public void showMessage(String message) {
        assert message != null : "Message to show should not be null";
        lastOutput.append(message).append("\n");
        System.out.println(" " + message);
    }

    /** displays an error message when loading saved tasks fails. */
    public void showLoadingError() {
        System.out.println(" Error loading saved tasks.");
    }

    /**
     * displays confirmation message for adding a task.
     *
     * @param task The task added.
     * @param size The new total number of tasks.
     */
    public void showTaskAdded(Task task, int size) {
        assert task != null : "Task to add should not be null";
        String msg = "Got you! I've added this task:\n  " + task + "\nNow you have " + size + " tasks in the list.";
        showMessage(msg);
    }

    /**
     * displays confirmation message for deleting a task.
     *
     * @param task The task deleted.
     * @param size The new total number of tasks.
     */
    public void showTaskDeleted(Task task, int size) {
        assert task != null : "Task to delete should not be null";
        String msg = "Sure thing! I've removed this task:\n  " + task + "\nNow you have " + size + " tasks in the list.";
        showMessage(msg);
    }

    /**
     * displays confirmation message for marking a task as done.
     *
     * @param task The task that was marked done.
     */
    public void showTaskMarked(Task task) {
        assert task != null : "Task to mark should not be null";
        showMessage("OK, I've marked this task as done:\n  " + task);
    }

    /**
     * displays confirmation message for marking a task as not done.
     *
     * @param task The task that was unmarked.
     */
    public void showTaskUnmarked(Task task) {
        assert task != null : "Task to unmark should not be null";
        showMessage("OK, I've marked this task as not done yet:\n  " + task);
    }

    /**
     * displays error message.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        assert message != null : "Error message should not be null";
        showMessage(message);
    }

    /**
     * returns the last message displayed.
     * Used by GUI to show the latest response from Kev.
     *
     * @return last output message
     */
    public String getLastOutput() {
            return lastOutput.toString().trim();
    }

    /**
     * clears the last output before executing new command.
     */
    public void clearLastOutput() {
        lastOutput.setLength(0);
    }
}



