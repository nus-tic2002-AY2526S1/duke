package command;

import common.ErrorMessage;
import exception.MeeBotException;
import manager.TaskManager;
import message.FilteredListMessage;
import message.Message;
import parser.TaskFilterParser;
import task.Task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Command to filter tasks based on specified criteria (task type, completion status, date).
 * <p>
 * <strong>Usage Notes:</strong>
 * <ol>
 *     <li>At least one filter criterion must be provided</li>
 *     <li>Maximum of 3 filter criteria can be applied simultaneously</li>
 *     <li>Multiple criteria are combined using logical AND. Example:
 *     {@code task:todo & done:true} returns only Todo task that are completed</li>
 *     <li>Conflicting criteria (e.g. {@code task:todo & task:event}) will return empty results
 *     since no task can satisfy contradictory conditions</li>
 */
public class FilterCmd extends BaseTaskCommand {
    public FilterCmd(TaskManager taskManager, String args) {
        super(taskManager, args);
    }

    /**
     * Filters task based on provided criteria and returns matching results.
     * <p>Supported filter criteria:
     * <ol>
     *     <li>{@code task:todo|deadline|event} - filter by task type</li>
     *     <li>{@code done:true|false} - filter by completion status</li>
     *     <li>{@code date:YYYY-MM-DD} - filter by date</li>
     * </ol>
     *
     * @return {@link FilteredListMessage} containing tasks matching all criteria, or
     *         {@link ErrorMessage} if validation fails or no criteria are provided
     * @see TaskFilterParser#chainPredicate(String)
     * @see TaskManager#filter(Predicate)
     */
    @Override
    public Message execute() {
        Message help = showHelpText(CommandType.FILTER);
        if (help != null) return help;
        if (taskManager.isEmpty()) {
            return new ErrorMessage(ErrorMessage.EMPTY_LIST);
        }
        try {
            Predicate<Task> predicates = TaskFilterParser.chainPredicate(args);
            List<Task> filteredList = taskManager.filter(predicates);

            Optional<LocalDate> dateFilter = TaskFilterParser.extractFilterDate(args);
            List<Task> instanceList;
            // Try to extract a date, only materialize instances when a date filter exists
            if (dateFilter.isPresent()) {
                LocalDate date = dateFilter.get();
                instanceList = new ArrayList<>(filteredList.size());
                for (Task filteredTask : filteredList) {
                    filteredTask.createInstance(date).ifPresent(instanceList::add);
                }
            } else {
                // No date filter provided, show initial filter list as-is
                instanceList = filteredList;
            }
            return new FilteredListMessage(instanceList, args);
        } catch (MeeBotException e) {
            return e.toErrorMessage();
        }
    }
}
