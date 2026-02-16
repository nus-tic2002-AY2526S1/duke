package parser.fileParser;

import checker.EventChecker;
import exceptions.EventException;
import exceptions.FileException;

import java.util.ArrayList;

/**
 * To extract all the relevant sections for event task saved in file
 */
public class EventFileParser {
    /**
     * Main parser for event task
     *
     * @param eventFile task information from file
     * @throws FileException.FileCorruptedException if it cannot be parsed, i.e. file is corrupted or modified
     */
    protected static void parseEventFile(ArrayList<String> eventFile) throws FileException.FileCorruptedException {
        assert eventFile != null;
        assert !eventFile.isEmpty();

        try {
            String taskInfo = eventFile.get(0). trim();
            String[] taskInfoArray = taskInfo.split("\\| from:");

            eventFile.set(0, taskInfoArray[0].trim());

            taskInfoArray = taskInfoArray[1].split(", to:");

            eventFile.add(taskInfoArray[0].trim());
            eventFile.add(taskInfoArray[1].trim());

            EventChecker.checkEventFormat(eventFile);
        } catch (EventException | ArrayIndexOutOfBoundsException e) {
            throw new FileException.FileCorruptedException();
        }
    }
}