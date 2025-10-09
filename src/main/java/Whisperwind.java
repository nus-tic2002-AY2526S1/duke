import java.util.Scanner;

public class Whisperwind {

    private TaskList tasks;
    private Scanner scanner;

    public Whisperwind() {
        this.tasks = new TaskList();
        this.scanner = new Scanner(System.in);
    }

    private void showInstructions() {
        System.out.println("Type 'list' to see the whole vibe for today");
        System.out.println("Type 'mark X' to mark a task as done. Slay!");
        System.out.println("Type 'unmark X' to get that task back on the grind");
        System.out.println("Type 'todo DESCRIPTION' to add a simple task");
        System.out.println("Type 'deadline DESCRIPTION /by TIME' to add a task with deadline");
        System.out.println("Type 'event DESCRIPTION /from START_TIME /to END_TIME' to add an event");
        System.out.println("Type 'view instruction' to peep these instructions again");
        System.out.println("Type 'bye' to say goodbye.");
    }

    public void start() {
        System.out.println("Hello! I'm Whisperwind");
        System.out.println("Type 'view instruction' to see instructions");
        System.out.println("What can I do for you?");

        boolean isExit = false;
        while (!isExit) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            String[] parts = input.split(" ", 2);
            String command = parts[0].toLowerCase();

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
                        }
                    } else {
                        System.out.println("Give me the number so I can unmark it.");
                    }
                    break;
                case "todo":
                    if (parts.length > 1) {
                        tasks.addTodo(parts[1]);
                    } else {
                        System.out.println("Wait, what's the todo? Give me the details!");
                    }
                    break;
                case "deadline":
                    if (parts.length > 1) {
                        tasks.addDeadline(parts[1]);
                    } else {
                        System.out.println("Wait, what's the deadline? Give me the details!");
                    }
                    break;
                case "event":
                    if (parts.length > 1) {
                        tasks.addEvent(parts[1]);
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
        }
        System.out.println("Bye. Hope to see you again soon!");
        scanner.close();
    }

    public static void main(String[] args) {
        new Whisperwind().start();
    }
}

