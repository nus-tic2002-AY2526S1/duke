package testutils;

import storage.Storage;
import task.TaskList;

/**
 * A dummy Storage class that does not perform any actual saving.
 * Used for testing purposes.
 */
public class DummyStorage extends Storage {

    public DummyStorage() {
        super(""); // no actual storage path needed
    }

    @Override
    public void save(TaskList tasks) {
    }
}
