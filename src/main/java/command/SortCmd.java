package command;

import common.ErrorMessage;
import exception.InvalidTaskFormatException.ErrorType;
import exception.MeeBotException;
import manager.TaskManager;
import message.ListTaskMessage;
import message.Message;
import parser.commandargs.StringTokenizer;

import java.util.regex.Pattern;

/**
 * Command class for sorting tasks by specified sorting mode. Sorting operations
 * are stable and the sorted order will persist until the list is modified.
 * <p>
 * New tasks are always inserted at the bottom of the list, so a new sort command
 * must be executed to maintain the desired ordering after adding tasks.
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
     * <p>Supported sorting criteria:
     * <li>{@code /by date} - sorts by first date in chronological order (tasks without dates appear last).</li>
     * <li>{@code /by status} - sorts by completion status (incomplete tasks first).</li>
     *
     * @return {@link ListTaskMessage} containing the sorted task list, or
     *         {@link ErrorMessage} if validation fails or no criteria are provided
     */
    @Override
    public Message execute() {
        Message help = showHelpText(CommandType.SORT);
        if (help != null) return help;
        if (taskManager.isEmpty()) {
            return new ErrorMessage(ErrorMessage.EMPTY_LIST);
        }
        try {
            String[] tokens = StringTokenizer.tokenize(
                    args, SORT_PATTERN, 1, ErrorType.SORT
            );
            String sortMode = tokens[0].toLowerCase();
            if (sortMode.equals("status")) {
                taskManager.sortByStatus();
            } else {
                taskManager.sortByDate();
            }
        } catch (MeeBotException e) {
            return e.toErrorMessage();
        }
        return new ListTaskMessage(taskManager);
    }
}
