package parser.fileParser;

import checker.TodoChecker;
import exceptions.FileException;
import exceptions.TodoException;

import java.util.ArrayList;

/**
 * To extract relevant sections for todo task saved in file
 */
public class TodoFileParser {
    /**
     * Main parser for todo task
     *
     * @param todoFile task information from file
     * @throws FileException.FileCorruptedException if it cannot be parsed, i.e. file is corrupted or modified
     */
    protected static void parseTodoFile(ArrayList<String> todoFile) throws FileException.FileCorruptedException {
        assert todoFile != null;
        assert !todoFile.isEmpty();

        try {
            todoFile.set(0, todoFile.get(0).trim());

            TodoChecker.checkTodoTaskName(todoFile.get(0));
        } catch (TodoException | IndexOutOfBoundsException e) {
            throw new FileException.FileCorruptedException();
        }
    }
}
