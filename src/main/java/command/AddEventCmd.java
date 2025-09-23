package command;

import exception.InvalidTaskFormatException.ErrorType;
import exception.MeeBotException;
import manager.TaskManager;
import message.ErrorMessage;
import message.Message;
import message.TaskAddedMessage;
import task.EventTask;
import task.Recurrence;
import task.Task;
import util.DateTimeParser;
import util.ParsedDateTime;
import util.RecurrenceParser;
import util.TokenizerUtil;

import java.util.regex.Pattern;

/**
 * Command to add a new event task with a time period.
 */
public class AddEventCmd extends BaseTaskCommand {

    /* Lazy regex syntax generated with the help of ChatGPT */
    private static final Pattern EVENT_PATTERN = Pattern.compile(
            "(.+?)\\s*/from\\s*(.+?)\\s*/to\\s*(.+)",
            Pattern.CASE_INSENSITIVE
    );

    public AddEventCmd(TaskManager taskManager, String args) {
        super(taskManager, args);
    }

    /**
     * Creates a deadline task from user input: "description /from dateTime /to dateTime".
     * <p>Parses the input string to extract the task description and date/time,
     * creates a new {@link EventTask}, and adds it to the {@link TaskManager}.
     *
     * @return {@link TaskAddedMessage} on successful task creation, or
     *         {@link ErrorMessage} on invalid format or date parsing
     */
    @Override
    public Message execute() {
        try {
            // Split input: "attend event /from 11/11/2011 /to 12/11/2011" → ["attend event", "11/11/2011", "12/11/2011"]
            String[] tokens = TokenizerUtil.tokenize(
                    args, EVENT_PATTERN, 3, ErrorType.EVENT
            );
            Recurrence recurrence = RecurrenceParser.parse(
                    tokens[tokens.length - 1], ErrorType.RECURRENCE
            );
            ParsedDateTime start = DateTimeParser.parse(tokens[1]);
            ParsedDateTime end = DateTimeParser.parse(tokens[2]);

            Task event = new EventTask(tokens[0], start, end, recurrence);
            boolean wasSorted = taskManager.isSorted();
            taskManager.addTask(event);
            return new TaskAddedMessage(event, taskManager, wasSorted);
        } catch (MeeBotException e) {
            return e.toErrorMessage();
        }
    }
}
