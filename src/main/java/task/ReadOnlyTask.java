package task;

import exception.InvalidDateTimeException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReadOnlyTask {
    TaskType getTaskType();

    List<LocalDateTime> getDates();

    String getDescription();

    Recurrence getRecurrence();

    boolean isDone();

    String toJsonFields();

    boolean occursOn(LocalDate filterDate);

    Optional<ReadOnlyTask> createInstance(LocalDate filterDate) throws InvalidDateTimeException;
}
