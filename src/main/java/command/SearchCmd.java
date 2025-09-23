package command;

import exception.MeeBotException;
import manager.TaskManager;
import message.Message;
import message.SearchResultMessage;
import task.Task;
import parser.TokenizerUtil;

import java.util.List;
import java.util.regex.Pattern;

public class SearchCmd extends BaseTaskCommand {
    private static final Pattern SEARCH_PATTERN = Pattern.compile("(.+)");

    public SearchCmd(TaskManager taskManager, String args) {
        super(taskManager, args);
    }

    @Override
    public Message execute() {
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
