package model.task;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Immutable value object representing a recurrence rule attached to a task.
 * <p>
 * The recurrence operates on a "sliding window" concept where {@code frequency}
 * defines how many task instances occur within each recurrence period, starting from
 * the {@code anchorDate}.
 * <p>
 * <b>Example:</b>
 * <pre>
 * // Task occurs on the first of each month for 5 months, starting from anchor date
 * Recurrence.of(RecurrenceType.MONTHLY, 5, LocalDate.of(2025, 11, 11))
 * </pre>
 * <em>Conceptual design of recurrence implementation was inspired by:
 * <a href = https://github.com/bmoeskau/Extensible/blob/master/recurrence-overview.md></a>
 * Brian Moeskau's Extensible Calendar</em>
 *
 * @param type          the type of recurrence interval (NONE, DAILY, WEEKLY, MONTHLY, YEARLY)
 * @param frequency     how often the recurrence occurs (must be non-negative)
 * @param anchorDate    the starting date from which recurrence is calculated
 * @param recurrenceEnd the last date on which this occurrence can occur, derived from
 *                      {@code anchorDate} and {@code frequency}
 */
public record Recurrence(
        RecurrenceType type,
        int frequency,
        LocalDate anchorDate,
        LocalDate recurrenceEnd     // derived field, computed in constructor
) {
    /**
     * Compact constructor with validation and field normalization.
     * <p>
     * The constructor automatically computes {@code recurrenceEnd} based on the
     * anchor date, frequency, and recurrence type. For NONE type or zero frequency,
     * the recurrence end date equals the anchor date.
     * <p>
     * The {@code anchorDate} serves as the reference point for recurrence math,
     * and its meaning depends on the type of task:
     * <ul>
     *     <li><b>DeadlineTask</b> — the deadline date itself since there is only one
     *     temporal point.</li>
     *     <li><b>EventTask</b> — the event’s <em>end date</em>, ensuring that the full
     *     event interval (start → end) shifts consistently with each recurrence.</li>
     *     <li><b>TodoTask</b> — todos do not support recurrence. They always uses
     *     {@link Recurrence#none(LocalDate)} with a {@code LocalDate} value that acts
     *     only as a placeholder to satisfy constructor signature, but has no effect on
     *     recurrence logic.</li>
     * </ul>
     *
     * @throws IllegalArgumentException if frequency is negative
     */
    public Recurrence {
        if (type == null) type = RecurrenceType.NONE;
        if (frequency < 0) throw new IllegalArgumentException("Repeat freq must be >0");
        if (anchorDate == null) anchorDate = LocalDate.now();   // for todo

        // Always compute recurrence from anchor + frequency
        if (type == RecurrenceType.NONE || frequency == 0) {
            recurrenceEnd = anchorDate;
        } else {
            ChronoUnit unit = type.getChronoUnit();
            recurrenceEnd = anchorDate.plus((long) frequency - 1, unit);
        }
    }

    /**
     * Creates a non-recurring {@code Recurrence} instance.
     * This is a convenience factory method equivalent to using type {@link RecurrenceType#NONE}
     * with a frequency of {@code 0}.
     *
     * @param anchorDate the single date on which the task occurs
     * @return a Recurrence with type NONE and frequency 0
     * @throws IllegalArgumentException if anchorDate is null
     */
    public static Recurrence none(LocalDate anchorDate) {
        return new Recurrence(RecurrenceType.NONE, 0, anchorDate, anchorDate);
    }

    /**
     * Convenience factory method that creates a recurring Recurrence instance
     * with the specified parameters.
     */
    public static Recurrence of(RecurrenceType type, int frequency, LocalDate anchorStart) {
        return new Recurrence(type, frequency, anchorStart, null);
    }

    /**
     * Checks if this is a non-recurrence pattern.
     *
     * @return {@code true} if the recurrence type is NONE, {@code false} otherwise
     */
    public boolean isNone() {
        return type == RecurrenceType.NONE;
    }

    /**
     * Evaluates whether an occurrence of this recurrence pattern falls on the specified
     * {@code filterDate}, relative to the original start and end dates of the task.
     * <p>
     * <ul>
     *     <li>If this recurrence is {@link RecurrenceType#NONE}, the method returns
     *     {@code true} only if {@code filterDate} lies between {@code originalStart} and
     *     {@code originalEnd}, inclusive.</li>
     *
     *     <li>For repeating recurrences, the method calculates the temporal difference
     *     between {@code originalStart} and {@code filterDate} in the appropriate {@link ChronoUnit}
     *     and checks whether that difference is a valid multiple within the configured frequency.
     *     If so, the shifted interval (from {@code originalStart} to {@code originalEnd})
     *     covers the filter date, and the method returns {@code true}.</li>
     *
     * @param filterDate    the date being tested
     * @param originalStart the original start date of the task
     * @param originalEnd   the original end date of the task (same as start for deadlines)
     * @return {@code true} if the recurrence pattern produces an occurrence
     *         covering {@code filterDate}, otherwise {@code false}
     */
    public boolean occursOn(
            LocalDate filterDate,
            LocalDate originalStart,
            LocalDate originalEnd
    ) {
        if (type == RecurrenceType.NONE) {
            return isWithin(filterDate, originalStart, originalEnd);
        }
        if (recurrenceEnd().isBefore(filterDate)) {
            return false;
        }

        ChronoUnit unit = type.getChronoUnit();
        long timeUnitDiff = unit.between(originalStart, filterDate);
        if (timeUnitDiff < 0 || timeUnitDiff >= frequency) {
            return false;
        }
        LocalDate instanceStart = originalStart.plus(timeUnitDiff, unit);
        LocalDate instanceEnd = originalEnd.plus(timeUnitDiff, unit);

        return isWithin(filterDate, instanceStart, instanceEnd);
    }

    private boolean isWithin(LocalDate date, LocalDate start, LocalDate end) {
        return !date.isBefore(start) && !date.isAfter(end);
    }

    /**
     * Defines the available recurrence patterns for tasks
     * <p>
     * Each type corresponds to a specific time interval and is associated with a
     * {@link ChronoUnit} for date calculations. The {@code NONE} type represents non-recurring tasks.
     */
    public enum RecurrenceType {
        NONE(null),
        DAILY(ChronoUnit.DAYS),
        WEEKLY(ChronoUnit.WEEKS),
        MONTHLY(ChronoUnit.MONTHS),
        YEARLY(ChronoUnit.YEARS);

        private final ChronoUnit unit;

        RecurrenceType(ChronoUnit unit) {
            this.unit = unit;
        }

        /**
         * Returns the {@link ChronoUnit} associated with this recurrence type.
         *
         * @return {@code ChronoUnit} or null if type is NONE
         */
        public ChronoUnit getChronoUnit() {
            return unit;
        }
    }
}
