package command;

import common.ErrorMessage;
import exception.InvalidTaskFormatException.ErrorType;
import exception.MeeBotException;
import manager.TaskManager;
import message.Message;
import message.TaskAddedMessage;
import parser.datetime.DateTimeParser;
import parser.datetime.ParsedDateTime;
import parser.taskops.RecurrenceParser;
import parser.taskops.StringTokenizer;
import task.EventTask;
import task.Recurrence;
import task.Task;

import java.util.regex.Pattern;

/**
 * Command to add a new event task with a time period and optional recurrence.
 *
 * @see TaskManager#addTask(Task)
 */
public class AddEventCmd extends BaseTaskCommand {

    /**
     * Regular expression pattern to parse event command input.
     *
     * @apiNote Pattern generated with AI assistance for optimal matching
     */
    private static final Pattern EVENT_PATTERN = Pattern.compile(
            "(.+?)\\s*/from\\s*(.+?)\\s*/to\\s*(.+)",
            Pattern.CASE_INSENSITIVE
    );

    public AddEventCmd(TaskManager taskManager, String args) {
        super(taskManager, args);
    }

    /**
     * Executes the event task creation command.
     * <p>
     * Parses user input in the format {@code "description /from dateTime /to dateTime [recurrence]"}
     * to extract the task description, date/time and optional recurrence pattern.
     * Creates a new {@link EventTask}, and adds it to the {@link TaskManager}.
     *
     * @return {@link TaskAddedMessage} on successful task creation, or
     *         {@link ErrorMessage} on invalid command format or date parsing
     * @implNote Captures task manager's sorted state before modification to provide
     *         accurate context in return messages
     */
    @Override
    public Message execute() {
        Message help = showHelpText(CommandType.EVENT);
        if (help != null) return help;
        try {
            String[] tokens = StringTokenizer.tokenize(
                    args, EVENT_PATTERN, 3, ErrorType.EVENT
            );
            ParsedDateTime start = DateTimeParser.parse(tokens[1]);
            ParsedDateTime end = DateTimeParser.parse(tokens[2]);
            Recurrence recurrence = RecurrenceParser.parse(
                    tokens[tokens.length - 1], end.dateTime().toLocalDate(), ErrorType.RECURRENCE
            );

            Task event = new EventTask(tokens[0], start, end, recurrence);
            boolean wasSorted = taskManager.isSorted();
            taskManager.addTask(event);
            return new TaskAddedMessage(event, taskManager, wasSorted);
        } catch (MeeBotException e) {
            return e.toErrorMessage();
        }
    }
}
