package command;

import exception.InvalidTaskFormatException.ErrorType;
import exception.MeeBotException;
import manager.TaskManager;
import message.ErrorMessage;
import message.Message;
import message.TaskAddedMessage;
import task.DeadlineTask;
import task.Recurrence;
import task.Task;
import util.DateTimeParser;
import util.ParsedDateTime;
import util.RecurrenceParser;
import util.TokenizerUtil;

import java.util.regex.Pattern;


/**
 * Command to add a new deadline task with a due date.
 */
public class AddDeadlineCmd extends BaseTaskCommand {

    /* Lazy regex syntax generated with the help of ChatGPT */
    private static final Pattern DEADLINE_PATTERN = Pattern.compile(
            "(.+?)\\s*/by\\s*(.+)",
            Pattern.CASE_INSENSITIVE
    );

    public AddDeadlineCmd(TaskManager taskManager, String args) {
        super(taskManager, args);
    }

    /**
     * Creates a deadline task from user input: "description /by dateTime".
     * <p>Parses the input string to extract the task description and date/time,
     * creates a new {@link DeadlineTask}, and adds it to the {@link TaskManager}.
     *
     * @return {@link TaskAddedMessage} on successful task creation, or
     *         {@link ErrorMessage} on invalid format or date parsing
     */
    @Override
    public Message execute() {
        try {
            // Split input: "finish task /by 11/11/2011" → ["finish task", "11/11/2011"]
            String[] tokens = TokenizerUtil.tokenize(
                    args, DEADLINE_PATTERN, 2, ErrorType.DEADLINE
            );
            Recurrence recurrence = RecurrenceParser.parse(
                    tokens[tokens.length - 1], ErrorType.RECURRENCE
            );
            ParsedDateTime parsed = DateTimeParser.parse(tokens[1]);

            Task deadline = new DeadlineTask(tokens[0], parsed, recurrence);
            boolean wasSorted = taskManager.isSorted();
            taskManager.addTask(deadline);
            return new TaskAddedMessage(deadline, taskManager, wasSorted);
        } catch (MeeBotException e) {
            return e.toErrorMessage();
        }
    }
}
