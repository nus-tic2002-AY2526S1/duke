package model.task.factory;

import common.exception.MeeBotException;
import logic.parser.json.SimpleJsonObject;
import model.task.Task;

/**
 * Factory interface for creating task instances from different input sources.
 * <p>
 * Implementations of this interface provide parsing and validation logic to
 * construct task objects from either:
 * <ul>
 *     <li>User command arguments (interactive input)</li>
 *     <li>JSON objects (data persistence/loading)</li>
 * </ul>
 */
public interface TaskCreator {

    /**
     * Creates a task instance from user command arguments.
     * <p>
     * Implementations should parse the argument string according to their
     * specific task type's format requirements and return a fully constructed
     * task object.
     *
     * @param args the command arguments string to parse
     * @return a new {@link Task} instance created from the arguments
     * @throws MeeBotException if the arguments are invalid or cannot be parsed
     */
    Task createFromArgs(String args) throws MeeBotException;

    /**
     * Creates a task instance from a JSON object representation.
     * <p>
     * Implementations should extract required fields from the JSON object
     * and construct a task instance.
     *
     * @param obj the JSON object containing task data
     * @return a new {@link Task} instance created from the JSON data
     * @throws MeeBotException if required fields are missing or invalid
     */
    Task createFromJson(SimpleJsonObject obj) throws MeeBotException;
}
