package task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import model.task.Recurrence;
import model.task.Recurrence.RecurrenceType;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RecurrenceTest {

    @Test
    @DisplayName("A recurrence instantiated with type:null should default to NONE")
    void nullTypeDefaultsToNone() {
        Recurrence r1 = new Recurrence(null, 0, LocalDate.of(2025, 11, 11), null);
        Recurrence r2 = Recurrence.none(LocalDate.of(2025, 11, 11));
        assertEquals(RecurrenceType.NONE, r1.type());
        assertEquals(r1, r2);   // value-based equality should hold
    }

    @Test
    @DisplayName("Should not create recurrence when frequency is negative")
    void invalidWhenNegativeCount() {
        assertThrows(IllegalArgumentException.class,
                () -> new Recurrence(RecurrenceType.DAILY, -1,
                        LocalDate.of(2025, 11, 11), null));
    }

    @Test
    @DisplayName("A recurrence with anchorDate:null should have same recurEndDate and default to today's date")
    void nullDateDefaultsToToday() {
        Recurrence r = Recurrence.none(null);
        assertEquals(RecurrenceType.NONE, r.type());
        assertEquals(LocalDate.now(), r.anchorDate());
        assertEquals(LocalDate.now(), r.recurrenceEnd());
    }

    @Test
    @DisplayName("A recurrence with frequency=0 should collapse to anchorDate")
    void zeroFrequencyCollapsesToAnchor() {
        LocalDate anchor = LocalDate.of(2025, 11, 11);
        Recurrence r = new Recurrence(RecurrenceType.DAILY, 0, anchor, null);
        assertEquals(anchor, r.recurrenceEnd());
    }

    @Test
    @DisplayName("Two value objects are equal when they have the same value, not necessarily being the same object.")
    void recurrenceWithSameValuesAreEqual() {
        Recurrence r1 = new Recurrence(RecurrenceType.WEEKLY, 2,
                LocalDate.of(2025, 11, 11), null);
        Recurrence r2 = new Recurrence(RecurrenceType.WEEKLY, 2,
                LocalDate.of(2025, 11, 11), null);
        assertEquals(r1, r2);                       // value-based equality
        assertEquals(r1.hashCode(), r2.hashCode()); // consistent with equals
        assertNotSame(r1, r2);                      // different instances
    }

    @Test
    @DisplayName("Should produce a valid no recurrence instance (type=NONE, repeatCount=0")
    void noneFactoryProducesNone() {
        Recurrence r = Recurrence.none(LocalDate.of(2025, 11, 11));
        assertTrue(r.isNone());
        assertEquals(RecurrenceType.NONE, r.type());
        assertEquals(0, r.frequency());
        assertEquals(LocalDate.of(2025, 11, 11), r.recurrenceEnd());
    }

    @Test
    @DisplayName("Should produce a daily recurrence with type, frequency count, anchor and recurring end")
    void createDailyRecurrenceWorks() {
        Recurrence r = new Recurrence(RecurrenceType.DAILY, 3,
                LocalDate.of(2025, 11, 11), null);

        assertEquals(RecurrenceType.DAILY, r.type());
        assertEquals(3, r.frequency());
        assertEquals(LocalDate.of(2025, 11, 11), r.anchorDate());
        assertEquals(LocalDate.of(2025, 11, 13), r.recurrenceEnd());
    }

    @Test
    @DisplayName("Monthly recurrence handles month-end correctly")
    void monthlyRecurrenceHandlesMonthEnd() {
        LocalDate anchor = LocalDate.of(2025, 1, 31);
        Recurrence r = new Recurrence(RecurrenceType.MONTHLY, 2, anchor, null);
        // The 2 occurrences: 2025-01-31, 2025-02-28
        assertEquals(LocalDate.of(2025, 2, 28), anchor.plusMonths(1)); // implicit check
        assertEquals(LocalDate.of(2025, 1, 31).plusMonths(1), r.recurrenceEnd());
    }

    @Test
    @DisplayName("Yearly recurrence on leap year should default to 28 Feb subsequently")
    void yearlyRecurrenceHandlesLeapYear() {
        LocalDate anchor = LocalDate.of(2024, 2, 29);
        Recurrence r = new Recurrence(RecurrenceType.YEARLY, 3, anchor, null);
        // The 3 occurrences: 2024-02-29, 2025-02-28, 2026-02-28
        assertEquals(LocalDate.of(2026, 2, 28), anchor.plusYears(2));   // freq-1
        assertEquals(LocalDate.of(2024, 2, 29).plusYears(2), r.recurrenceEnd());
    }
}
