package logic.parser.commandargs;

import common.exception.InvalidTaskOperationException;
import common.exception.InvalidTaskOperationException.ErrorType;

public final class TaskIndexParser {
    private TaskIndexParser() {
    }

    public static int parseTaskIndex(String args)
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
