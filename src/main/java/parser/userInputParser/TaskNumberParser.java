package parser.userInputParser;

import checker.TaskNumberChecker;
import enums.CommandType;
import exceptions.GrootException;
import exceptions.TaskNumberException;

import java.util.AbstractMap;
import java.util.ArrayList;

/**
 * To extract task number from mark, unmark, delete and clone command
 */
public class TaskNumberParser {
    /**
     * Parse information for mark, unmark and delete command
     * Only need to check task number given by user
     *
     * @param command information given
     * @throws GrootException if error during check
     */
    protected static void parseTaskNumber(AbstractMap.SimpleEntry<CommandType, ArrayList<String>> command)
            throws GrootException {
        assert command.getValue() != null;
        assert !command.getValue().isEmpty();
        assert command.getValue().get(0) != null;
        assert command.getKey() != null;
        assert command.getKey() == CommandType.MARK || command.getKey() == CommandType.UNMARK ||
                command.getKey() == CommandType.DELETE || command.getKey() == CommandType.CLONE;

        TaskNumberChecker.checkTaskNumberFormat(command.getValue().get(0), command.getKey());
    }

    /**
     * Convert task number from string to integer and check if task number is within range
     *
     * @param taskNumber   task number for operation
     * @param tasklistSize size of tasklist
     * @return task number if valid
     * @throws TaskNumberException.TaskNotFoundException if task number is out of range
     */
    public static int getTaskNumber(String taskNumber, int tasklistSize)
            throws TaskNumberException.TaskNotFoundException {
        assert taskNumber != null;

        int taskNumberInt = Integer.parseInt(taskNumber);
        TaskNumberChecker.checkTaskNumberValid(taskNumberInt, tasklistSize);
        return taskNumberInt;
    }
}
