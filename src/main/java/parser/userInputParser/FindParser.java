package parser.userInputParser;

import checker.FindChecker;
import enums.CommandType;
import exceptions.FindException;

import java.util.AbstractMap;
import java.util.ArrayList;

public class FindParser {
    protected static void parseFind (AbstractMap.SimpleEntry<CommandType, ArrayList<String>> input) throws FindException {
        assert input.getValue() != null;
        assert input.getValue().get(0) != null;

        FindChecker.checkKeyword(input.getValue().get(0));
    }
}
