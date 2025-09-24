package parser;

import exception.InvalidDateTimeException;
import exception.InvalidFilterException;
import task.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Utility class for converting string arguments into task filter {@link Predicate}
 * objects that can be used to filter task collections.
 * <p>
 * <strong>Supported Filter Types:</strong><ul>
 * <li><strong>task:</strong> Filters by task type keyword (case-insensitive)</li>
 * <li><strong>done:</strong> Filters by completion status (true/false)</li>
 * <li><strong>date:</strong> Filters by date (matches any task with the specified date)</li>
 * </ul>
 * <p><strong>Thread Safety:</strong> This class is thread-safe as it contains only static methods
 * and maintains no mutable state.</p>
 */
public final class TaskFilterParser {
    private TaskFilterParser() {}

    /**
     * Parses filter argument string into a combined predicate. All predicates are combined
     * using logical AND, meaning tasks must satisfy ALL criteria to pass the filter.
     * <p>
     * <em>Predicate chaining technique referenced from:
     * <a href="https://www.baeldung.com/java-predicate-chain"></a>
     * Baeldung: Java Predicate Chain</em></p>
     *
     * @param args filter criteria string with ampersands(&) delimited filters
     *             (e.g. "task:deadline & done:true")
     * @return combined predicate that applies all filters with AND logic
     * @throws InvalidFilterException   if filter format is invalid or too many filters are provided
     * @throws InvalidDateTimeException if date filter contains invalid date format
     */
    public static Predicate<Task> chainPredicate(String args)
            throws InvalidFilterException, InvalidDateTimeException {
        // "task:deadline & done:true & date:2024-01-15" → ["task:deadline", "done:true", "date:2024-01-15"]
        String[] tokens = args.trim().split("\\s*&\\s*");
        if (tokens.length > 3) {
            throw new InvalidFilterException(InvalidFilterException.ErrorType.TOO_MANY_FILTERS);
        }

        List<Predicate<Task>> predicates = new ArrayList<>();
        for (String token : tokens) {
            predicates.add(parseFilterToken(token));
        }
        // Chain all predicates with AND logic
        Predicate<Task> chained = task -> true;
        for (Predicate<Task> p : predicates) {
            chained = chained.and(p);
        }
        return chained;
    }

    /**
     * Parses a single filter token (key:value pair) into a task predicate.
     * Both key and value must be non-empty.
     *
     * @param token filter token in format "key:value"
     * @return predicate for the specified filter criteria
     * @throws InvalidFilterException   if token format is invalid (wrong format, empty key/value)
     * @throws InvalidDateTimeException if date filter contains invalid date format
     */
    public static Predicate<Task> parseFilterToken(String token)
            throws InvalidFilterException, InvalidDateTimeException {
        // "task:deadline" → ["task", "deadline"]
        String[] parts = token.split(":");
        String key = parts[0].trim().toLowerCase();
        String value = parts.length > 1 ? parts[1].trim() : "";

        if (parts.length != 2 || key.isEmpty() || value.isEmpty()) {
            throw new InvalidFilterException(InvalidFilterException.ErrorType.INVALID_FILTER_FORMAT);
        }
        return createPredicate(key, value);
    }

    /**
     * Factory for creating appropriate predicate based on filter key and value.
     * Each predicate type has specific validation and matching logic appropriate for its data type.
     * <p>
     * <strong>Implementation Note:</strong> Date filtering ignores time components and matches
     * on date only.</p>
     *
     * @param key   filter key (task, done, date)
     * @param value filter value
     * @return predicate that matches the specified criteria
     * @throws InvalidFilterException   for unknown keys or invalid values
     * @throws InvalidDateTimeException if date value cannot be parsed
     */
    public static Predicate<Task> createPredicate(String key, String value)
            throws InvalidFilterException, InvalidDateTimeException {

        switch (key) {
        case "task":
            return task -> task.getTaskType().getKeyword().equalsIgnoreCase(value);

        case "done":
            boolean isDone = Boolean.parseBoolean(value);
            return task -> task.isDone() == isDone;

        case "date":
            ParsedDateTime parsed = DateTimeParser.parse(value);
            LocalDate filterDate = parsed.dateTime().toLocalDate(); // ignore time

            return task -> {
                List<LocalDateTime> dates = task.getDates();
                if (dates.isEmpty()) return false;

                // handle all dates in between start and end of event
                if (dates.size() == 2) {
                    LocalDate start = dates.get(0).toLocalDate();
                    LocalDate end   = dates.get(1).toLocalDate();
                    return !filterDate.isBefore(start) && !filterDate.isAfter(end);
                } else {
                    return dates.get(0).toLocalDate().equals(filterDate);
                }
            };

        default:
            throw new InvalidFilterException(InvalidFilterException.ErrorType.UNKNOWN_FILTER_KEY);
        }
    }
}
