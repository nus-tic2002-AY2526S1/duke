import java.util.ArrayList;
//additional added: Memory Management & Data Recovery
public class TaskList {
    private ArrayList<Task> tasks;
    private static final int MAX_TASKS = 1000;
    private static final int WARNING_THRESHOLD = 500;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    // Memory Management: Check limits before adding
    public void addTodo(String description) {
        if (tasks.size() >= MAX_TASKS) {
            System.out.println("❌ Task limit reached (" + MAX_TASKS + ")! Complete or remove some tasks first.");
            return;
        }

        String sanitizedDesc = InputSanitizer.sanitizeDescription(description);
        if (sanitizedDesc.isEmpty()) {
            System.out.println("Wait, what's the todo? Give me the details!");
            return;
        }

        try {
            Todo newTask = new Todo(sanitizedDesc);
            tasks.add(newTask);
            System.out.println("🎉 Got it. I've added this task:");
            System.out.println("  " + newTask.toString());
            System.out.println("📊 Now you have " + tasks.size() + " tasks in the list.");
        } catch (Exception e) {
            System.out.println("❌ Oops! Couldn't add the todo: " + e.getMessage());
        }
    }

    public void addDeadline(String input) {
        if (tasks.size() >= MAX_TASKS) {
            System.out.println("❌ Task limit reached (" + MAX_TASKS + ")! Complete or remove some tasks first.");
            return;
        }

        String sanitizedInput = InputSanitizer.sanitizeInput(input);
        if (sanitizedInput.isEmpty()) {
            System.out.println("Wait, what's the deadline? Give me the details!");
            return;
        }

        String[] deadlineParts = sanitizedInput.split(" /by ", 2);
        if (deadlineParts.length < 2) {
            System.out.println("⏰ Deadline format should be: deadline DESCRIPTION /by TIME");
            return;
        }

        String description = InputSanitizer.sanitizeDescription(deadlineParts[0]);
        String by = InputSanitizer.sanitizeTime(deadlineParts[1]);

        if (description.isEmpty()) {
            System.out.println("Wait, what's the deadline description? Give me the details!");
            return;
        }
        if (by.isEmpty()) {
            System.out.println("When is this deadline due? Add the time after /by!");
            return;
        }

        try {
            Deadline newTask = new Deadline(description, by);
            tasks.add(newTask);
            System.out.println("🎉 Got it. I've added this task:");
            System.out.println("  " + newTask.toString());
            System.out.println("📊 Now you have " + tasks.size() + " tasks in the list.");
        } catch (Exception e) {
            System.out.println("❌ Oops! Couldn't add the deadline: " + e.getMessage());
        }
    }

    public void addEvent(String input) {
        if (tasks.size() >= MAX_TASKS) {
            System.out.println("❌ Task limit reached (" + MAX_TASKS + ")! Complete or remove some tasks first.");
            return;
        }

        String sanitizedInput = InputSanitizer.sanitizeInput(input);
        if (sanitizedInput.isEmpty()) {
            System.out.println("Wait, what's the event? Give me the details!");
            return;
        }

        String[] eventParts = sanitizedInput.split(" /from ", 2);
        if (eventParts.length < 2) {
            System.out.println("🎉 Event format should be: event DESCRIPTION /from START_TIME /to END_TIME");
            return;
        }

        String[] timeParts = eventParts[1].split(" /to ", 2);
        if (timeParts.length < 2) {
            System.out.println("🎉 Event format should be: event DESCRIPTION /from START_TIME /to END_TIME");
            return;
        }

        String description = InputSanitizer.sanitizeDescription(eventParts[0]);
        String from = InputSanitizer.sanitizeTime(timeParts[0]);
        String to = InputSanitizer.sanitizeTime(timeParts[1]);

        if (description.isEmpty()) {
            System.out.println("Wait, what's the event description? Give me the details!");
            return;
        }
        if (from.isEmpty()) {
            System.out.println("When does this event start? Add the start time after /from!");
            return;
        }
        if (to.isEmpty()) {
            System.out.println("When does this event end? Add the end time after /to!");
            return;
        }

        try {
            Event newTask = new Event(description, from, to);
            tasks.add(newTask);
            System.out.println("🎉 Got it. I've added this task:");
            System.out.println("  " + newTask.toString());
            System.out.println("📊 Now you have " + tasks.size() + " tasks in the list.");
        } catch (Exception e) {
            System.out.println("❌ Oops! Couldn't add the event: " + e.getMessage());
        }
    }

    // Data Corruption Recovery
    public boolean validateTaskIntegrity() {
        boolean integrityOk = true;
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task == null || !task.isValid()) {
                System.out.println("⚠️  Found corrupted task at position " + (i+1) + ", removing...");
                tasks.remove(i);
                i--; // Adjust index after removal
                integrityOk = false;
            }
        }
        return integrityOk;
    }

    // Performance Monitoring
    public void checkPerformance() {
        if (tasks.size() > WARNING_THRESHOLD) {
            System.out.println("🐢 You have " + tasks.size() + " tasks. Consider archiving old ones for better performance.");
        }

        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        long maxMemory = runtime.maxMemory();

        if (usedMemory > maxMemory * 0.7) {
            System.out.println("⚠️  Memory usage is high! The app might slow down.");
        }

        if (tasks.size() > MAX_TASKS * 0.8) {
            System.out.println("📈 You're approaching the task limit (" + MAX_TASKS + "). Consider completing some tasks.");
        }
    }

    // Direct task addition for file loading (bypasses normal validation)
    public void addTaskDirectly(Task task) {
        if (task != null && task.isValid() && tasks.size() < MAX_TASKS) {
            tasks.add(task);
        }
    }

    // Existing methods with minor updates for new features
    public void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("📭 Your task list is empty! Time to add some tasks?");
            System.out.println("💡 Try 'todo', 'deadline', or 'event' to get started!");
            return;
        }

        // Validate integrity before listing
        validateTaskIntegrity();

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
                System.out.println("❌ Oops! Something went wrong marking the task: " + e.getMessage());
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
                System.out.println("❌ Oops! Something went wrong unmarking the task: " + e.getMessage());
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