package checker;

import exceptions.ViewException;

/**
 * Used for checking the various conditions required for todo command
 */

public class ViewChecker {
    /**
     * Checks if task name is given
     *
     * @param viewDate task name extracted from user's input
     * @throws ViewException.MissingViewDateException if task name is empty i.e. taskName = ""
     */
    public static void checkViewDate(String viewDate) throws ViewException.MissingViewDateException {
        if (viewDate.isEmpty()) {
            throw new ViewException.MissingViewDateException();
        }
    }
}
