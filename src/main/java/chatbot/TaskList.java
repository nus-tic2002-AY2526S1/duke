package chatbot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import chatbot.tasks.*;

public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a new task into the task list.
     * @param task Task to be added
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    public void deleteTask(int index) throws QianException {
        if (index < 0 || index >= tasks.size())
            throw new QianException("That task number doesn't exist!");
        Task removed = tasks.remove(index);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + removed);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Marks a task as done.
     * @param index Index of task in the list
     * @throws QianException If index is invalid
     */
    public void markTask(int index) throws QianException {
        if (index < 0 || index >= tasks.size()) {
            throw new QianException("That task number doesn't exist!");
        }

        assert index >= 0 && index < tasks.size() : "Index should be within tasks list bounds";

        Task t = tasks.get(index);
        assert t != null : "Task retrieved from list should not be null";

        t.markAsDone();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + t);
    }

    public void unmarkTask(int index) throws QianException {
        if (index < 0 || index >= tasks.size()) {
            throw new QianException("That task number doesn't exist!");
        }

        assert index >= 0 && index < tasks.size() : "Index should be within tasks list bounds";

        Task t = tasks.get(index);
        assert t != null : "Task retrieved from list should not be null";

        t.markAsNotDone();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + t);
    }

    public void printTasks() {
        if (tasks.isEmpty()) {
            System.out.println("Your list is empty!");
        } else {
            System.out.println("Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + "." + tasks.get(i));
            }
        }
    }

    public Task getLastTask() {
        return tasks.get(tasks.size() - 1);
    }

    public int size() {
        return tasks.size();
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public ArrayList<Task> findTasks(String keyword) {
        ArrayList<Task> matching = new ArrayList<>();
        for (Task t : tasks) {
            if (t.description.toLowerCase().contains(keyword.toLowerCase())) {
                matching.add(t);
            }
        }
        return matching;
    }

    public Optional<LocalDateTime[]> findFreeSlot(int hours) {
        ArrayList<LocalDateTime[]> busy = new ArrayList<>();

        // collect all busy periods
        for (Task t : tasks) {
            t.getBusyPeriod().ifPresent(busy::add);
        }

        // if no busy events, free anytime
        if (busy.isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            return Optional.of(new LocalDateTime[]{now, now.plusHours(hours)});
        }

        // sort by start time
        busy.sort((a, b) -> a[0].compareTo(b[0]));

        // start checking from now
        LocalDateTime cursor = LocalDateTime.now();

        for (LocalDateTime[] period : busy) {
            if (cursor.plusHours(hours).isBefore(period[0])) {
                // found gap before this busy period
                return Optional.of(new LocalDateTime[]{cursor, cursor.plusHours(hours)});
            }
            // move cursor to end of current busy block
            if (cursor.isBefore(period[1])) {
                cursor = period[1];
            }
        }

        // if no gap before last busy period, free after it
        return Optional.of(new LocalDateTime[]{cursor, cursor.plusHours(hours)});
    }

    public Task updatePriority(int taskIndex, int priority) throws QianException {
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new QianException("Task number is out of range!");
        }

        assert taskIndex >= 0 && taskIndex < tasks.size() : "Task index must be valid";

        Task task = tasks.get(taskIndex);
        assert task != null : "Task should not be null when updating priority";

        task.setPriority(priority);
        return task;
    }

}