package checker;

import exceptions.DeadlineException;

import java.util.ArrayList;

/**
 * Used to check for the various conditions required for the deadline command.
 */

public class DeadlineChecker {

    /**
     * Checks if /by is in user's input
     *
     * @param deadlineInput user's input for deadline command
     * @throws DeadlineException.MissingDeadlineByKeywordException if /by is missing in input
     */
    public static void checkDeadlineByKeyword(String deadlineInput)
            throws DeadlineException.MissingDeadlineByKeywordException {
        boolean containsByKeyword = deadlineInput.contains("/by");
        if (!containsByKeyword) {
            throw new DeadlineException.MissingDeadlineByKeywordException();
        }
    }

    /**
     * Checks if task name or date and time for /by is missing
     *
     * @param deadlineInput parsed input from user's initial input, where index 0 is task name,
     *                      index 1 is by-date and time
     * @throws DeadlineException.MissingDeadlineTaskNameException if task name is empty, i.e. index 0 = ""
     * @throws DeadlineException.MissingDeadlineByException       if by-date and time is empty, i.e. index 1 = ""
     * @throws DeadlineException                                  if there is any missing sections i.e. nothing in index 0 and/or index 1
     */
    public static void checkDeadlineFormat(ArrayList<String> deadlineInput) throws DeadlineException {
        try {
            checkDeadlineTaskName(deadlineInput.get(0));
            checkDeadlineBy(deadlineInput.get(1));
        } catch (IndexOutOfBoundsException e) {
            throw new DeadlineException("Invalid deadline format.");
        }
    }

    /*
     * Used by checkDeadlineFormat function.
     * Checks if task name is missing
     */
    private static void checkDeadlineTaskName(String taskName)
            throws DeadlineException.MissingDeadlineTaskNameException {

        if (taskName.isEmpty()) {
            throw new DeadlineException.MissingDeadlineTaskNameException();
        }
    }

    /*
     * Used by checkDeadlineFormat function.
     * Checks if date and time for /by section is missing
     */
    private static void checkDeadlineBy(String by) throws DeadlineException.MissingDeadlineByException {
        if (by.isEmpty()) {
            throw new DeadlineException.MissingDeadlineByException();
        }
    }
}
