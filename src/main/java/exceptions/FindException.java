package exceptions;

import checker.FindChecker;
import enums.CommandType;

public class FindException extends GrootException{
    public FindException(String message){
        super(message);
    }
    public static class MissingKeywordException extends FindException {
        public MissingKeywordException() {
            super("Keyword to find is missing. Usage: find <keyword>");
        }
    }

    public static class TaskNotFoundException extends FindException{
        public TaskNotFoundException() {
            super("No tasks found.");
        }
    }
}
