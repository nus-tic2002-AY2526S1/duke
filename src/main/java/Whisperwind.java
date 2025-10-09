import java.util.Scanner;

public class Whisperwind {

    private TaskList tasks;
    private Scanner scanner;

    public Whisperwind() {
        this.tasks = new TaskList();
        this.scanner = new Scanner(System.in);
    }

    //AI is used to print a view command as I have difficulty to read the instruction. Better visibility for user friendly option
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
        System.out.println("║ ❓   view instruction  - Show this pop-up        ║");
        System.out.println("║ 👋   bye               - Exit gracefully         ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println("✨ Press Enter to return to your tasks... ✨");
        scanner.nextLine();

        // Clear some space and show welcome back message
        System.out.println("\n".repeat(2));
        System.out.println("Welcome back! What would you like to do next?");
    }

    public void start() {
        showWelcomeMessage();

        boolean isExit = false;
        while (!isExit) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            // Added: Check for empty input
            if (input.isEmpty()) {
                System.out.println("It's giving 'silent treatment'. Say something!");
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
                            }
                        } else {
                            System.out.println("Wait, what's the event? Give me the details!");
                        }
                        break;
                    case "bye":
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
                System.out.println("Oops! Something went wrong. Let's try that again!");
            }
        }
        showGoodbyeMessage();
        scanner.close();
    }

    private void showWelcomeMessage() {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║                 🌸 WHISPERWIND 🌸                 ║");
        System.out.println("║            Your Personal Task Manager            ║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.println("║   Type 'view instruction' for command pop-up!    ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println("\nWhat can I do for you today? 💫");
    }

    private void showGoodbyeMessage() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║                    👋 GOODBYE!                    ║");
        System.out.println("║         Thanks for using Whisperwind! 🌸          ║");
        System.out.println("║         Hope to see you again soon! 💫            ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
    }

    public static void main(String[] args) {
        try {
            new Whisperwind().start();
        } catch (Exception e) {
            System.out.println("Critical error! Whisperwind has to close. Bye!");
            e.printStackTrace();
        }
    }
}