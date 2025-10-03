package parser.commandargs;

import exception.InvalidTaskOperationException;
import exception.InvalidTaskOperationException.ErrorType;
import manager.TaskManager;

public final class TaskIndexParser {
    private TaskIndexParser() {}

    public static int parseTaskIndex(String args, TaskManager taskManager)
            throws InvalidTaskOperationException {
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(args.trim());
        } catch (NumberFormatException e) {
            throw new InvalidTaskOperationException(ErrorType.INVALID_NUMBER_FORMAT, args);
        }
        return taskNumber;
    }
}
