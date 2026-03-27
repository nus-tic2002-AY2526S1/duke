package kev.storage;

import kev.task.*;
import kev.exception.KevException;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * to test the Storage class for saving and loading tasks.
 */
public class StorageTest {

    private static final String TEST_PATH = "data/test-storage.txt";

    /**
     * cleans up the test file before each test to ensure a fresh start.
     */
    @BeforeEach
    public void cleanFile() {
        File file = new File(TEST_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * tests that saving tasks and then loading them returns an equivalent list.
     * @throws Exception If there is an error during save/load.
     */
    @Test
    public void saveTasks_and_loadTasks_returnsEquivalentData() throws Exception {
        Storage storage = new Storage(TEST_PATH);

        // Original tasks
        TaskList original = new TaskList();
        original.addTask(new Todo("Read book"));
        original.addTask(new Deadline("Submit report", "2025-12-01"));
        original.addTask(new Event(
                "Meeting",
                "2025-11-03", "10:00",
                "2025-11-03", "11:00"
        ));

        // Save
        storage.saveTasks(original.getTasks());

        // Load
        List<Task> loaded = storage.loadTasks();

        assertEquals(3, loaded.size());

        // Compare each task
        assertEquals(original.getTask(0).toString(), loaded.get(0).toString());
        assertEquals(original.getTask(1).toString(), loaded.get(1).toString());
        assertEquals(original.getTask(2).toString(), loaded.get(2).toString());
    }

    /**
     * tests that loading from a non-existent file returns an empty list.
     * @throws Exception If there is an error reading the file.
     */
    @Test
    public void loadTasks_fileDoesNotExist_returnsEmptyList() throws Exception {
        Storage storage = new Storage("data/nonexistent-file.txt");

        List<Task> tasks = storage.loadTasks();

        assertTrue(tasks.isEmpty());
    }
}

