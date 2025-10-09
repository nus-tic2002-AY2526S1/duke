import java.util.Scanner;
import java.io.IOException;

public class Whisperwind {

    private TaskList tasks;
    private Scanner scanner;
    private TaskFileManager fileManager;
    private DeleteManager deleteManager;
    private TaskManager taskManager; // NEW
    private long lastCommandTime = 0;
    private static final long MIN_COMMAND_INTERVAL = 300;

    public Whisperwind() {
        this.tasks = new TaskList();
        this.scanner = new Scanner(System.in);
        this.fileManager = new TaskFileManager();
        this.deleteManager = new DeleteManager(tasks, scanner);
        this.taskManager = new TaskManager(tasks, scanner); // NEW
    }

    public void start() {
        try {
            InstructionManager.showWelcomeMessage();
            loadTasksOnStartup();

            boolean isExit = false;
            while (!isExit) {
                System.out.print("> ");
                String input = scanner.nextLine().trim();

                input = InputSanitizer.sanitizeInput(input);

                if (input.isEmpty()) {
                    System.out.println("It's giving 'silent treatment'. Say something!");
                    continue;
                }

                if (!checkRateLimit()) {
                    continue;
                }

                // Handle instruction commands first
                if (input.equalsIgnoreCase("view instruction")) {
                    InstructionManager.showBasicInstructions();
                    continue;
                }

                if (input.equalsIgnoreCase("delete instruction")) {
                    InstructionManager.showDeleteInstructions();
                    continue;
                }

                String[] parts = input.split(" ", 2);
                String command = parts[0].toLowerCase();

                try {
                    switch (command) {
                        case "list":
                            tasks.listTasks();
                            break;
                        case "mark":
                            if (parts.length > 1) {
                                taskManager.handleMarkCommand(parts); // UPDATED
                                autoSaveTasks();
                            } else {
                                System.out.println("Wait, which task are we marking?");
                                System.out.println("💡 You can mark multiple: mark 1,3,5");
                            }
                            break;
                        case "unmark":
                            if (parts.length > 1) {
                                taskManager.handleUnmarkCommand(parts); // UPDATED
                                autoSaveTasks();
                            } else {
                                System.out.println("Give me the number so I can unmark it.");
                                System.out.println("💡 You can unmark multiple: unmark 1,3,5");
                            }
                            break;
                        case "delete":
                            if (parts.length > 1) {
                                if (parts[1].equalsIgnoreCase("instruction")) {
                                    InstructionManager.showDeleteInstructions();
                                } else {
                                    deleteManager.handleDeleteCommand(parts);
                                    autoSaveTasks();
                                }
                            } else {
                                System.out.println("Wait, what do you want to delete?");
                                deleteManager.showDeleteHelp();
                            }
                            break;
                        case "todo":
                            handleTodoCommand(parts);
                            break;
                        case "deadline":
                            handleDeadlineCommand(parts);
                            break;
                        case "event":
                            handleEventCommand(parts);
                            break;
                        case "save":
                            saveTasks();
                            break;
                        case "bye":
                            saveTasks();
                            isExit = true;
                            break;
                        case "view":
                            if (parts.length > 1 && parts[1].equalsIgnoreCase("instruction")) {
                                InstructionManager.showBasicInstructions();
                            } else {
                                System.out.println("Did you mean 'view instruction'?");
                            }
                            break;
                        default:
                            System.out.println("I don't know that command! Type 'view instruction' to see what I can do.");
                            break;
                    }
                } catch (Exception e) {
                    System.out.println("❌ Oops! Something went wrong. Let's try that again!");
                    System.out.println("💡 Error: " + e.getMessage());
                }

                tasks.checkPerformance();
            }
            InstructionManager.showGoodbyeMessage();
        } catch (Exception e) {
            System.out.println("❌ Critical error in application: " + e.getMessage());
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private void handleTodoCommand(String[] parts) {
        if (parts.length > 1 && !parts[1].trim().isEmpty()) {
            tasks.addTodo(parts[1]);
            autoSaveTasks();
        } else {
            System.out.println("Wait, what's the todo? Give me the details!");
        }
    }

    private void handleDeadlineCommand(String[] parts) {
        if (parts.length > 1 && !parts[1].trim().isEmpty()) {
            tasks.addDeadline(parts[1]);
            autoSaveTasks();
        } else {
            System.out.println("Wait, what's the deadline? Give me the details!");
        }
    }

    private void handleEventCommand(String[] parts) {
        if (parts.length > 1 && !parts[1].trim().isEmpty()) {
            tasks.addEvent(parts[1]);
            autoSaveTasks();
        } else {
            System.out.println("Wait, what's the event? Give me the details!");
        }
    }

    private boolean checkRateLimit() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCommandTime < MIN_COMMAND_INTERVAL) {
            System.out.println("⏳ Whoa there! Slow down a bit...");
            return false;
        }
        lastCommandTime = currentTime;
        return true;
    }

    private void loadTasksOnStartup() {
        try {
            System.out.println("🔍 Looking for saved tasks...");
            fileManager.loadTasks(tasks);
            System.out.println("✅ Loaded " + tasks.getTaskCount() + " tasks from previous session");
        } catch (IOException e) {
            System.out.println("💫 Starting fresh! No previous tasks found or error loading.");
        }
    }

    private void saveTasks() {
        try {
            if (tasks.validateTaskIntegrity()) {
                fileManager.saveTasks(tasks);
                System.out.println("💾 Tasks saved successfully!");
            } else {
                System.out.println("⚠️  Fixed some task issues before saving.");
                fileManager.saveTasks(tasks);
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to save tasks: " + e.getMessage());
        } catch (SecurityException e) {
            System.out.println("❌ No permission to save tasks file!");
        }
    }

    private void autoSaveTasks() {
        try {
            if (tasks.getTaskCount() > 0) {
                fileManager.autoSaveTasks(tasks);
            }
        } catch (Exception e) {
            System.out.println("⚠️  Auto-save failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            new Whisperwind().start();
        } catch (Exception e) {
            System.out.println("💥 Critical error! Whisperwind has to close. Bye!");
            System.out.println("Error: " + e.getMessage());
        }
    }
}