import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public void addTodo(String description) {
        if (description == null || description.trim().isEmpty()) {
            System.out.println("Wait, what's the todo? Give me the details!");
            return;
        }

        try {
            Todo newTask = new Todo(description);
            tasks.add(newTask);
            System.out.println("🎉 Got it. I've added this task:");
            System.out.println("  " + newTask.toString());
            System.out.println("📊 Now you have " + tasks.size() + " tasks in the list.");
        } catch (Exception e) {
            System.out.println("❌ Oops! Couldn't add the todo. Let's try that again!");
        }
    }

    public void addDeadline(String input) {
        if (input == null || input.trim().isEmpty()) {
            System.out.println("Wait, what's the deadline? Give me the details!");
            return;
        }

        String[] deadlineParts = input.split(" /by ", 2);
        if (deadlineParts.length < 2) {
            System.out.println("⏰ Deadline format should be: deadline DESCRIPTION /by TIME");
            return;
        }

        if (deadlineParts[0].trim().isEmpty()) {
            System.out.println("Wait, what's the deadline description? Give me the details!");
            return;
        }
        if (deadlineParts[1].trim().isEmpty()) {
            System.out.println("When is this deadline due? Add the time after /by!");
            return;
        }

        try {
            Deadline newTask = new Deadline(deadlineParts[0], deadlineParts[1]);
            tasks.add(newTask);
            System.out.println("🎉 Got it. I've added this task:");
            System.out.println("  " + newTask.toString());
            System.out.println("📊 Now you have " + tasks.size() + " tasks in the list.");
        } catch (Exception e) {
            System.out.println("❌ Oops! Couldn't add the deadline. Let's try that again!");
        }
    }

    public void addEvent(String input) {
        if (input == null || input.trim().isEmpty()) {
            System.out.println("Wait, what's the event? Give me the details!");
            return;
        }

        String[] eventParts = input.split(" /from ", 2);
        if (eventParts.length < 2) {
            System.out.println("🎉 Event format should be: event DESCRIPTION /from START_TIME /to END_TIME");
            return;
        }

        String[] timeParts = eventParts[1].split(" /to ", 2);
        if (timeParts.length < 2) {
            System.out.println("🎉 Event format should be: event DESCRIPTION /from START_TIME /to END_TIME");
            return;
        }

        if (eventParts[0].trim().isEmpty()) {
            System.out.println("Wait, what's the event description? Give me the details!");
            return;
        }
        if (timeParts[0].trim().isEmpty()) {
            System.out.println("When does this event start? Add the start time after /from!");
            return;
        }
        if (timeParts[1].trim().isEmpty()) {
            System.out.println("When does this event end? Add the end time after /to!");
            return;
        }

        try {
            Event newTask = new Event(eventParts[0], timeParts[0], timeParts[1]);
            tasks.add(newTask);
            System.out.println("🎉 Got it. I've added this task:");
            System.out.println("  " + newTask.toString());
            System.out.println("📊 Now you have " + tasks.size() + " tasks in the list.");
        } catch (Exception e) {
            System.out.println("❌ Oops! Couldn't add the event. Let's try that again!");
        }
    }

    public void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("📭 Your task list is empty! Time to add some tasks?");
            System.out.println("💡 Try 'todo', 'deadline', or 'event' to get started!");
            return;
        }

        System.out.println("📋 Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i).toString());
        }
    }

    public void markTask(int taskNumber) {
        if (taskNumber <= 0) {
            System.out.println("❌ It's giving 'error'. Task numbers start from 1.");
            return;
        }

        int index = taskNumber - 1;
        if (index >= 0 && index < tasks.size()) {
            try {
                Task task = tasks.get(index);
                if (task.isDone()) {
                    System.out.println("ℹ️  This task is already marked as done!");
                } else {
                    task.markAsDone();
                    System.out.println("✅ Slay, you completed the task. Let's go!");
                    System.out.println("  " + task.toString());
                }
            } catch (Exception e) {
                System.out.println("❌ Oops! Something went wrong marking the task.");
            }
        } else {
            System.out.println("❌ It's giving 'error'. That task number is sus.");
        }
    }

    public void unmarkTask(int taskNumber) {
        if (taskNumber <= 0) {
            System.out.println("❌ It's giving 'error'. Task numbers start from 1.");
            return;
        }

        int index = taskNumber - 1;
        if (index >= 0 && index < tasks.size()) {
            try {
                Task task = tasks.get(index);
                if (!task.isDone()) {
                    System.out.println("ℹ️  This task is already marked as not done!");
                } else {
                    task.markAsUndone();
                    System.out.println("🔄 No cap, this one's un-marked. Back to the grind.");
                    System.out.println("  " + task.toString());
                }
            } catch (Exception e) {
                System.out.println("❌ Oops! Something went wrong unmarking the task.");
            }
        } else {
            System.out.println("❌ It's giving 'error'. That task number is sus.");
        }
    }

    public int getTaskCount() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public Task getTask(int taskNumber) {
        if (taskNumber <= 0 || taskNumber > tasks.size()) {
            return null;
        }
        return tasks.get(taskNumber - 1);
    }
}