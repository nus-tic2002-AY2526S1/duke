package checker;

import enums.CommandType;
import exceptions.TaskNumberException;

/**
 * Used for checking the various conditions required for mark, unmark and delete commands.
 * Mark, Unmark and Delete commands have the same format
 */

public class TaskNumberChecker {

    /**
     * Checks if task number given by user is empty or not digit
     *
     * @param taskNumber       task number given by user
     * @param commandType command type, i.e. mark, unmark or delete
     * @throws TaskNumberException.MissingTaskNumberException if task number is missing, i.e. taskNumber = ""
     * @throws TaskNumberException.InvalidTaskNumberException if task number is not a digit
     */
    public static void checkTaskNumberFormat(String taskNumber, CommandType commandType) throws TaskNumberException {
        checkTaskNumberEmpty(taskNumber, commandType); //check if there is task number i.e. not empty
        checkTaskNumberDigits(taskNumber, commandType); //check if task number is a digit
    }

    /*
     * Used by checkTaskNumberFormat
     * Checks if task number is empty
     */
    private static void checkTaskNumberEmpty(String taskNumber, CommandType markUnmarkDelete) throws TaskNumberException.MissingTaskNumberException {
        if (taskNumber.isEmpty()) {
            throw new TaskNumberException.MissingTaskNumberException(markUnmarkDelete.toString().toLowerCase());
        }
    }

    /*
     * Used by checkTaskNumberFormat
     * Checks if task number is a digit
     */
    private static void checkTaskNumberDigits(String taskNumber, CommandType markUnmarkDelete) throws TaskNumberException.InvalidTaskNumberException {
        boolean notDigit = !taskNumber.matches("\\d+");
        if (notDigit) {
            throw new TaskNumberException.InvalidTaskNumberException(markUnmarkDelete.toString().toLowerCase());
        }
    }

    /**
     * Checks if task number is within size of task list
     *
     * @param taskNumber   task number given by user
     * @param tasklistSize size of task list
     * @throws TaskNumberException.TaskNotFoundException if task number is out of task list range, i.e. task number is more than task list size or less than 1
     */
    public static void checkTaskNumberValid(int taskNumber, int tasklistSize)
            throws TaskNumberException.TaskNotFoundException {
        boolean isWithinRange = taskNumber <= tasklistSize && taskNumber > 0;
        if (!isWithinRange) {
            throw new TaskNumberException.TaskNotFoundException();
        }
    }

    /**
     * Checks if task is already marked as user's intention
     * Used by mark and unmark command
     *
     * @param taskIsDone task is marked as done or undone
     * @param isDone     user's intention to mark the task done or undone
     * @throws TaskNumberException.TaskAlreadyMarkedException if task is already marked according to user's intention
     */
    public static void checkTaskStatus(boolean taskIsDone, boolean isDone)
            throws TaskNumberException.TaskAlreadyMarkedException {

        boolean isTaskAlreadyMarked = taskIsDone == isDone;

        if (isTaskAlreadyMarked) {
            throw new TaskNumberException.TaskAlreadyMarkedException(isDone ? "done" : "not done");
        }
    }
}
