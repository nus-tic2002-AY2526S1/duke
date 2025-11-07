package logic.command;

import java.util.regex.Pattern;

import common.exception.InvalidTaskFormatException;
import common.exception.InvalidTaskFormatException.ErrorType;
import model.TaskManager;
import common.message.ListTaskMessage;
import common.message.Message;
import logic.parser.commandargs.ArgTokenizer;

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
        assert args != null : "Arguments must not be null";

        final Pattern SORT_PATTERN = Pattern.compile(
                "/by\\s+(date|status)", Pattern.CASE_INSENSITIVE
        );
        String sortMode = ArgTokenizer.tokenize(
                args, SORT_PATTERN, 1, ErrorType.SORT)[0]
                .toLowerCase();

        if ("date".equals(sortMode)) {
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
