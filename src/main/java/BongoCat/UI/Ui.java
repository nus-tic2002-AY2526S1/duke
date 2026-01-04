package bongocat.ui;
import java.util.ArrayList;

import bongocat.tasks.Task;
import bongocat.tasks.TaskList;

import java.util.Scanner;

/**
 * Handles all user interface messages.
 */
public class Ui {

    private Scanner sc = new Scanner(System.in);

    public void showWelcome() {
        String logo = """
⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⣿⣶⣄⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⢀⣴⣿⣿⣿⣿⣿⣿⣿⣿⣿⣶⣦⣄⣀⡀⣠⣾⡇⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⣴⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⠀⠀⠀⠀
⠀⠀⠀⠀⢀⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⠿⢿⣿⣿⡇⠀⠀⠀⠀
⠀⣶⣿⣦⣜⣿⣿⣿⡟⠻⣿⣿⣿⣿⣿⣿⣿⡿⢿⡏⣴⣺⣦⣙⣿⣷⣄⠀⠀⠀
⠀⣯⡇⣻⣿⣿⣿⣿⣷⣾⣿⣬⣥⣭⣽⣿⣿⣧⣼⡇⣯⣇⣹⣿⣿⣿⣿⣧⠀⠀
⠀⠹⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠸⣿⣿⣿⣿⣿⣿⣿⣷
""";

        System.out.println("BONGO CAT ACTIVATED! This is DA BONGO CAT.\nHow may I annoy you today?\n" + logo);
    }

    public void showGoodbye() {
        System.out.println("GOODBYE.");
    }

    public String readCommand() {
        return sc.nextLine();
    }

    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    public void showError(String msg) {
        System.out.println(msg);
    }

    public void showList(TaskList tasks) {
        System.out.println("NAH YOUR TASKS:");
        int i = 1;
        for (Task t : tasks.getTasks()) {
            System.out.println(i + ". " + t);
            i++;
        }
    }

    public void showTaskAdded(Task t, int size) {
        System.out.println("Oki! I've added this task:");
        System.out.println("  " + t);
        System.out.println("Now you have " + size + " tasks in the list.");
    }

    /** Matches AddDeadlineCommand.showAdd */
    public void showAdd(Task t, int size) {
        System.out.println("Oki! I've added this task:");
        System.out.println("  " + t);
        System.out.println("Now you have " + size + " tasks in the list.");
    }

    /** Matches Duplicate detection */
    public void showDuplicate(Task t) {
        System.out.println("Bruh, this task already exists:");
        System.out.println("  " + t);
    }

    public void showTaskDeleted(Task t, int size) {
        System.out.println("Oki. I've removed this task:");
        System.out.println("  " + t);
        System.out.println("Now you have " + size + " tasks in the list.");
    }

    public void showTaskMarked(Task t) {
        System.out.println("NOICE. Marked this task as done:");
        System.out.println("  " + t);
    }

    public void showTaskUnmarked(Task t) {
        System.out.println("Boo~ I've marked this task as not done:");
        System.out.println("  " + t);
    }

    public void showSpaceLeft(int space) {
        System.out.println("Space left: " + space);
    }

    public void showFindResults(ArrayList<Task> results) {
        System.out.println("____________________________________________________________");
        System.out.println("Here are the matching tasks in your list:");

        if (results.isEmpty()) {
            System.out.println("  No matching tasks found.");
        } else {
            int index = 1;
            for (Task t : results) {
                System.out.println("  " + index + "." + t);
                index++;
            }
        }

        System.out.println("____________________________________________________________");
    }

}
