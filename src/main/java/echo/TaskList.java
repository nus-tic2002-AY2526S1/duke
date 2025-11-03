package echo;

import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task t) {
        tasks.add(t);
        System.out.println("___________________________________________");
        System.out.println(t.addedMessage(tasks.size()));
        System.out.println("___________________________________________");
    }

    public void deleteTask(int index) {
        System.out.println("___________________________________________");
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + tasks.get(index));
        tasks.remove(index);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        System.out.println("___________________________________________");
    }

    public void clearTasks() {
        tasks.clear();
        System.out.println("___________________________________________");
        System.out.println("All tasks have been removed.");
        System.out.println("___________________________________________");
    }

    public Task getTask(int index) {
        return tasks.get(index);
    }

    public int size() {
        return tasks.size();
    }

    public ArrayList<Task> getAll() {
        return tasks;
    }

    public void listTasks() {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }
}
