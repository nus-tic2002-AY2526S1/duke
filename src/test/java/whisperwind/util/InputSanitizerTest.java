package whisperwind.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputSanitizerTest {

    @Test
    void testSanitizeInput_normalInput_returnsSame() {
        assertEquals("Hello World", InputSanitizer.sanitizeInput("Hello World"));
    }

    @Test
    void testSanitizeInput_withSpaces_trimmed() {
        assertEquals("Hello World", InputSanitizer.sanitizeInput("  Hello World  "));
    }

    @Test
    void testSanitizeInput_nullInput_returnsEmpty() {
        assertEquals("", InputSanitizer.sanitizeInput(null));
    }

    @Test
    void testSanitizeDescription_validInput_returnsSanitized() {
        assertEquals("Read book", InputSanitizer.sanitizeDescription("  Read book  "));
    }

    @Test
    void testIsValidDateTimeFormat_validFormats_returnsTrue() {
        assertTrue(InputSanitizer.isValidDateTimeFormat("25/12/2024 1800"));
        assertTrue(InputSanitizer.isValidDateTimeFormat("2024-12-25 1800"));
    }

    @Test
    void testIsValidDateTimeFormat_invalidFormats_returnsFalse() {
        assertFalse(InputSanitizer.isValidDateTimeFormat("25-12-2024 1800"));
        assertFalse(InputSanitizer.isValidDateTimeFormat("invalid"));
    }

    @Test
    void testIsValidTaskNumber_validNumbers_returnsTrue() {
        assertTrue(InputSanitizer.isValidTaskNumber("1", 10));
        assertTrue(InputSanitizer.isValidTaskNumber("5", 10));
    }

    @Test
    void testIsValidTaskNumber_invalidNumbers_returnsFalse() {
        assertFalse(InputSanitizer.isValidTaskNumber("0", 10));
        assertFalse(InputSanitizer.isValidTaskNumber("11", 10));
        assertFalse(InputSanitizer.isValidTaskNumber("abc", 10));
    }
}