package chatbot;

import chatbot.tasks.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public class Ui {
    private static final String LINE = "____________________________________________________________";

    public void showLine() {
        System.out.println(LINE);
    }

    public void showWelcome() {
        showLine();
        System.out.println(" Hello! I'm Qian 🌸");
        System.out.println(" What can I do for you?");
        showLine();
    }

    public void showGoodbye() {
        System.out.println(" Bye. Hope to see you again soon!");
    }

    public void showMessage(String msg) {
        showLine();
        System.out.println(" " + msg);
        showLine();
    }

    public void showError(String msg) {
        showLine();
        System.out.println(" ⚠️ OOPS!!! " + msg);
        showLine();
    }

    public void showLoadingError() {
        System.out.println("Error loading tasks from file. Starting with an empty list.");
    }

    public void showAddConfirmation(TaskList tasks) {
        chatbot.tasks.Task t = tasks.getLastTask();
        showLine();
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        showLine();
    }

    public void showFindResults(ArrayList<Task> matching) {
        showLine();
        if (matching.isEmpty()) {
            System.out.println("No matching tasks found.");
        } else {
            System.out.println("Here are the matching tasks in your list:");
            for (int i = 0; i < matching.size(); i++) {
                System.out.println((i + 1) + "." + matching.get(i));
            }
        }
        showLine();
    }

    public void showFreeTime(Optional<LocalDateTime[]> slot, int hours) {
        showLine();
        if (slot.isEmpty()) {
            System.out.println("Sorry, I couldn't find any free slots right now.");
        } else {
            LocalDateTime[] range = slot.get();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            System.out.println("Your next " + hours + "-hour free slot is on:");
            System.out.println(range[0].format(fmt) + " to " + range[1].format(fmt));
        }
        showLine();
    }

    public void showPriorityUpdated(Task task) {
        showLine();
        System.out.println("Got it! I've updated the task priority:");
        System.out.println("  " + task);
        showLine();
    }

}