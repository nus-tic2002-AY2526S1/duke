package command;

import java.util.function.BiFunction;

import common.CommandDocs;
import manager.TaskManager;

/**
 * Enumeration defining all available commands and factory methods.
 * This enum serves as a central registry for command instantiation. It provides the core
 * command routing mechanism while keeping documentation concerns separated in {@link CommandDocs}.
 * <p>
 * <b>Command Categories:</b>
 * <ul>
 *     <li><strong>Meta Commands:</strong> HELP, BYE</li>
 *     <li><strong>Task Creation:</strong> TODO, DEADLINE, EVENT</li>
 *     <li><strong>Task Querying:</strong> LIST, FILTER, SEARCH, SORT</li>
 *     <li><strong>Task Management:</strong> DELETE, MARK, UNMARK</li>
 */
public enum CommandType {
    HELP("help", (tm, args) -> new HelpCmd()),
    BYE("bye", (tm, args) -> new ExitCmd()),
    TODO("todo", AddTodoCmd::new),
    DEADLINE("deadline", AddDeadlineCmd::new),
    EVENT("event", AddEventCmd::new),
    LIST("list", (tm, args) -> new ListCmd(tm)),
    FILTER("filter", FilterCmd::new),
    SEARCH("search", SearchCmd::new),
    SORT("sort", SortCmd::new),
    DELETE("delete", DeleteTaskCmd::new),
    MARK("mark", (tm, args) -> new UpdateTaskStatusCmd(tm, args, true)),
    UNMARK("unmark", (tm, args) -> new UpdateTaskStatusCmd(tm, args, false));

    private final String keyword;
    private final BiFunction<TaskManager, String, Command> cmdFactory;

    CommandType(String keyword, BiFunction<TaskManager, String, Command> cmdFactory) {
        this.keyword = keyword;
        this.cmdFactory = cmdFactory;
    }

    /**
     * Looks up a CommandType by keyword and perform case-insensitive matching.
     *
     * @param keyword the user input command to look up
     * @return matching {@code CommandType} if found or {@code null} if no match exists
     * @apiNote Returns null rather than throwing exceptions to support graceful
     *         error handling in the command processing pipeline.
     */
    public static CommandType fromKeyword(String keyword) {
        for (CommandType type : values()) {
            if (type.keyword.equalsIgnoreCase(keyword.trim())) {
                return type;
            }
        }
        return null;
    }

    public String getKeyword() {
        return keyword;
    }

    /**
     * Factory method to create appropriate Command instance.
     *
     * @param tm   the task manager for command execution
     * @param args parsed command arguments
     * @return executable Command object
     */
    public Command createCommand(TaskManager tm, String args) {
        return cmdFactory.apply(tm, args);
    }
}
