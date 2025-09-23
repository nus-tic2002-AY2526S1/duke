package task;

/**
 * Immutable value object representing a recurrence rule for a task.
 * This record encapsulates the type and frequency, defining how often a task repeats.
 *
 * @param type the type of recurrence (NONE, DAILY, WEEKLY, MONTHLY, YEARLY)
 * @param frequency how often the recurrence occurs (must be non-negative)
 */
public record Recurrence(
        RecurrenceType type,
        int frequency
) {
    public Recurrence {
        if (type == null) type = RecurrenceType.NONE;
        if (frequency < 0) throw new IllegalArgumentException("Repeat freq must be >0");
    }

    /**
     * Checks if this recurrence represents no repetition.
     *
     * @return true if the recurrence type is NONE, false otherwise
     */
    public boolean isNone() {
        return type == RecurrenceType.NONE;
    }

    /**
     * Creates a {@code Recurrence} instance that represents no repetition.
     * This is a convenience factory method equivalent to using type {@link RecurrenceType#NONE}
     * with a frequency of {@code 0}.
     *
     * @return a Recurrence instance with type NONE and frequency 0
     */
    public static Recurrence none() {
        return new Recurrence(RecurrenceType.NONE, 0);
    }

    /**
     * Defines the recurrence patterns, specifying the different intervals at which a task can repeat.
     */
    public enum RecurrenceType {
        NONE, DAILY, WEEKLY, MONTHLY, YEARLY
    }
}
