package commands;

import enums.CommandType;
import exceptions.TaskNumberException;
import parser.userInputParser.TaskNumberParser;
import tasks.Task;
import ui.UserInteraction;

import java.util.AbstractMap;
import java.util.ArrayList;

/**
 * Used for deletion of task from task list
 */
public class DeleteCommand extends Command {
    private final int taskIndex;

    /**
     * Populate parameter required
     *
     * @param commandLine parsed user's input
     * @param tasklist    task list from task manager
     * @throws TaskNumberException if there are any errors in populating
     */
    protected DeleteCommand(AbstractMap.SimpleEntry<CommandType,
            ArrayList<String>> commandLine, ArrayList<Task> tasklist) throws TaskNumberException {
        super(tasklist);

        assert commandLine != null;
        assert commandLine.getValue() != null;
        assert commandLine.getValue().size() == 1;

        String taskNum = commandLine.getValue().get(0);
        int taskNumber = TaskNumberParser.getTaskNumber(taskNum, tasklist.size());
        this.taskIndex = taskNumber - 1;
    }

    /**
     * Delete task from task list
     */
    public void deleteTask() {
        Task task = tasklist.get(taskIndex);

        tasklist.remove(taskIndex);

        UserInteraction.printMessage("Task deleted: " + task,
                "Now you have " + tasklist.size() + " tasks in the list.");
    }

    /**
     * Execution of command
     */
    @Override
    public void execute() {
        deleteTask();
    }
}
