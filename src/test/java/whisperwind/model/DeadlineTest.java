package whisperwind.model;

import org.junit.jupiter.api.Test;
import whisperwind.exceptions.TaskException;

import static org.junit.jupiter.api.Assertions.*;

class DeadlineTest {

    @Test
    void testCreateDeadline_validInput_success() throws TaskException {
        // Arrange - Use future date
        String description = "Submit report";
        String by = "25/12/2025 1800"; // Future date

        // Act
        Deadline deadline = new Deadline(description, by);

        // Assert
        assertNotNull(deadline);
        assertEquals(description, deadline.getDescription());
        assertNotNull(deadline.getBy());
    }

    @Test
    void testCreateDeadline_emptyDate_throwsException() {
        // Arrange
        String description = "Submit report";
        String emptyDate = "";

        // Act & Assert
        assertThrows(TaskException.class, () -> new Deadline(description, emptyDate));
    }

    @Test
    void testCreateDeadline_pastDate_throwsException() {
        // Arrange - Use clearly past date
        String description = "Submit report";
        String pastDate = "01/01/2020 1200"; // Past date

        // Act & Assert
        assertThrows(TaskException.class, () -> new Deadline(description, pastDate));
    }

    @Test
    void testSetBy_validFutureDate_success() throws TaskException {
        // Arrange
        Deadline deadline = new Deadline("Test", "25/12/2025 1800");
        String newDate = "26/12/2025 0900"; // Future date

        // Act
        deadline.setBy(newDate);

        // Assert
        assertNotNull(deadline.getBy());
    }

    @Test
    void testSetBy_pastDate_throwsException() throws TaskException {
        // Arrange
        Deadline deadline = new Deadline("Test", "25/12/2025 1800");
        String pastDate = "01/01/2020 1200"; // Past date

        // Act & Assert
        assertThrows(TaskException.class, () -> deadline.setBy(pastDate));
    }

    @Test
    void testGetByFormatted_returnsReadableFormat() throws TaskException {
        // Arrange
        Deadline deadline = new Deadline("Test", "25/12/2025 1800");

        // Act
        String formatted = deadline.getByFormatted();

        // Assert
        assertNotNull(formatted);
        assertFalse(formatted.isEmpty());
        assertNotEquals("unspecified", formatted);
    }

    @Test
    void testMeetsDeadlineRequirements_validDeadline_returnsTrue() throws TaskException {
        // Arrange
        Deadline deadline = new Deadline("Valid task", "25/12/2025 1800");

        // Act & Assert
        assertTrue(deadline.meetsDeadlineRequirements());
    }
}