package whisperwind.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import whisperwind.exceptions.CommandException;
import whisperwind.exceptions.TaskException;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class DeleteManagerTest {
    private TaskList taskList;
    private DeleteManager deleteManager;

    @BeforeEach
    void setUp() throws TaskException, CommandException {
        taskList = new TaskList();
        // Add some test tasks
        taskList.addTodo("Task 1");
        taskList.addTodo("Task 2");
        taskList.addTodo("Task 3");

        Scanner scanner = new Scanner(System.in);
        deleteManager = new DeleteManager(taskList, scanner);
    }

    // Note: We'll test the parseBulkDeleteNumbers method since it's private
    // We'll create a testable version or test through public methods

    @Test
    void testDeleteManagerCreation() {
        assertNotNull(deleteManager);
    }

    // We can test the public showDeleteHelp method
    @Test
    void testShowDeleteHelp_doesNotThrow() {
        assertDoesNotThrow(() -> deleteManager.showDeleteHelp());
    }
}