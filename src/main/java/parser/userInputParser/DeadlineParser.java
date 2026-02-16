package parser.userInputParser;

import checker.DeadlineChecker;
import enums.CommandType;
import exceptions.DeadlineException;

import java.util.AbstractMap;
import java.util.ArrayList;

/**
 * To extract out relevant sections for deadline task
 */
public class DeadlineParser {
    /**
     * Extract information for deadline task creation
     *
     * @param deadline information given by user
     * @throws DeadlineException if any errors in user input
     */
    protected static void parseDeadline(AbstractMap.SimpleEntry<CommandType, ArrayList<String>> deadline)
            throws DeadlineException {
        assert deadline.getValue() != null;
        assert !deadline.getValue().isEmpty();

        ArrayList<String> deadlineInformation = deadline.getValue();

        DeadlineChecker.checkDeadlineByKeyword(deadlineInformation.get(0));

        splitDeadlineInformation(deadlineInformation);

        DeadlineChecker.checkDeadlineFormat(deadlineInformation);

        deadline.setValue(deadlineInformation);
    }

    /*
     * Extract the relevant sections from user input, i.e. task name and by-date and time
     */
    private static void splitDeadlineInformation(ArrayList<String> deadlineInformation) {
        String[] info = deadlineInformation.get(0).split("/by", -1);

        assert info.length == 2;
        assert info[0] != null;
        assert info[1] != null;

        String taskName = info[0].trim();
        String by = info[1].trim();

        deadlineInformation.set(0, taskName);
        deadlineInformation.add(by);
    }
}
