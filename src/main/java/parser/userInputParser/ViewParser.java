package parser.userInputParser;

import checker.ViewChecker;
import enums.CommandType;
import exceptions.ViewException;

import java.util.AbstractMap;
import java.util.ArrayList;

public class ViewParser {
    /**
     * Extract information for view function
     *
     * @param input information given by user
     * @throws ViewException.MissingViewDateException if date is missing, i.e. index 0 is ""
     */
    protected static void parseView(AbstractMap.SimpleEntry<CommandType, ArrayList<String>> input)
            throws ViewException.MissingViewDateException {
        assert input.getValue() != null;
        assert !input.getValue().isEmpty();
        assert input.getValue().get(0) != null;

        ViewChecker.checkViewDate(input.getValue().get(0));
    }
}
