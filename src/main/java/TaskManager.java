import java.util.ArrayList;

public class TaskManager {
    private TaskList taskList;
    private java.util.Scanner scanner;

    public TaskManager(TaskList taskList, java.util.Scanner scanner) {
        this.taskList = taskList;
        this.scanner = scanner;
    }

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
                }
            }
        } else {
            System.out.println("Wait, which task are we marking?");
            System.out.println("💡 You can mark multiple tasks: mark 1,3,5");
        }
    }

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
                }
            }
        } else {
            System.out.println("Give me the number so I can unmark it.");
            System.out.println("💡 You can unmark multiple tasks: unmark 1,3,5");
        }
    }

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

    private int[] parseBulkNumbers(String argument) {
        String[] numberStrings = argument.split(",");
        ArrayList<Integer> validNumbers = new ArrayList<>();

        for (String numStr : numberStrings) {
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

        int[] result = new int[uniqueNumbers.size()];
        for (int i = 0; i < uniqueNumbers.size(); i++) {
            result[i] = uniqueNumbers.get(i);
        }
        return result;
    }
}