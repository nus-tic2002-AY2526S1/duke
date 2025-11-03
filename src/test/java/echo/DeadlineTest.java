package echo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeadlineTest {

    @Test
    void testToString_formatsDateCorrectly() {
        Deadline d = new Deadline("return book", "2019-12-02 1800");
        String expected = "[D][ ] return book (by: Dec 02 2019, 6:00 PM)";
        assertEquals(expected, d.toString());
    }

    @Test
    void testToSaveFormat_includesCorrectStatusAndDate() {
        Deadline d = new Deadline("return book", "2019-12-02 1800");
        d.markAsDone();
        String expected = "D | 1 | return book | 2019-12-02 1800";
        assertEquals(expected, d.toSaveFormat());
    }
}
