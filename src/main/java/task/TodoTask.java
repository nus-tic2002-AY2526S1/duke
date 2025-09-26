package task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Simple task without date/time constraints.
 */
public class TodoTask extends Task {
    public TodoTask(String description, Recurrence recurrence) {
        super(description, recurrence);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.TODO;
    }

    @Override
    public List<LocalDateTime> getDates() {
        return Collections.emptyList();
    }

    @Override
    public String toJsonFields() {
        return "";
    }

    /**
     * Creates a copy of this todo task with no recurrence.
     *
     * @return a new {@link TodoTask} instance with the same description,
     * but with recurrence set to {@link Recurrence#none(LocalDate)}
     */
    @Override
    protected Task copy(LocalDateTime start, LocalDateTime end) {
        return new TodoTask(this.getDescription(), Recurrence.none(LocalDate.now()));
    }
}
