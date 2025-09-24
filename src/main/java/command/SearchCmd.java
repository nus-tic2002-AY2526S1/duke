package command;

import common.ErrorMessage;
import exception.MeeBotException;
import manager.TaskManager;
import message.FilteredListMessage;
import message.Message;
import message.SearchResultMessage;
import parser.TokenizerUtil;
import task.Task;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Command that performs a search operation over task.
 */
public class SearchCmd extends BaseTaskCommand {
    private static final Pattern SEARCH_PATTERN = Pattern.compile("(.+)");

    public SearchCmd(TaskManager taskManager, String args) {
        super(taskManager, args);
    }

    /**
     * Search for tasks by extracting keywords from user input and retrieving
     * matching tasks from the {@link TaskManager}.
     * <p>
     * Supports multiple keywords separated by whitespace {@code "search keyword1 keyword2 ..."}.
     * A task will be included in the results if its description matches
     * <em>any</em> of the keywords (OR logic).
     * <p>
     * <b>Matching rules</b>
     * <ul>
     * <li>Case-insensitive: e.g. {@code "MEETING"} matches "meeting".</li>
     * <li>Partial matches allowed: e.g. {@code "proj"} matches "project".</li>
     * <li>Whitespace-delimited keywords: e.g. {@code "search proj urgent"}
     *     will match tasks containing either "proj" or "urgent".</li>
     * </ul>
     *
     * @return {@link FilteredListMessage} containing tasks matching search terms, or
     *         {@link ErrorMessage} if validation fails or no criteria are provided
     * @see TaskManager#search(String[])
     * @see TaskManager#filter(Predicate)
     */
    @Override
    public Message execute() {
        Message help = showHelpText(CommandType.SEARCH);
        if (help != null) {
            return help;
        }
        try {
            String[] tokens = TokenizerUtil.tokenize(
                    args, SEARCH_PATTERN, 1, null
            );
            String[] keywords = tokens[0].toLowerCase().split("\\s+");
            List<Task> results = taskManager.search(keywords);
            return new SearchResultMessage(results, keywords);
        } catch (MeeBotException e) {
            return e.toErrorMessage();
        }
    }
}
