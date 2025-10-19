package task;

import exception.InvalidDateTimeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import parser.datetime.ParsedDateTime;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * To test for:
 * <ol>
 * <li>Polymorphism: copy() returns the right subclass automatically</li>
 * <li>Independence: clones are not the same reference as originals</li>
 * <li>Value equality: methods returns equal data</li>
 * </ol>
 */
class CloneTaskTest {

    @Test
    @DisplayName("Cloned events should preserve description and date/time, but no recurrence")
    void eventTaskClone() throws InvalidDateTimeException {
        ParsedDateTime start = new ParsedDateTime(
                LocalDateTime.of(2025, 9, 28, 10, 10), true);
        ParsedDateTime end = new ParsedDateTime(
                LocalDateTime.of(2025, 9, 28, 11, 10), true);
        Recurrence r = new Recurrence(
                Recurrence.RecurrenceType.WEEKLY, 5, start.dateTime().toLocalDate(), null);

        Task original = new EventTask("yoga", start, end, r);
        Task clone = original.copy(start.dateTime(), end.dateTime());

        // Test: description, completion, date-time
        assertEquals(original.getDescription(), clone.getDescription());
        assertEquals(original.isDone(), clone.isDone());
        assertEquals(original.getDates(), clone.getDates());

        // Test: different object references
        assertNotSame(original, clone);
        assertNotSame(original.getRecurrence(), clone.getRecurrence());

        // Test: recurrence reset
        assertNotNull(clone.getRecurrence());
        assertEquals(Recurrence.RecurrenceType.NONE, clone.getRecurrence().type());
    }

    @Test
    @DisplayName("Todo clones should preserve description only and no recurrence")
    void todoTaskClone() throws InvalidDateTimeException {
        Task original = new TodoTask("pay bills", null);
        // Any start/end passed in should be irrelevant
        LocalDateTime dummyStart = LocalDateTime.of(2025, 9, 28, 10, 0);
        LocalDateTime dummyEnd   = LocalDateTime.of(2025, 9, 28, 10, 0);
        Task clone = original.copy(dummyStart, dummyEnd);

        // Test: description date-time completion
        assertEquals(original.getDates(), clone.getDates());
        assertEquals(original.isDone(), clone.isDone());
        assertEquals(original.getDates(), clone.getDates());

        // Test different object references
        assertNotSame(original, clone);
        assertNotSame(original.getRecurrence(), clone.getRecurrence());

        assertNotNull(clone.getRecurrence());
        assertEquals(Recurrence.RecurrenceType.NONE, clone.getRecurrence().type());
    }
}
