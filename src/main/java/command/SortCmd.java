package command;

import java.util.regex.Pattern;

import exception.InvalidTaskFormatException;
import exception.InvalidTaskFormatException.ErrorType;
import manager.TaskManager;
import message.ListTaskMessage;
import message.Message;
import parser.commandargs.ArgTokenizer;

/**
 * Command to sort the task list by a specified criterion.
 * <p>
 * Sorting operations are stable and the sorted order persists across the session
 * until the list is modified by operations like adding or marking tasks.
 * New tasks are always inserted at the bottom of the list, so a new sort command
 * must be executed to maintain the desired ordering after adding tasks.
 * <p>
 * <strong>Supported sorting criteria:</strong>
 * <ul>
 *     <li>{@code /by date} - sorts by first date in chronological order (tasks without dates appear last).</li>
 *     <li>{@code /by status} - sorts by completion status (incomplete tasks first).</li>
 *
 * @see TaskManager#sortByDate()
 * @see TaskManager#sortByStatus()
 */
public class SortCmd extends BaseTaskCommand {
    private static final Pattern SORT_PATTERN = Pattern.compile(
            "/by\\s+(date|status)",
            Pattern.CASE_INSENSITIVE
    );

    public SortCmd(TaskManager taskManager, String args) {
        super(taskManager, args);
    }

    /**
     * Sorts task based on provided criteria and returns sorted list.
     *
     * @return {@link ListTaskMessage} containing the sorted task list
     * @throws InvalidTaskFormatException if the sort criterion is invalid
     */
    @Override
    public Message executes() throws InvalidTaskFormatException {
        String[] tokens = ArgTokenizer.tokenize(
                args, SORT_PATTERN, 1, ErrorType.SORT
        );
        String sortMode = tokens[0].toLowerCase();
        if (sortMode.equals("date")) {
            taskManager.sortByDate();
        } else {
            taskManager.sortByStatus();
        }
        return new ListTaskMessage(taskManager);
    }

    @Override
    protected CommandType getCommandType() {
        return CommandType.SORT;
    }
}
