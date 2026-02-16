package checker;

import exceptions.ViewException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ViewCheckerTests {
    @Nested
    @DisplayName("checkViewDate()")
    class CheckViewDate_Test {
        String date;

        @Test
        @DisplayName("Success")
        public void checkViewDate_Success() {
            date = "13/10/25";

            assertDoesNotThrow(() -> ViewChecker.checkViewDate(date));
        }

        @Test
        @DisplayName("Throws MissingViewDateException")
        public void checkViewDate_MissingDate_MissingViewDateException() {
            date = "";

            ViewException viewException = assertThrows(ViewException.MissingViewDateException.class,
                    () -> ViewChecker.checkViewDate(date));

            assertEquals("Missing date. Usage: view <dd/MM/yyyy>", viewException.getMessage());
        }
    }
}
