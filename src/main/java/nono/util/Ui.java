package nono.util;

import java.util.Scanner;

import nono.exception.UserInputException;
import nono.task.Task;

/**
 * Handles user interactions by displaying messages and reading input from the console.
 */
public class Ui {
    private Scanner scanner;

    /**
     * Constructs a Ui instance that reads user input from the console.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome message to the user.
     */
    public void showWelcome() {
        System.out.println("Hello! I'm NoNo");
        System.out.println("What can I do for you?\n");
    }

    /**
     * Displays the goodbye message when the program exits.
     */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Reads and returns the user's next input line.
     *
     * @return The command entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays an error message.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Displays a general message.
     *
     * @param message The message to display.
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays the list of all current tasks.
     *
     * @param tasks The task list to display.
     */
    public void showTaskList(TaskList tasks) {
        System.out.println("Current task list:\nTask No. Status Type Description");
        if (tasks.isEmpty()) {
            System.out.println("No task added yet\n");
        } else {
            for (int j = 0; j < tasks.size(); j++) {
                try {
                    System.out.println("     " + (j + 1) + ". " + tasks.getTask(j));
                } catch (UserInputException e) {
                    // This should never happen since we're iterating within bounds
                    System.out.println("     " + (j + 1) + ". [Error: Invalid task]");
                }
            }
            System.out.println();
        }
    }

    /**
     * Displays the result of marking or unmarking a task.
     *
     * @param task   The task that was marked or unmarked.
     * @param isMark True if the task was marked done, false if unmarked.
     */
    public void showMarkResult(Task task, boolean isMark) {
        String action = isMark ? "done" : "not done yet";
        System.out.println("Nice! The task is marked as " + action + ":");
        System.out.println(task + "\n");
    }

    /**
     * Displays the result of deleting a task.
     *
     * @param task           The deleted task.
     * @param remainingTasks The number of tasks remaining after deletion.
     */
    public void showDeleteResult(Task task, int remainingTasks) {
        System.out.println("OK! The task is deleted:");
        System.out.println(task);
        System.out.println("Now have " + remainingTasks + " task left\n");
    }

    /**
     * Displays the result of adding a new task.
     *
     * @param task The newly added task.
     */
    public void showAddResult(Task task) {
        System.out.println("New task added: " + task + "\n");
    }

    /**
     * Displays search results for the 'find' command.
     *
     * @param tasks   The task list to search within.
     * @param keyword The keyword used for searching.
     */
    public void showFindResults(TaskList tasks, String keyword) {
        System.out.println("Here are the matching tasks in your list:");
        int count = 0;
        for (int i = 0; i < tasks.size(); i++) {
            try {
                if (tasks.getTask(i).toString().toLowerCase().contains(keyword.toLowerCase())) {
                    System.out.println("     " + (++count) + ". " + tasks.getTask(i));
                }
            } catch (UserInputException e) {
                System.out.println("     [Error: Invalid task]");
            }
        }
        if (count == 0) {
            System.out.println("No matching tasks found");
        }
        System.out.println();
    }
}