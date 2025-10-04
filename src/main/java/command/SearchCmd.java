package command;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import exception.InvalidTaskFormatException;
import manager.TaskManager;
import message.Message;
import message.SearchResultMessage;
import parser.commandargs.StringTokenizer;
import task.ReadOnlyTask;

/**
 * Command to search for tasks by keywords in their descriptions.
 * <p>
 * This command performs a case-insensitive, partial-match search across task descriptions.
 * Multiple keywords are combined using OR logic, meaning a task matches if its description
 * contains <em>any</em> of the provided keywords.
 * <p>
 * <strong>Matching Rules:</strong>
 * <ul>
 *     <li><strong>Case-insensitive:</strong> {@code "MEETING"} matches "meeting"</li>
 *     <li><strong>Partial matches:</strong> {@code "proj"} matches "project"</li>
 *     <li><strong>OR logic:</strong> Multiple keywords match if any keyword is found</li>
 *     <li><strong>Whitespace-separated:</strong> Keywords are split by spaces</li>
 * </ul>
 */
public class SearchCmd extends BaseTaskCommand {
    private static final Pattern SEARCH_PATTERN = Pattern.compile("(.+)");

    public SearchCmd(TaskManager taskManager, String args) {
        super(taskManager, args);
    }

    /**
     * Search for tasks by extracting keywords from user input and retrieving
     * matching tasks from the {@link TaskManager}.
     *
     * @return {@link SearchResultMessage} containing tasks matching any of the search terms
     * @throws InvalidTaskFormatException if the search query format is invalid
     * @see TaskManager#search(String[])
     * @see TaskManager#filter(Predicate)
     */
    @Override
    public Message executes() throws InvalidTaskFormatException {
        String[] tokens = StringTokenizer.tokenize(
                args, SEARCH_PATTERN, 1, null
        );
        String[] keywords = tokens[0].toLowerCase().split("\\s+");
        List<ReadOnlyTask> results = taskManager.search(keywords);
        return new SearchResultMessage(results, keywords);
    }

    @Override
    protected CommandType getCommandType() {
        return CommandType.SEARCH;
    }
}
