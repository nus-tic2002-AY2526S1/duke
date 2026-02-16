package parser.fileParser;

import checker.DeadlineChecker;
import exceptions.DeadlineException;
import exceptions.FileException;

import java.util.ArrayList;

/**
 * To extract all the relevant sections for deadline task saved in file
 */
public class DeadlineFileParser {
    /**
     * Main parser for deadline task
     *
     * @param deadlineFile task information from file
     * @throws FileException.FileCorruptedException if it cannot be parsed, i.e. file is corrupted or modified
     */
    protected static void parseDeadlineFile(ArrayList<String> deadlineFile)
            throws FileException.FileCorruptedException {
        assert deadlineFile != null;
        assert !deadlineFile.isEmpty();

        try {
            String taskInfo = deadlineFile.get(0).trim();
            String[] taskInfoArray = taskInfo.split("\\| by:", -1);

            assert taskInfoArray.length == 2;

            deadlineFile.set(0, taskInfoArray[0].trim());
            deadlineFile.add(taskInfoArray[1].trim());

            DeadlineChecker.checkDeadlineFormat(deadlineFile);
        } catch (DeadlineException | ArrayIndexOutOfBoundsException e) {
            throw new FileException.FileCorruptedException();
        }
    }
}
