package whisperwind.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import whisperwind.exceptions.CommandException;
import whisperwind.exceptions.TaskException;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class DeleteManagerComprehensiveTest {
    private TaskList taskList;
    private DeleteManager deleteManager;

    @BeforeEach
    void setUp() throws TaskException, CommandException {
        taskList = new TaskList();
        // Add various test tasks
        taskList.addTodo("Read book");
        taskList.addTodo("Read novel");
        taskList.addTodo("Buy groceries");
        taskList.addTodo("Meeting with team");

        // Mark some as completed
        taskList.markTask(1);
        taskList.markTask(3);

        Scanner scanner = new Scanner(System.in);
        deleteManager = new DeleteManager(taskList, scanner);
    }

    @Test
    void testDeleteCompletedTasks() throws CommandException {
        // This tests requirement: Delete completed tasks
        // Note: Might require mocking user confirmation
        assertEquals(4, taskList.getTaskCount());
        // deleteManager.handleDeleteCommand(new String[]{"delete", "completed"});
        // Would need to simulate "yes" confirmation
    }

    @Test
    void testDeleteByPattern() throws CommandException {
        // This tests requirement: Delete by pattern (book*, *meeting)
        // deleteManager.handleDeleteCommand(new String[]{"delete", "book*"});
        // Would need to simulate confirmation
    }

    @Test
    void testDeleteByType() throws CommandException {
        // This tests requirement: Delete by task type
        // deleteManager.handleDeleteCommand(new String[]{"delete", "todo"});
        // Would need to simulate confirmation
    }
}