package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tasks.Deadline;
import tasks.Event;
import tasks.Task;
import tasks.ToDo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTests {
    @TempDir
    File tempDir; // JUnit temporary directory

    File testFolder;
    File testFile;

    @BeforeEach
    void setup() throws IOException {
        testFolder = new File(tempDir, "data");
        testFile = new File(testFolder, "tasklist.txt");

        assertTrue(testFolder.mkdirs());
        assertTrue(testFile.createNewFile());

        FileManager.setFolderAndFile(testFolder, testFile);

        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("[T][X] task1\n");
            writer.write("[D][ ] task2 | by: 13 Oct 2025 0000\n");
            writer.write("[E][ ] task3 | from: 13 Oct 2025 0000, to: 14 Oct 2025 0000\n");
        }
    }

    @Nested
    @DisplayName("TaskManager()")
    class taskManager_Test {
        @Test
        @DisplayName("Success")
        public void taskManagerTest_Success() {
            assertDoesNotThrow(TaskManager::new);

            assertEquals(3, TaskManager.getTasklist().size());

            ArrayList<Task> tasks = TaskManager.getTasklist();

            assertInstanceOf(ToDo.class, tasks.get(0));
            assertEquals("task1", tasks.get(0).getDescription());
            assertTrue(tasks.get(0).getIsDone());

            assertInstanceOf(Deadline.class, tasks.get(1));
            assertEquals("task2", tasks.get(1).getDescription());
            assertFalse(tasks.get(1).getIsDone());
            assertEquals(LocalDateTime.parse("2025-10-13T00:00:00"), ((Deadline) tasks.get(1)).getBy());

            assertInstanceOf(Event.class, tasks.get(2));
            assertEquals("task3", tasks.get(2).getDescription());
            assertFalse(tasks.get(2).getIsDone());
            assertEquals(LocalDateTime.parse("2025-10-13T00:00:00"), ((Event) tasks.get(2)).getStartDateTime());
            assertEquals(LocalDateTime.parse("2025-10-14T00:00:00"), ((Event) tasks.get(2)).getEndDateTime());
        }
    }

    @Nested
    @DisplayName("manageTask()")
    class ManageTask_Test {
        @Test
        @DisplayName("Success")
        public void manageTask_Success() {
            String userInput = "todo task4";

            TaskManager taskManager = assertDoesNotThrow(TaskManager::new);
            assertEquals(3, TaskManager.getTasklist().size());

            assertDoesNotThrow(() -> taskManager.manageTask(userInput));
            assertEquals(4, TaskManager.getTasklist().size());

            assertInstanceOf(ToDo.class, TaskManager.getTasklist().get(3));
            assertEquals("task4", TaskManager.getTasklist().get(3).getDescription());

            List<String> lines = assertDoesNotThrow(() -> Files.readAllLines(testFile.toPath()));
            assertEquals(4, lines.size());
            assertEquals("[T][ ] task4", lines.get(3));
        }

        @Test
        @DisplayName("Update Success")
        public void manageTask_Update_Success() {
            String userInput = "update 3, taskName: task10, start: 31/12/25 1200, end: 1/1/26 2359";

            TaskManager taskManager = assertDoesNotThrow(TaskManager::new);

            assertDoesNotThrow(() -> taskManager.manageTask(userInput));

            assertInstanceOf(Event.class, TaskManager.getTasklist().get(2));
            assertEquals("task10", TaskManager.getTasklist().get(2).getDescription());
            assertEquals(LocalDateTime.parse("2025-12-31T12:00:00"), ((Event) TaskManager.getTasklist().get(2)).getStartDateTime());
            assertEquals(LocalDateTime.parse("2026-01-01T23:59:00"),  ((Event) TaskManager.getTasklist().get(2)).getEndDateTime());
        }

        @Test
        @DisplayName("clone success")
        public void manageTask_Clone_Success() {
            String userInput = "clone 1";

            TaskManager taskManager = assertDoesNotThrow(TaskManager::new);

            assertDoesNotThrow(() -> taskManager.manageTask(userInput));

            assertEquals(4, TaskManager.getTasklist().size());
            assertInstanceOf(ToDo.class, TaskManager.getTasklist().get(3));
            assertEquals("task1", TaskManager.getTasklist().get(3).getDescription());
            assertTrue(TaskManager.getTasklist().get(3).getIsDone());
        }
        @Test
        void manageTask_invalidInput_doesNotModifyTasklistOrFile() throws IOException {
            TaskManager taskManager = assertDoesNotThrow(TaskManager::new);
            List<String> before = Files.readAllLines(testFile.toPath());

            taskManager.manageTask("invalidCommand xyz");

            assertEquals(3, TaskManager.getTasklist().size());

            List<String> after = Files.readAllLines(testFile.toPath());
            assertEquals(before, after);
        }
    }


}
