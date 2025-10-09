import java.util.Scanner;
import java.io.IOException;

public class Whisperwind {

    private TaskList tasks;
    private Scanner scanner;
    private TaskFileManager fileManager;
    private long lastCommandTime = 0;
    private static final long MIN_COMMAND_INTERVAL = 300; // milliseconds

    public Whisperwind() {
        this.tasks = new TaskList();
        this.scanner = new Scanner(System.in);
        this.fileManager = new TaskFileManager();
    }

    private void showInstructions() {
        System.out.println("\n🎊 POP-UP INSTRUCTIONS 🎊");
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║              AVAILABLE COMMANDS                  ║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.println("║ 🗒️   list              - View all tasks          ║");
        System.out.println("║ ✅   mark [number]     - Complete a task         ║");
        System.out.println("║ 🔄   unmark [number]   - Uncomplete a task       ║");
        System.out.println("║ 📝   todo [task]       - Add simple task         ║");
        System.out.println("║ ⏰   deadline [task]   - Add task with deadline  ║");
        System.out.println("║ 🎉   event [task]      - Add event with times    ║");
        System.out.println("║ 💾   save              - Force save tasks        ║");
        System.out.println("║ ❓   view instruction  - Show this pop-up        ║");
        System.out.println("║ 👋   bye               - Exit gracefully         ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println("✨ Press Enter to return to your tasks... ✨");
        scanner.nextLine();

        System.out.println("\n".repeat(2));
        System.out.println("Welcome back! What would you like to do next?");
    }

    public void start() {
        // Resource leak prevention: Use try-with-resources
        try {
            showWelcomeMessage();
            loadTasksOnStartup();

            boolean isExit = false;
            while (!isExit) {
                System.out.print("> ");
                String input = scanner.nextLine().trim();

                // Input sanitization
                input = InputSanitizer.sanitizeInput(input);

                if (input.isEmpty()) {
                    System.out.println("It's giving 'silent treatment'. Say something!");
                    continue;
                }

                // Rate limiting
                if (!checkRateLimit()) {
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
                                try {
                                    int taskNumber = Integer.parseInt(parts[1]);
                                    tasks.markTask(taskNumber);
                                    autoSaveTasks();
                                } catch (NumberFormatException e) {
                                    System.out.println("It's giving 'error'. That number ain't it.");
                                } catch (IndexOutOfBoundsException e) {
                                    System.out.println("That task number doesn't exist in the list!");
                                }
                            } else {
                                System.out.println("Wait, which task are we marking?");
                            }
                            break;
                        case "unmark":
                            if (parts.length > 1) {
                                try {
                                    int taskNumber = Integer.parseInt(parts[1]);
                                    tasks.unmarkTask(taskNumber);
                                    autoSaveTasks();
                                } catch (NumberFormatException e) {
                                    System.out.println("It's giving 'error'. That number ain't it.");
                                } catch (IndexOutOfBoundsException e) {
                                    System.out.println("That task number doesn't exist in the list!");
                                }
                            } else {
                                System.out.println("Give me the number so I can unmark it.");
                            }
                            break;
                        case "todo":
                            if (parts.length > 1) {
                                if (parts[1].trim().isEmpty()) {
                                    System.out.println("Wait, what's the todo? Give me the details!");
                                } else {
                                    tasks.addTodo(parts[1]);
                                    autoSaveTasks();
                                }
                            } else {
                                System.out.println("Wait, what's the todo? Give me the details!");
                            }
                            break;
                        case "deadline":
                            if (parts.length > 1) {
                                if (parts[1].trim().isEmpty()) {
                                    System.out.println("Wait, what's the deadline? Give me the details!");
                                } else {
                                    tasks.addDeadline(parts[1]);
                                    autoSaveTasks();
                                }
                            } else {
                                System.out.println("Wait, what's the deadline? Give me the details!");
                            }
                            break;
                        case "event":
                            if (parts.length > 1) {
                                if (parts[1].trim().isEmpty()) {
                                    System.out.println("Wait, what's the event? Give me the details!");
                                } else {
                                    tasks.addEvent(parts[1]);
                                    autoSaveTasks();
                                }
                            } else {
                                System.out.println("Wait, what's the event? Give me the details!");
                            }
                            break;
                        case "save":
                            saveTasks();
                            break;
                        case "bye":
                            saveTasks(); // Auto-save on exit
                            isExit = true;
                            break;
                        case "view":
                            if (parts.length > 1 && parts[1].equalsIgnoreCase("instruction")) {
                                showInstructions();
                            } else {
                                System.out.println("If you're trying to add a task, use 'todo', 'deadline', or 'event' commands!");
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

                // Performance monitoring
                tasks.checkPerformance();
            }
            showGoodbyeMessage();
        } catch (Exception e) {
            System.out.println("❌ Critical error in application: " + e.getMessage());
        } finally {
            // Resource leak prevention: Ensure scanner is closed
            if (scanner != null) {
                scanner.close();
            }
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
            // Data corruption recovery: Validate before saving
            if (tasks.validateTaskIntegrity()) {
                fileManager.saveTasks(tasks);
                System.out.println("💾 Tasks saved successfully!");
            } else {
                System.out.println("⚠️  Fixed some task issues before saving.");
                fileManager.saveTasks(tasks); // Save after recovery
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to save tasks: " + e.getMessage());
        } catch (SecurityException e) {
            System.out.println("❌ No permission to save tasks file!");
        }
    }

    private void autoSaveTasks() {
        try {
            // Only auto-save if we have tasks to prevent unnecessary I/O
            if (tasks.getTaskCount() > 0) {
                fileManager.autoSaveTasks(tasks);
            }
        } catch (Exception e) {
            System.out.println("⚠️  Auto-save failed: " + e.getMessage());
        }
    }

    private void showWelcomeMessage() {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║                 🌸 WHISPERWIND 🌸                 ║");
        System.out.println("║            Your Personal Task Manager            ║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.println("║   Type 'view instruction' for command pop-up!    ║");
        System.out.println("║   Type 'save' to manually backup your tasks      ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println("\nWhat can I do for you today? 💫");
    }

    private void showGoodbyeMessage() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║                    👋 GOODBYE!                    ║");
        System.out.println("║         Thanks for using Whisperwind! 🌸          ║");
        System.out.println("║      Your tasks are saved for next time! 💾       ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
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