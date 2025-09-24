package command;

import common.ErrorMessage;
import exception.InvalidTaskFormatException.ErrorType;
import exception.MeeBotException;
import manager.TaskManager;
import message.Message;
import message.TaskAddedMessage;
import parser.DateTimeParser;
import parser.ParsedDateTime;
import parser.RecurrenceParser;
import parser.TokenizerUtil;
import task.DeadlineTask;
import task.Recurrence;
import task.Task;

import java.util.regex.Pattern;


/**
 * Command to add a new deadline task with a due date and optional recurrence.
 *
 * @see TaskManager#addTask(Task)
 */
public class AddDeadlineCmd extends BaseTaskCommand {

    /**
     * Regular expression pattern to parse deadline command input.
     *
     * @apiNote Pattern generated with AI assistance for optimal matching
     */
    private static final Pattern DEADLINE_PATTERN = Pattern.compile(
            "(.+?)\\s*/by\\s*(.+)",
            Pattern.CASE_INSENSITIVE
    );

    public AddDeadlineCmd(TaskManager taskManager, String args) {
        super(taskManager, args);
    }

    /**
     * Executes the deadline task creation command.
     * <p>
     * Parses user input in the format {@code "description /by dateTime [recurrence]"}
     * to extract the task description, date/time and optional recurrence pattern.
     * Creates a new {@link DeadlineTask}, and adds it to the {@link TaskManager}.
     *
     * @return {@link TaskAddedMessage} on successful task creation, or
     *         {@link ErrorMessage} on invalid command format or date parsing
     * @implNote Captures task manager's sorted state before modification to provide
     *         accurate context in return messages
     */
    @Override
    public Message execute() {
        Message help = showHelpText(CommandType.DEADLINE);
        if (help != null) {
            return help;
        }
        try {
            String[] tokens = TokenizerUtil.tokenize(
                    args, DEADLINE_PATTERN, 2, ErrorType.DEADLINE
            );
            ParsedDateTime parsed = DateTimeParser.parse(tokens[1]);
            Recurrence recurrence = RecurrenceParser.parse(
                    tokens[tokens.length - 1], parsed.dateTime().toLocalDate(), ErrorType.RECURRENCE
            );

            Task deadline = new DeadlineTask(tokens[0], parsed, recurrence);
            boolean wasSorted = taskManager.isSorted();
            taskManager.addTask(deadline);
            return new TaskAddedMessage(deadline, taskManager, wasSorted);
        } catch (MeeBotException e) {
            return e.toErrorMessage();
        }
    }
}
