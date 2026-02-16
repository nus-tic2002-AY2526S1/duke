package checker;

import exceptions.TodoException;

/**
 * Used for checking the various conditions required for todo command
 */

public class TodoChecker {
    /**
     * Checks if task name is given
     *
     * @param taskName task name extracted from user's input
     * @throws TodoException.MissingTodoTaskNameException if task name is empty i.e. taskName = ""
     */
    public static void checkTodoTaskName(String taskName) throws TodoException.MissingTodoTaskNameException {
        if (taskName.isEmpty()) {
            throw new TodoException.MissingTodoTaskNameException();
        }
    }
}
