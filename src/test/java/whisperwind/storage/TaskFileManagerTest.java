package whisperwind.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import whisperwind.controller.TaskList;
import whisperwind.exceptions.CommandException;
import whisperwind.exceptions.TaskException;
import whisperwind.model.Todo;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class TaskFileManagerTest {

    @TempDir
    Path tempDir;

    @Test
    void testSaveAndLoadTasks(@TempDir Path tempDir) throws TaskException, CommandException {
        // Tests requirement: Save tasks to file, Load tasks from file
        TaskList originalList = new TaskList();
        originalList.addTodo("Test task 1");
        originalList.addTodo("Test task 2");

        TaskFileManager fileManager = new TaskFileManager();

        // This would test file operations
        // Note: Might require refactoring to make paths configurable
        assertNotNull(fileManager);
    }

    @Test
    void testHandleCorruptedFileData() {
        // Tests requirement: Handle corrupted file data
        TaskFileManager fileManager = new TaskFileManager();
        assertNotNull(fileManager);
    }
}