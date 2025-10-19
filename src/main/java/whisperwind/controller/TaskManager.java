package whisperwind.controller;

import whisperwind.exceptions.CommandException;
import whisperwind.exceptions.TaskException;
import whisperwind.model.*;
import java.util.ArrayList;

/**
 * The {@code TaskManager} class handles task-related operations such as marking,
 * unmarking, and bulk updates for a list of tasks. It interacts with the {@link TaskList}
 * model and manages user confirmations through console input.
 */
public class TaskManager {
    private TaskList taskList;
    private java.util.Scanner scanner;

    /**
     * Constructs a TaskManager instance.
     *
     * @param taskList The list of tasks to be managed.
     * @param scanner  The scanner used for reading user input from the console.
     */
    public TaskManager(TaskList taskList, java.util.Scanner scanner) {
        this.taskList = taskList;
        this.scanner = scanner;
    }

    /**
     * Handles the 'mark' command to mark one or more tasks as done.
     * <p>
     * Example usage:
     * <pre>
     *     mark 1
     *     mark 2,3,5
     * </pre>
     *
     * @param parts The user input split into command parts.
     */
    public void handleMarkCommand(String[] parts) {
        if (parts.length > 1) {
            String argument = parts[1].trim();

            if (argument.contains(",")) {
                handleBulkMark(argument, true);
            } else {
                try {
                    int taskNumber = Integer.parseInt(argument);
                    taskList.markTask(taskNumber);
                } catch (NumberFormatException e) {
                    System.out.println("It's giving 'error'. That number ain't it.");
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("That task number doesn't exist in the list!");
                } catch (CommandException e) {
                    // You can customize the message based on what the exception contains
                    System.out.println("Cannot mark task: " + e.getMessage());
                } catch (TaskException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            System.out.println("Wait, which task are we marking?");
            System.out.println("💡 You can mark multiple tasks: mark 1,3,5");
        }
    }

    /**
     * Handles the 'unmark' command to mark one or more tasks as not done.
     * <p>
     * Example usage:
     * <pre>
     *     unmark 2
     *     unmark 1,4,5
     * </pre>
     *
     * @param parts The user input split into command parts.
     */
    public void handleUnmarkCommand(String[] parts) {
        if (parts.length > 1) {
            String argument = parts[1].trim();

            if (argument.contains(",")) {
                handleBulkMark(argument, false);
            } else {
                try {
                    int taskNumber = Integer.parseInt(argument);
                    taskList.unmarkTask(taskNumber);
                } catch (NumberFormatException e) {
                    System.out.println("It's giving 'error'. That number ain't it.");
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("That task number doesn't exist in the list!");
                } catch (TaskException e) {
                    throw new RuntimeException(e);
                } catch (CommandException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            System.out.println("Give me the number so I can unmark it.");
            System.out.println("💡 You can unmark multiple tasks: unmark 1,3,5");
        }
    }

    /**
     * Handles marking or unmarking multiple tasks at once.
     * Displays the list of tasks to process, confirms the operation with the user,
     * and performs the updates accordingly.
     *
     * @param argument   Comma-separated string of task numbers.
     * @param markAsDone {@code true} to mark as done, {@code false} to unmark.
     */
    private void handleBulkMark(String argument, boolean markAsDone) {
        int[] taskNumbers = parseBulkNumbers(argument);
        if (taskNumbers.length > 0) {
            String action = markAsDone ? "mark" : "unmark";
            System.out.println("🔍 The following tasks will be " + (markAsDone ? "marked as done" : "unmarked") + ":");

            ArrayList<Task> tasksToProcess = new ArrayList<>();
            ArrayList<Integer> validIndexes = new ArrayList<>();

            for (int taskNumber : taskNumbers) {
                if (taskNumber > 0 && taskNumber <= taskList.getTaskCount()) {
                    Task task = taskList.getTask(taskNumber);
                    tasksToProcess.add(task);
                    validIndexes.add(taskNumber);
                    System.out.println("  " + taskNumber + ". " + task.toString());
                } else {
                    System.out.println("⚠️  Skipping invalid task number: " + taskNumber);
                }
            }

            if (tasksToProcess.isEmpty()) {
                System.out.println("❌ No valid task numbers to " + action + ".");
                return;
            }

            boolean hasRedundantOperations = false;
            for (Task task : tasksToProcess) {
                if ((markAsDone && task.isDone()) || (!markAsDone && !task.isDone())) {
                    hasRedundantOperations = true;
                    break;
                }
            }

            if (hasRedundantOperations) {
                System.out.println("💡 Some tasks are already in the desired state.");
            }

            System.out.print("🔄 " + (markAsDone ? "Mark" : "Unmark") + " these " + tasksToProcess.size() + " tasks? (yes/no): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (confirmation.equals("yes") || confirmation.equals("y")) {
                int successCount = 0;
                int skipCount = 0;

                for (int taskNumber : validIndexes) {
                    try {
                        Task task = taskList.getTask(taskNumber);
                        boolean currentState = task.isDone();

                        if ((markAsDone && !currentState) || (!markAsDone && currentState)) {
                            if (markAsDone) {
                                task.markAsDone();
                            } else {
                                task.markAsUndone();
                            }
                            successCount++;
                        } else {
                            skipCount++;
                        }
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("⚠️  Task " + taskNumber + " no longer exists, skipping...");
                    } catch (TaskException e) {
                        throw new RuntimeException(e);
                    }
                }

                System.out.println("✅ " + (markAsDone ? "Marked" : "Unmarked") + " " + successCount + " tasks successfully.");
                if (skipCount > 0) {
                    System.out.println("💫 Skipped " + skipCount + " tasks that were already in the desired state.");
                }
            } else {
                System.out.println("😅 Operation cancelled.");
            }
        } else {
            System.out.println("❌ No valid task numbers found in: " + argument);
        }
    }

    /**
     * Parses a comma-separated list of task numbers from a user input string.
     * Filters out invalid and duplicate numbers.
     *
     * @param argument A comma-separated string of numbers (e.g., "1,2,3").
     * @return An array of unique, valid task numbers.
     */
    private int[] parseBulkNumbers(String argument) {
        assert argument != null : "Argument should not be null";

        String[] numberStrings = argument.split(",");
        ArrayList<Integer> validNumbers = new ArrayList<>();

        for (String numStr : numberStrings) {
            assert numStr != null : "Number string should not be null";
            try {
                int num = Integer.parseInt(numStr.trim());
                if (num > 0) {
                    validNumbers.add(num);
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️  Skipping invalid number: " + numStr);
            }
        }

        ArrayList<Integer> uniqueNumbers = new ArrayList<>();
        for (Integer num : validNumbers) {
            if (!uniqueNumbers.contains(num)) {
                uniqueNumbers.add(num);
            }
        }

        int[] result = new int[validNumbers.size()];
        for (int i = 0; i < uniqueNumbers.size(); i++) {
            result[i] = uniqueNumbers.get(i);
        }
        assert result.length == validNumbers.size() : "Array size should match list size";
        return result;
    }
}
