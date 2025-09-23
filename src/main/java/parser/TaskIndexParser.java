package parser;

import exception.InvalidTaskOperationException;
import exception.InvalidTaskOperationException.ErrorType;
import manager.TaskManager;

public class TaskIndexParser {
    private TaskIndexParser() {}

    public static int parseTaskIndex(String args, TaskManager taskManager)
            throws InvalidTaskOperationException {

        if (taskManager.isEmpty()) {
            throw new InvalidTaskOperationException(ErrorType.EMPTY_LIST);
        }
        if (args.isBlank()) {
            throw new InvalidTaskOperationException(ErrorType.MISSING_TASK_NUMBER);
        }
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(args.trim());
        } catch (NumberFormatException e) {
            throw new InvalidTaskOperationException(ErrorType.INVALID_NUMBER_FORMAT, args);
        }
        return taskNumber;
    }
}
