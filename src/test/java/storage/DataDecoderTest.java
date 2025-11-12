package storage;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import task.Deadline;
import task.Event;
import task.Task;
import task.TaskList;
import task.Todo;

public class DataDecoderTest {

    @Test
    public void testDecodeTodo() {
        List<String> data = List.of("T | 0 | NIL | read book");

        TaskList tl = DataDecoder.decodeData(data);

        Task t = tl.get(0);
        assertTrue(t instanceof Todo);
        assertEquals("read book", t.getDescription());
        assertFalse(t.getStatus());
        assertEquals(1, t.getIndex());
    }

    @Test
    public void testDecodeDeadline() {
        List<String> data = List.of("D | 1 | MEDIUM | submit report | 2025-01-31T23:59");

        TaskList tl = DataDecoder.decodeData(data);

        Deadline d = (Deadline) tl.get(0);
        assertEquals("submit report", d.getDescription());
        assertTrue(d.getStatus());
        assertEquals(LocalDateTime.parse("2025-01-31T23:59"), d.getDeadline());
        assertEquals(2, d.getIndex());
    }

    @Test
    public void testDecodeEvent() {
        List<String> data = List.of(
                "E | 0 | LOW | meeting | 2025-03-10T10:00 | 2025-03-10T12:00"
        );

        TaskList tl = DataDecoder.decodeData(data);

        Event e = (Event) tl.get(0);
        assertEquals("meeting", e.getDescription());
        assertFalse(e.getStatus());
        assertEquals(LocalDateTime.parse("2025-03-10T10:00"), e.getFromDate());
        assertEquals(LocalDateTime.parse("2025-03-10T12:00"), e.getToDate());
        assertEquals(3, e.getIndex());
    }
}
