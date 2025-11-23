package task;

import common.exception.FileContentException;
import common.exception.InvalidTaskFormatException;
import common.exception.MeeBotException;
import org.junit.jupiter.api.Test;
import logic.parser.json.SimpleJsonObject;
import model.task.Task;
import model.task.TodoTask;
import model.task.factory.TodoCreator;

import static org.junit.jupiter.api.Assertions.*;

class TodoTaskCreatorTest {

    private final TodoCreator creator = new TodoCreator();

    @Test
    void testCreateFromArgs_nonRepeatTask() throws MeeBotException {
        String args = "water plants";
        Task task = creator.createFromArgs(args);

        assertNotNull(task);
        assertInstanceOf(TodoTask.class, task);
        assertEquals("water plants", task.getDescription());
        assertFalse(task.isDone());
    }

    @Test
    void testCreateFromArgs_RepeatTask_throwsInvalidTaskFormatException() {
        String args = "water plants /repeat daily 7"; // TODO should not repeat

        assertThrows(InvalidTaskFormatException.class,
                () -> creator.createFromArgs(args)
        );
    }

    @Test
    void testCreateFromJson_validInput() throws MeeBotException {
        SimpleJsonObject obj = new SimpleJsonObject();
        obj.put("type", "todo");
        obj.put("description", "water plants");

        Task task = creator.createFromJson(obj);

        assertNotNull(task);
        assertInstanceOf(TodoTask.class, task);
        assertEquals("water plants", task.getDescription());
    }

    @Test
    void testCreateFromJson_missingDescription_throwsException() {
        SimpleJsonObject obj = new SimpleJsonObject();
        obj.put("type", "todo");
        assertThrows(
                FileContentException.class,
                () -> creator.createFromJson(obj)
        );
    }
}
