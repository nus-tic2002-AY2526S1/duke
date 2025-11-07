package task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import model.task.Recurrence;
import model.task.Recurrence.RecurrenceType;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RecurrenceOccurOnTest {

    @Test
    @DisplayName("Non-recurring event should only match dates within its range")
    void nonRecurringEvent_matchesOnlyInsideDateRange() {
        LocalDate taskStart = LocalDate.of(2025, 9, 24);
        LocalDate taskEnd = LocalDate.of(2025, 9, 26);
        LocalDate anchor = LocalDate.of(2025, 9, 26);   // anchor is event end
        Recurrence r = Recurrence.none(anchor);

        // Test: Dates inside range
        assertTrue(r.occursOn(LocalDate.of(2025, 9, 24), taskStart, taskEnd));
        assertTrue(r.occursOn(LocalDate.of(2025, 9, 25), taskStart, taskEnd));
        assertTrue(r.occursOn(LocalDate.of(2025, 9, 26), taskStart, taskEnd));

        // Test: Dates outside range
        assertFalse(r.occursOn(LocalDate.of(2025, 9, 23), taskStart, taskEnd));
        assertFalse(r.occursOn(LocalDate.of(2025, 9, 27), taskStart, taskEnd));
    }

    @Test
    @DisplayName("Daily recurring deadline should occur across three consecutive days")
    void dailyRecurringDeadline_occursForThreeDays() {
        LocalDate anchor = LocalDate.of(2025, 9, 24);
        Recurrence r = new Recurrence(RecurrenceType.DAILY, 3, anchor, null);

        assertTrue(r.occursOn(LocalDate.of(2025, 9, 24), anchor, anchor));
        assertTrue(r.occursOn(LocalDate.of(2025, 9, 25), anchor, anchor));
        assertTrue(r.occursOn(LocalDate.of(2025, 9, 26), anchor, anchor));

        // Test: after recurrence end
        assertFalse(r.occursOn(LocalDate.of(2025, 9, 27), anchor, anchor));
    }

    @Test
    @DisplayName("Weekly recurring deadline should occur exactly twice")
    void weeklyRecurringDeadline_OccursForTwoWeeks() {
        LocalDate anchor = LocalDate.of(2025, 9, 24);
        Recurrence r = new Recurrence(RecurrenceType.WEEKLY, 2, anchor, null);

        // Test: valid recurrence slot
        assertTrue(r.occursOn(LocalDate.of(2025, 9, 24), anchor, anchor));
        assertTrue(r.occursOn(LocalDate.of(2025, 10, 1), anchor, anchor));

        // Test: within recurrence period but not an occurrence
        assertFalse(r.occursOn(LocalDate.of(2025, 9, 30), anchor, anchor));
    }

    @Test
    @DisplayName("Monthly recurring event should shift full interval across months")
    void monthlyRecurringEvent_occursForThreeMonths() {
        LocalDate eventStart = LocalDate.of(2025, 1, 10);
        LocalDate eventEnd = LocalDate.of(2025, 1, 12);
        LocalDate anchor = LocalDate.of(2025, 1, 12);

        Recurrence r = new Recurrence(RecurrenceType.MONTHLY, 3, anchor, null);

        // First month: Jan 10 to 12
        assertTrue(r.occursOn(LocalDate.of(2025, 1, 10), eventStart, eventEnd));
        assertTrue(r.occursOn(LocalDate.of(2025, 1, 11), eventStart, eventEnd));
        assertTrue(r.occursOn(LocalDate.of(2025, 1, 12), eventStart, eventEnd));

        // Second month: Feb 10 to 12
        assertTrue(r.occursOn(LocalDate.of(2025, 2, 11), eventStart, eventEnd));
        assertEquals(LocalDate.of(2025, 2, 10), eventStart.plusMonths(1));
        assertEquals(LocalDate.of(2025, 2, 12), eventEnd.plusMonths(1));

        // Third month: Mar 10 to 12
        assertTrue(r.occursOn(LocalDate.of(2025, 3, 12), eventStart, eventEnd));
        assertEquals(LocalDate.of(2025, 3, 10), eventStart.plusMonths(2));
        assertEquals(LocalDate.of(2025, 3, 12), eventEnd.plusMonths(2));

        // After recurrence window: Apr
        assertFalse(r.occursOn(LocalDate.of(2025, 4, 10), eventStart, eventEnd));
    }

    @Test
    @DisplayName("Monthly recurring deadline should occur across three consecutive months")
    void monthlyRecurringDeadline_matchesByMonth() {
        LocalDate anchor = LocalDate.of(2025, 1, 15);
        Recurrence r = new Recurrence(RecurrenceType.MONTHLY, 3, anchor, null);

        assertTrue(r.occursOn(LocalDate.of(2025, 1, 15), anchor, anchor));
        assertTrue(r.occursOn(LocalDate.of(2025, 2, 15), anchor, anchor));
        assertTrue(r.occursOn(LocalDate.of(2025, 3, 15), anchor, anchor));

        // Test: beyond recurrence end
        assertFalse(r.occursOn(LocalDate.of(2025, 4, 15), anchor, anchor));

        // Test: within recurrence period but not an occurrence
        assertFalse(r.occursOn(LocalDate.of(2025, 2, 10), anchor, anchor));
    }

    @Test
    @DisplayName("Yearly recurring deadline should occur across two consecutive years")
    void yearlyRecurringDeadline_matchesByYear() {
        LocalDate anchor = LocalDate.of(2025, 9, 24);
        Recurrence r = new Recurrence(RecurrenceType.YEARLY, 2, anchor, null);

        assertTrue(r.occursOn(LocalDate.of(2025, 9, 24), anchor, anchor));
        assertTrue(r.occursOn(LocalDate.of(2026, 9, 24), anchor, anchor));

        // Test: beyond recurrence end
        assertFalse(r.occursOn(LocalDate.of(2027, 9, 24), anchor, anchor));

        // Test: within recurrence period but not an occurrence
        assertFalse(r.occursOn(LocalDate.of(2026, 1, 1), anchor, anchor));
    }

    @Test
    @DisplayName("Yearly recurring event should shift full interval")
    void yearlyRecurringEvent_occursForTwoYears() {
        LocalDate eventStart = LocalDate.of(2024, 2, 20);
        LocalDate eventEnd   = LocalDate.of(2024, 2, 29);
        LocalDate anchor = LocalDate.of(2024, 2, 29);

        Recurrence r = new Recurrence(RecurrenceType.YEARLY, 5, anchor, null);

        // Forth year: Feb 20 to 29, 2027
        assertTrue(r.occursOn(LocalDate.of(2027, 2, 25), eventStart, eventEnd));
        assertEquals(LocalDate.of(2027, 2, 20), eventStart.plusYears(3));
        assertEquals(LocalDate.of(2027, 2, 28), eventEnd.plusYears(3));

        // Fifth year: Feb 20 to 29, 2028
        assertTrue(r.occursOn(LocalDate.of(2028, 2, 22), eventStart, eventEnd));
        assertEquals(LocalDate.of(2028, 2, 20), eventStart.plusYears(4));
        assertEquals(LocalDate.of(2028, 2, 29), eventEnd.plusYears(4));

        // Test: within recurrence period but not an occurrence
        assertFalse(r.occursOn(LocalDate.of(2027, 6, 5), eventStart, eventEnd));
    }
}