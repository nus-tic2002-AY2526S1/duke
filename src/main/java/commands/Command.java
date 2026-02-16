package commands;

import enums.CommandType;
import exceptions.*;
import tasks.Task;

import java.util.AbstractMap;
import java.util.ArrayList;

/**
 * Main class for all other command types.
 */
public abstract class Command {
    /**
     * tasklist is used for all commands
     */
    protected ArrayList<Task> tasklist;

    /**
     * Initialise Command
     *
     * @param tasklist tasklist in task manager
     */
    public Command(ArrayList<Task> tasklist) {
        assert tasklist != null;

        this.tasklist = tasklist;
    }

    /**
     * Create the various command objects based on commandType
     *
     * @param commandLine parsed user's input
     * @param tasklist    tasklist from task manager
     * @return various commands
     * @throws GrootException if there are any error in creating command
     */
    public static Command createCommand(AbstractMap.SimpleEntry<CommandType,
            ArrayList<String>> commandLine, ArrayList<Task> tasklist) throws GrootException {
        assert commandLine != null;
        assert commandLine.getKey() != null;
        assert tasklist != null;

        try {
            return switch (commandLine.getKey()) {
                case MARK, UNMARK -> new MarkTaskCommand(commandLine, tasklist);
                case LIST, VIEW -> new ShowListCommand(commandLine, tasklist);
                case DELETE -> new DeleteCommand(commandLine, tasklist);
                case TODO, DEADLINE, EVENT -> new AddCommand(commandLine, tasklist);
                case NONE -> null;
                case BYE -> new ExitCommand(tasklist);
                case UPDATE -> new UpdateCommand(commandLine, tasklist);
                case FIND -> new FindCommand(commandLine, tasklist);
                case CLONE -> new CloneCommand(commandLine, tasklist);
            };
        } catch (DateTimeException e) {
            switch (commandLine.getKey()) {
                case DEADLINE -> throw new DeadlineException.InvalidDeadlineDateTimeException();
                case EVENT -> throw new EventException.InvalidEventDateTimeException();
                case VIEW -> throw new ViewException.InvalidViewDateException();
            }
            return null;
        }
    }

    /**
     * Function used by all commands to execute itself
     */
    public abstract void execute();
}
