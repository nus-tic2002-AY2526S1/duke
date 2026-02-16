package parser.userInputParser;

import checker.TodoChecker;
import enums.CommandType;
import exceptions.TodoException;

import java.util.AbstractMap;
import java.util.ArrayList;

/**
 * Parse todo information.
 * Only need to check if task name is given by user
 */
public class TodoParser {
    /**
     * Extract information for todo task creation
     *
     * @param input information given by user
     * @throws TodoException.MissingTodoTaskNameException if task name is missing, i.e. index 0 is ""
     */
    protected static void parseTodo(AbstractMap.SimpleEntry<CommandType, ArrayList<String>> input)
            throws TodoException.MissingTodoTaskNameException {
        assert input.getValue() != null;
        assert !input.getValue().isEmpty();
        assert input.getValue().get(0) != null;

        TodoChecker.checkTodoTaskName(input.getValue().get(0));
    }
}
