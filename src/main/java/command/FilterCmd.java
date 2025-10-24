package command;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import exception.InvalidDateTimeException;
import exception.InvalidFilterException;
import manager.TaskManager;
import message.FilteredListMessage;
import message.Message;
import parser.commandargs.TaskFilterParser;
import task.ReadOnlyTask;

/**
 * Command to filter tasks based on specified criteria (task type, completion status, date).
 * <p>
 * This command allows users to retrieve tasks that match one or more filter conditions.
 * All specified criteria are combined using logical AND, meaning tasks must satisfy
 * all conditions to be included in the results.
 * <p>
 * <strong>Usage Notes:</strong>
 * <ol>
 *     <li>At least one filter criterion must be provided</li>
 *     <li>Maximum of 3 filter criteria can be applied simultaneously</li>
 *     <li>Multiple criteria are combined using logical AND. Example:
 *     {@code task:todo & done:true} returns only Todo task that are completed</li>
 *     <li>Conflicting criteria (e.g. {@code task:todo & task:event}) will return empty results
 *     since no task can satisfy contradictory conditions</li>
 * </ol><p>
 * <strong>Supported Filter Criteria:</strong>
 * <ul>
 *     <li>filter by task type - {@code task:todo|deadline|event}</li>
 *     <li>filter by completion status - {@code done:true|false}</li>
 *     <li>filter by date - {@code date:YYYY-MM-DD}</li>
 */
public class FilterCmd extends BaseTaskCommand {
    public FilterCmd(TaskManager taskManager, String args) {
        super(taskManager, args);
    }

    /**
     * Builds a filtered message containing tasks that match the specified criteria.
     * <p>
     * This method parses the filter arguments into predicates, applies them to the master task list,
     * and materialize recurring task to specific instance if a date filter is present.
     *
     * @return {@link FilteredListMessage} containing tasks matching all criteria
     * @throws InvalidFilterException   if the filter syntax is invalid or unsupported
     * @throws InvalidDateTimeException if a date filter contains an invalid date format
     * @see TaskFilterParser#chainPredicate(String)
     * @see TaskManager#filter(Predicate)
     */
    @Override
    public Message executes() throws InvalidFilterException, InvalidDateTimeException {
        assert args != null : "Arguments must not be null";

        Predicate<ReadOnlyTask> predicates = TaskFilterParser.chainPredicate(args);
        List<ReadOnlyTask> filteredList = taskManager.filter(predicates);
        List<ReadOnlyTask> finalFilteredList = expandRecurringTasks(filteredList, args);
        return new FilteredListMessage(finalFilteredList, args);
    }

    @Override
    protected CommandType getCommandType() {
        return CommandType.FILTER;
    }

    /**
     * Expands recurring tasks to specific instances for the filtered date.
     * <p>
     * If the filter arguments contain a date criterion, this method creates
     * specific task instances for that date from any recurring tasks in the
     * filtered list. If no date filter is present, the original filtered list is returned.
     *
     * @param filteredList the list of tasks after applying filter predicates
     * @param args         the filter criteria string, potentially containing a date filter
     * @return a list with recurring tasks expanded to specific date instances,
     *         or the original list if no date filter is present
     * @throws InvalidDateTimeException if the date filter contains an invalid date format
     */
    private List<ReadOnlyTask> expandRecurringTasks(List<ReadOnlyTask> filteredList, String args)
            throws InvalidDateTimeException {
        return TaskFilterParser.extractFilterDate(args)
                .map(date -> filteredList.stream()
                        .map(task -> {
                            try {
                                return task.createInstance(date);
                            } catch (InvalidDateTimeException e) {
                                return Optional.<ReadOnlyTask>empty();
                            }
                        })
                        .flatMap(Optional::stream)
                        .toList())
                .orElse(filteredList);
    }
}
