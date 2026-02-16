package parser.fileParser;

import commands.AddCommand;
import enums.CommandType;
import exceptions.FileException;
import tasks.Task;

import java.util.AbstractMap;
import java.util.ArrayList;

/**
 * To delegate the different parsing of task information and create the task based on information
 */
public class FileParser {
    /**
     * Parse task information from tasklist file
     *
     * @param taskFile information in file
     * @return task based on information
     * @throws FileException if there are any errors during function
     */
    public static Task parseTaskFile(String taskFile) throws FileException {
        assert taskFile != null;

        taskFile = taskFile.trim();

        CommandType taskType = parseCommandType(taskFile); //[T/D/E] task definition

        boolean taskDone = taskIsDone(taskFile); // [X/_]

        ArrayList<String> taskInfoInput = new ArrayList<>();

        taskInfoInput.add(taskFile.substring(6));

        switch (taskType) {
            case TODO:
                TodoFileParser.parseTodoFile(taskInfoInput);
                break;
            case DEADLINE:
                DeadlineFileParser.parseDeadlineFile(taskInfoInput);
                break;
            case EVENT:
                EventFileParser.parseEventFile(taskInfoInput);
                break;
            default:
                throw new FileException.FileCorruptedException();
        }

        Task task = AddCommand.createTask(new AbstractMap.SimpleEntry<>(taskType, taskInfoInput));
        task.setIsDone(taskDone);

        return task;
    }

    /*
     * Get type of task from task information and convert to CommandType enum
     */
    private static CommandType parseCommandType(String taskLine) throws FileException {
        assert taskLine != null;
        assert taskLine.length() >= 4;

        String taskType = taskLine.substring(0, 3);

        return switch (taskType) {
            case "[T]" -> CommandType.TODO;
            case "[D]" -> CommandType.DEADLINE;
            case "[E]" -> CommandType.EVENT;
            default -> throw new FileException.FileCorruptedException();
        };
    }

    /*
     * Get task is marked as done or undone from task information
     */
    private static boolean taskIsDone(String taskLine) throws FileException {
        assert taskLine != null;
        assert taskLine.length() >= 7;

        String taskIsDone = taskLine.substring(3, 6);

        if (taskIsDone.equals("[X]") || taskIsDone.equals("[ ]")) {
            return taskIsDone.charAt(1) == 'X';
        }

        throw new FileException.FileCorruptedException();
    }
}
