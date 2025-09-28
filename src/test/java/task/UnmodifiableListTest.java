package task;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UnmodifiableListTest {

    @Test
    void unmodifiableListStillAllowsMutatingTasks() {
        // given: a mutable task and an unmodifiable list wrapper
        Task task = new TodoTask("Read book", Recurrence.none(null));
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        List<Task> readOnlyList = Collections.unmodifiableList(tasks);

        // when: we modify the task directly through the "read-only" list
        Task fromList = readOnlyList.get(0);
        fromList.markAsDone();

        // then: the change is reflected
        assertTrue(task.isDone());
        assertTrue(fromList.isDone());

        // and: list structure cannot be modified
        assertThrows(UnsupportedOperationException.class, () -> readOnlyList.add(task));
    }
}
