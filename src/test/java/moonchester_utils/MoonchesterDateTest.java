package moonchester_utils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

public class MoonchesterDateTest {
    /**
     * Tests if ConvertToDateTime does not return null for a valid date string
     * Also tests the the respective values of the date to ensure that the date is properly converted
     */
    @Test
    public void testConvertToDateTimeValid() {
        String dateString = "30/10/2025 2359";
        LocalDateTime result = MoonchesterDate.convertToDateTime(dateString, 2);
        assertNotNull(result, "Result should not be null for a valid date string");
        assertEquals(2025, result.getYear());
        assertEquals(10, result.getMonthValue());
        assertEquals(30, result.getDayOfMonth());
        assertEquals(23, result.getHour());
        assertEquals(59, result.getMinute());
    }
}
