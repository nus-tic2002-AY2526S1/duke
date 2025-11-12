package whisperwind.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import whisperwind.exceptions.CommandException;
import whisperwind.exceptions.TaskException;
import whisperwind.model.TaskType;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ArchiveManager functionality.
 */
class ArchiveManagerTest {
    private TaskList taskList;
    private ArchiveManager archiveManager;

    @BeforeEach
    void setUp() throws TaskException, CommandException {
        taskList = new TaskList();
        archiveManager = new ArchiveManager();

        // Add test tasks
        taskList.addTodo("Read Java book");
        taskList.addTodo("Buy groceries");
        taskList.addDeadline("Submit report /by 25/12/2025 1800");
        taskList.addEvent("Team meeting /from 25/12/2025 1400 /to 25/12/2025 1500");

        // Mark one task as completed
        taskList.markTask(1);
    }

    @Test
    void testArchiveManagerCreation() {
        assertNotNull(archiveManager, "ArchiveManager should be created successfully");
    }

    @Test
    void testArchiveAllTasks_withTasks_success() {
        // Tests that archiveAllTasks doesn't throw exceptions with valid tasks
        assertDoesNotThrow(() -> {
            String archivePath = archiveManager.archiveAllTasks(taskList);
            assertNotNull(archivePath, "Archive path should not be null");
            assertTrue(archivePath.contains("archive_"), "Archive path should contain 'archive_' prefix");
        });
    }

    @Test
    void testArchiveAllTasks_emptyTaskList_throwsException() {
        TaskList emptyTaskList = new TaskList();

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> archiveManager.archiveAllTasks(emptyTaskList),
                "Should throw IllegalStateException when archiving an empty list");

        assertEquals("No tasks to archive - task list is empty", exception.getMessage());
    }

    @Test
    void testArchiveCompletedTasks_withCompletedTasks_success() {
        // One task is marked as done
        assertDoesNotThrow(() -> {
            String archivePath = archiveManager.archiveCompletedTasks(taskList);
            assertNotNull(archivePath, "Archive path should not be null");
            assertTrue(archivePath.contains("completed_"), "Archive path should contain 'completed_' prefix");
        });
    }

    @Test
    void testArchiveCompletedTasks_noCompletedTasks_throwsException() throws TaskException, CommandException {
        TaskList freshTaskList = new TaskList();
        freshTaskList.addTodo("Task 1");
        freshTaskList.addTodo("Task 2");

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> archiveManager.archiveCompletedTasks(freshTaskList),
                "Should throw IllegalStateException when no completed tasks exist");

        assertEquals("No completed tasks to archive", exception.getMessage());
    }

    @Test
    void testArchiveTasksByType_withTodoTasks_success() {
        assertDoesNotThrow(() -> {
            String archivePath = archiveManager.archiveTasksByType(taskList, TaskType.TODO);
            assertNotNull(archivePath, "Archive path should not be null");
            assertTrue(archivePath.contains("todo_"), "Archive path should contain 'todo_' prefix");
        });
    }

    @Test
    void testArchiveTasksByType_withDeadlineTasks_success() {
        assertDoesNotThrow(() -> {
            String archivePath = archiveManager.archiveTasksByType(taskList, TaskType.DEADLINE);
            assertNotNull(archivePath, "Archive path should not be null");
            assertTrue(archivePath.contains("deadline_"), "Archive path should contain 'deadline_' prefix");
        });
    }

    @Test
    void testArchiveTasksByType_noTasksOfType_throwsException() {
        TaskList emptyTaskList = new TaskList();

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> archiveManager.archiveTasksByType(emptyTaskList, TaskType.TODO),
                "Should throw IllegalStateException when no tasks of specified type exist");

        assertEquals("No todo tasks to archive", exception.getMessage());
    }

    @Test
    void testListArchiveFiles_doesNotThrow() {
        // Should not throw even if no archive files exist
        assertDoesNotThrow(() -> archiveManager.listArchiveFiles());
    }

    @Test
    void testViewArchive_invalidIndex_doesNotThrow() {
        // Viewing invalid archive index should not throw exceptions
        assertDoesNotThrow(() -> archiveManager.viewArchive(999));
    }
}
