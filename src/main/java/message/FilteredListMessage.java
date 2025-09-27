package message;

import task.ReadOnlyTask;

import java.util.List;

/**
 * Message displaying filtered task results with 1-based numbering and filter criteria summary.
 */
public class FilteredListMessage implements Message {
    private final List<ReadOnlyTask> tasks;
    private final String filterCriteria;

    /**
     * @param tasks          filtered task list (may be empty); this list is copied defensively
     * @param filterCriteria original filter criteria for display
     */
    public FilteredListMessage(List<ReadOnlyTask> tasks, String filterCriteria) {
        this.tasks = List.copyOf(tasks);
        this.filterCriteria = filterCriteria;
    }

    /**
     * Generates formatted message showing filter criteria and numbered task results.
     * <p>
     * If no tasks match the criteria, returns a helpful message explaining possible
     * reasons. Otherwise, displays the filter criteria followed by a numbered list
     * of matching tasks.
     *
     * @return formatted message with filter summary and task list, or empty result
     *         explanation if no tasks match
     */
    @Override
    public String message() {
        if (tasks.isEmpty()) {
            return String.format("""
                    '%s' came back empty-handed.
                    This could mean: (1) no matching tasks, or (2) your filter values are incorrect.
                    """, filterCriteria);
        }

        StringBuilder content = new StringBuilder();
        if (filterCriteria != null && !filterCriteria.isBlank()) {
            content.append("Filtered by: ").append(filterCriteria).append("\n");
        }
        for (int i = 0; i < tasks.size(); i++) {
            ReadOnlyTask task = tasks.get(i);
            content.append(String.format("%d. %s\n", i + 1, task.toString()));
        }
        return content.toString().trim();
    }
}
