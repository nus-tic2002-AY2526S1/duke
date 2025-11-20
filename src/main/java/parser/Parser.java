package parser;

import command.Command;
import command.AddDeadlineCommand;
import command.AddEventCommand;
import command.AddTodoCommand;
import command.DeleteCommand;
import command.ExitCommand;
import command.ListCommand;
import command.MarkCommand;
import command.UnmarkCommand;
import command.FindCommand;
import util.DukeException;
import util.DateTime;


public class Parser {
    public static Command parse(String input) throws DukeException {
        if (input == null || input.trim().isEmpty()) {
            throw new DukeException("Empty command.");
        }
        String s = input.trim();
        assert s != null : "Trimmed input should not be null";
        assert !s.isEmpty() : "Trimmed input should not be empty";
        s = s.toLowerCase();

        if (s.equals("bye"))
            return new ExitCommand();
        if (s.equals("list"))
            return new ListCommand();

        if (s.startsWith("todo ")) {
            String desc = s.substring(5).trim();
            assert desc != null : "todo description should never be null";
            if (desc.isEmpty())
                throw new DukeException("todo what?");
            return new AddTodoCommand(desc);
        }

    if (s.startsWith("deadline ")) {
        String body = s.substring(9);
        String[] parts = body.split("/by", 2);

        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
        throw new DukeException("deadline by? Usage: deadline <description> /by <yyyy-MM-dd HHmm>");
        }

        String desc = parts[0].trim();
        String byStr = parts[1].trim();

        try {
            DateTime.parseDateTime(byStr);
        } catch (IllegalArgumentException e) {
            throw new DukeException(e.getMessage());
        }

        return new AddDeadlineCommand(desc, byStr);
    }


  if (s.startsWith("event ")) {
    String body = s.substring(6);
    String[] p1 = body.split("/from", 2);

    if (p1.length < 2 || p1[0].trim().isEmpty()) {
        throw new DukeException("what event?");
    }

    String desc = p1[0].trim();
    String[] p2 = p1[1].split("/to", 2);

    if (p2.length < 2 || p2[0].trim().isEmpty() || p2[1].trim().isEmpty()) {
        throw new DukeException("Usage: event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>");
    }

    String fromStr = p2[0].trim();
    String toStr = p2[1].trim();

    try {
        DateTime.parseDateTime(fromStr);
        DateTime.parseDateTime(toStr);
    } catch (IllegalArgumentException e) {
        throw new DukeException(e.getMessage());
    }

    return new AddEventCommand(desc, fromStr, toStr);
}


        if (s.startsWith("mark ")) {
            String idxs = s.substring(5).trim();
            return new MarkCommand(idxs);
        }

        if (s.startsWith("unmark ")) {
            String idxs = s.substring(7).trim();
            return new UnmarkCommand(idxs);
        }

        if (s.startsWith("delete ")) {
            int idx = parseIndex(s.substring(7).trim());
            return new DeleteCommand(idx);
        }

        if (s.startsWith("find ")) {
            String desc = s.substring(5).trim();
            return new FindCommand(desc);
        }

        throw new DukeException("Typo?");
    }

    private static int parseIndex(String s) throws DukeException {
        try {
            int i = Integer.parseInt(s);
            if (i < 1)
                throw new DukeException("Index must be >= 1");
            return i - 1;
        } catch (NumberFormatException e) {
            throw new DukeException("Please provide a valid task number.");
        }
    }
}
