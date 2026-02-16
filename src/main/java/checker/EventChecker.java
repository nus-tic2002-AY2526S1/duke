package checker;

import exceptions.EventException;

import java.util.ArrayList;

/**
 * Used for checking the various conditions required for event command
 */
public class EventChecker {
    /**
     * Checks if both keywords, /from and /to, is in user's input
     *
     * @param eventInput user's input for event command
     * @throws EventException.MissingEventKeywordsException    if both /from and /to are missing
     * @throws EventException.MissingEventFromKeywordException if only /from is missing
     * @throws EventException.MissingEventToKeywordException   if only /to is missing
     */
    public static void checkEventKeywords(String eventInput) throws EventException {
        boolean isFromKeywordMissing = !eventInput.contains("/from");
        boolean isToKeywordMissing = !eventInput.contains("/to");
        boolean isKeywordsMissing = isFromKeywordMissing && isToKeywordMissing;

        if (isKeywordsMissing) {
            throw new EventException.MissingEventKeywordsException();
        } else if (isFromKeywordMissing) {
            throw new EventException.MissingEventFromKeywordException();
        } else if (isToKeywordMissing) {
            throw new EventException.MissingEventToKeywordException();
        }
    }

    /**
     * Checks if required information is available
     *
     * @param eventInput parsed input from user's initial input, where index 0 is task name,
     *                   index 1 is start-date and time, index 2 is end-date and time
     * @throws EventException.MissingEventTaskNameException if task name is empty, i.e. index 0 = ""
     * @throws EventException.MissingEventFromException     if from-date and time is empty, i.e. index 1 = ""
     * @throws EventException.MissingEventToException       if to-date and time is empty, i.e. index 2 = ""
     * @throws EventException                               if there is any missing sections i.e. nothing in index 0, index 1 and/or index 2
     */
    public static void checkEventFormat(ArrayList<String> eventInput) throws EventException {
        try {
            checkEventTaskNameMissing(eventInput.get(0));
            checkEventFromMissing(eventInput.get(1));
            checkEventToMissing(eventInput.get(2));
        } catch (IndexOutOfBoundsException e) {
            throw new EventException("Invalid event format.");
        }
    }

    /*
     * Used by checkEventFormat.
     * Checks if task name is missing from userInput
     */
    private static void checkEventTaskNameMissing(String taskName)
            throws EventException.MissingEventTaskNameException {

        if (taskName.isEmpty()) {
            throw new EventException.MissingEventTaskNameException();
        }
    }

    /*
     * Used by checkEventFormat.
     * Checks if /from section is missing from user's input
     */
    private static void checkEventFromMissing(String eventInput) throws EventException.MissingEventFromException {
        if (eventInput.isEmpty()) {
            throw new EventException.MissingEventFromException();
        }
    }

    /**
     * Used by checkEventFormat.
     * Checks if /from section is missing from user's input
     */
    private static void checkEventToMissing(String eventInput) throws EventException.MissingEventToException {
        if (eventInput.isEmpty()) {
            throw new EventException.MissingEventToException();
        }
    }


}
