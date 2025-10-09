import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public void addTodo(String description) {
        Todo newTask = new Todo(description);
        tasks.add(newTask);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + newTask.toString());
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }

    public void addDeadline(String input) {
        String[] deadlineParts = input.split(" /by ", 2);
        if (deadlineParts.length < 2) {
            System.out.println("Deadline format should be: deadline DESCRIPTION /by TIME");
            return;
        }
        Deadline newTask = new Deadline(deadlineParts[0], deadlineParts[1]);
        tasks.add(newTask);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + newTask.toString());
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }

    public void addEvent(String input) {
        String[] eventParts = input.split(" /from ", 2);
        if (eventParts.length < 2) {
            System.out.println("Event format should be: event DESCRIPTION /from START_TIME /to END_TIME");
            return;
        }

        String[] timeParts = eventParts[1].split(" /to ", 2);
        if (timeParts.length < 2) {
            System.out.println("Event format should be: event DESCRIPTION /from START_TIME /to END_TIME");
            return;
        }

        Event newTask = new Event(eventParts[0], timeParts[0], timeParts[1]);
        tasks.add(newTask);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + newTask.toString());
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }

    public void listTasks() {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i).toString());
        }
    }

    public void markTask(int taskNumber) {
        int index = taskNumber - 1;
        if (index >= 0 && index < tasks.size()) {
            tasks.get(index).markAsDone();
            System.out.println("Slay, you completed the task. Let's go!");
            System.out.println("  " + tasks.get(index).toString());
        } else {
            System.out.println("It's giving 'error'. That task number is sus.");
        }
    }

    public void unmarkTask(int taskNumber) {
        int index = taskNumber - 1;
        if (index >= 0 && index < tasks.size()) {
            tasks.get(index).markAsUndone();
            System.out.println("No cap, this one's un-marked. Back to the grind.");
            System.out.println("  " + tasks.get(index).toString());
        } else {
            System.out.println("It's giving 'error'. That task number is sus.");
        }
    }
}