package parser.userInputParser;

import checker.EventChecker;
import enums.CommandType;
import exceptions.GrootException;

import java.util.AbstractMap;
import java.util.ArrayList;

/**
 * To extract relevant sections for event task
 */
public class EventParser {
    /**
     * Extract information for event task creation
     *
     * @param event information given by user
     * @throws GrootException if there are any errors in user input
     */
    protected static void parseEvent(AbstractMap.SimpleEntry<CommandType, ArrayList<String>> event)
            throws GrootException {
        assert event != null;
        assert event.getValue() != null;
        assert event.getValue().get(0) != null;

        ArrayList<String> eventInput = event.getValue();

        EventChecker.checkEventKeywords(eventInput.get(0));

        splitEventInformation(eventInput);

        EventChecker.checkEventFormat(eventInput);

        event.setValue(eventInput);
    }

    /*
     * Extract the relevant sections\
     */
    private static void splitEventInformation(ArrayList<String> eventInfo) {
        String[] eventParts = eventInfo.get(0).split("/from|/to", -1);

        assert eventParts.length == 3;
        assert eventParts[0] != null;
        assert eventParts[1] != null;
        assert eventParts[2] != null;

        String taskName = eventParts[0].trim();
        String from = eventParts[1].trim();
        String to = eventParts[2].trim();

        eventInfo.set(0, taskName); //set task name to index 0
        eventInfo.add(from); //add from
        eventInfo.add(to); //add to
    }
}
