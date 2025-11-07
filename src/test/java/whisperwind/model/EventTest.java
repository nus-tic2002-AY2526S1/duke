package whisperwind.model;
import org.junit.jupiter.api.Test;
import whisperwind.exceptions.TaskException;
import static org.junit.jupiter.api.Assertions.*;

class EventTest {
    @Test
    void testCreateEvent_validTimeRange_success() throws TaskException {
        // Arrange
        String description = "Team meeting";
        String from = "25/12/2025 1400";
        String to = "25/12/2025 1500";

        // Act
        Event event = new Event(description, from, to);

        // Assert
        assertNotNull(event);
        assertEquals(description, event.getDescription());
        assertNotNull(event.getFrom());
        assertNotNull(event.getTo());
    }

    @Test
    void testCreateEvent_endBeforeStart_throwsException() {
        // Arrange
        String description = "Invalid event";
        String from = "25/12/2025 1500";
        String to = "25/12/2025 1400"; // End before start

        // Act & Assert
        assertThrows(TaskException.class, () -> new Event(description, from, to));
    }

    @Test
    void testCreateEvent_pastDate_throwsException() {
        // Arrange
        String description = "Past event";
        String from = "01/01/2020 1400";
        String to = "01/01/2020 1500";

        // Act & Assert
        assertThrows(TaskException.class, () -> new Event(description, from, to));
    }
}