package echo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void testParseTask_parsesDeadlineCorrectly() {
        String line = "D | 0 | return book | 2019-12-02 1800";
        Task task = Parser.parseTask(line);
        assertTrue(task instanceof Deadline);
        assertEquals("[D][ ] return book (by: Dec 02 2019, 6:00 PM)", task.toString());
    }

    @Test
    void testParseTask_parsesEventCorrectly() {
        String line = "E | 1 | wedding | 2019-10-15 0800 | 2019-10-15 1700";
        Task task = Parser.parseTask(line);
        assertTrue(task instanceof Event);
        assertEquals("[E][X] wedding (from: Oct 15 2019, 8:00 AM to: Oct 15 2019, 5:00 PM)", task.toString());
    }
}
