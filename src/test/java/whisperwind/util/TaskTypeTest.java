package whisperwind.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTypeTest {

    @Test
    void testFromString_todo_returnsTodo() {
        assertEquals(TaskType.TODO, TaskType.fromString("todo"));
        assertEquals(TaskType.TODO, TaskType.fromString("TODO"));
    }

    @Test
    void testFromString_deadline_returnsDeadline() {
        assertEquals(TaskType.DEADLINE, TaskType.fromString("deadline"));
    }

    @Test
    void testFromString_event_returnsEvent() {
        assertEquals(TaskType.EVENT, TaskType.fromString("event"));
    }

    @Test
    void testFromString_unknown_returnsUnknown() {
        assertEquals(TaskType.UNKNOWN, TaskType.fromString("invalid"));
        assertEquals(TaskType.UNKNOWN, TaskType.fromString(""));
    }

    @Test
    void testFromCode_validCodes_returnsCorrectType() {
        assertEquals(TaskType.TODO, TaskType.fromCode("T"));
        assertEquals(TaskType.DEADLINE, TaskType.fromCode("D"));
        assertEquals(TaskType.EVENT, TaskType.fromCode("E"));
    }

    @Test
    void testFromCode_invalidCode_returnsUnknown() {
        assertEquals(TaskType.UNKNOWN, TaskType.fromCode("X"));
    }
}