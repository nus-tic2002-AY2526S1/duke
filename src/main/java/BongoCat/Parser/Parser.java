package bongocat.parser;

import bongocat.commands.*;
import bongocat.BongoException;

import java.time.LocalDate;

/**
 * Scans, Trims, Calls commands and throws Exceptions
 */
public class Parser {


    public static Command parse(String input) throws BongoException {
        String trimmed = input.trim();
        assert input != null : "Input to parser cannot be null";

        if (trimmed.equals("bye")) return new ExitCommand();
        if (trimmed.equals("list")) return new ListCommand();
        if (trimmed.equals("space left")) return new SpaceLeftCommand();

        if (trimmed.startsWith("mark ")) return new MarkCommand(parseNum(trimmed, 5));
        if (trimmed.startsWith("unmark ")) return new UnmarkCommand(parseNum(trimmed, 7));
        if (trimmed.startsWith("delete ")) return new DeleteCommand(parseNum(trimmed, 7));

        if (trimmed.startsWith("todo")) {
            String name = trimmed.substring(4).trim();
            if (name.isEmpty()) throw new BongoException("Description cannot be empty lah PLEASE.");
            return new AddTodoCommand(name);
        }

        if (trimmed.startsWith("deadline")) {
            String[] parts = trimmed.split(" /by ");
            if (parts.length < 2)
                throw new BongoException("Deadline must have /by <yyyy-mm-dd>");

            String name = parts[0].substring(8).trim();
            String by = parts[1].trim();

            try {
                LocalDate.parse(by);
            } catch (Exception e) {
                throw new BongoException("Write your date properly! Use yyyy-mm-dd e.g. 2025-11-16");
            }

            return new AddDeadlineCommand(name, by);
        }

        if (trimmed.startsWith("event")) {
            String[] parts = trimmed.split(" /from | /to ");
            if (parts.length < 3)
                throw new BongoException("Event must have /from and /to");
            return new AddEventCommand(parts[0].substring(5).trim(),
                    parts[1].trim(),
                    parts[2].trim());
        }

        if (trimmed.startsWith("on ")) {
            String dateString = trimmed.substring(3).trim();
            try {
                LocalDate date = LocalDate.parse(dateString);
                return new DateQueryCommand(date);
            } catch (Exception e) {
                throw new BongoException("Date must be in yyyy-mm-dd format!");
            }
        }

        // 🔥 LEVEL 9: FIND COMMAND
        if (trimmed.startsWith("find ")) {
            String keyword = trimmed.substring(5).trim();
            return new FindCommand(keyword);
        }
        assert false : "Parser failed to match a known command but did not throw earlier";

        throw new BongoException("HUH? Unknown command.");
    }

    private static int parseNum(String input, int offset) throws BongoException {
        try {
            return Integer.parseInt(input.substring(offset).trim());
        } catch (Exception e) {
            throw new BongoException("Invalid number.");
        }
    }
}
