package com.golden.parser;

import com.golden.commands.*;
import com.golden.exceptions.BotException;
import com.golden.exceptions.ParseException;
import com.golden.exceptions.parseErrors.MissingArgumentException;
import com.golden.exceptions.parseErrors.UnknownCommandException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandParserTest {

    @Test
    public void parseCommand_success() throws BotException {
        // test 'hello'
        assertInstanceOf(EchoCommand.class, CommandParser.parseCommand("Hello"));
        // test 'bye' - CAPS and mixed-case
        assertInstanceOf(ExitCommand.class, CommandParser.parseCommand("bye"));
        assertInstanceOf(ExitCommand.class, CommandParser.parseCommand("BYE"));
        // test 'list' with extra whitespaces and mixed-case
        assertInstanceOf(ListCommand.class, CommandParser.parseCommand("LiST    "));
        // test 'mark' with 1 arg
        assertInstanceOf(MarkCommand.class, CommandParser.parseCommand("mark 1   "));
        // test 'unmark' with 1 arg
        assertInstanceOf(UnmarkCommand.class, CommandParser.parseCommand("UNmark    0"));
        // test 'delete' with 1 arg
        assertInstanceOf(DeleteCommand.class, CommandParser.parseCommand("delete    10"));
        // test 'todo' with valid args
        assertInstanceOf(TodoCommand.class, CommandParser.parseCommand("TODO task Description /priority HIGH"));
        // test 'event' with valid args
        assertInstanceOf(EventCommand.class, CommandParser.parseCommand(
                "event task Description /from 2025-12-11 /to 2025-12-12  /priority medium"));
        // test 'deadline' with valid args
        assertInstanceOf(DeadlineCommand.class, CommandParser.parseCommand(
                "DeAdLine task Description /by 2025-12-11  /priority lOw"));
        // test unknown command
        UnknownCommandException ex1 = assertThrows(UnknownCommandException.class, () ->
                CommandParser.parseCommand("test")
        );
        String expected1 = String.format("unknown command '%s'.", "test");
        assertEquals(expected1, ex1.getMessage());
    }
    @Test
    public void parseCommand_exceptionThrown() throws BotException {
        // test 'list' with extra arguments
        ParseException ex1 = assertThrows(ParseException.class, () ->
                CommandParser.parseCommand("LIst 1")
        );
        String expected1 = "too many arguments. Try 'list'.";
        assertEquals(expected1, ex1.getMessage());

        // test 'mark' with 0 args (whitespace)
        MissingArgumentException ex2 = assertThrows(MissingArgumentException.class, () ->
                CommandParser.parseCommand("mark    ")
        );
        String expected2 = "task number is missing!!";
        assertEquals(expected2, ex2.getMessage());

        // test 'mark' with 2 args
        ParseException ex3 = assertThrows(ParseException.class, () ->
                CommandParser.parseCommand("maRk 12 34")
        );
        String expected3 = "too many arguments. \nPlease provide one task number.";
        assertEquals(expected3, ex3.getMessage());

        // test 'unmark' with 1 arg that is NOT a number
        BotException ex4 = assertThrows(BotException.class, () ->
                CommandParser.parseCommand("UNmark  one")
        );
        String expected4 = String.format(
                "Illegal argument: '%s'.\nPlease provide a valid task number.", "one");
        assertEquals(expected4, ex4.getMessage());

        // test 'delete' with number and punctuation
        BotException ex5 = assertThrows(BotException.class, () ->
                CommandParser.parseCommand("delete 10!")
        );
        String expected5 = String.format(
                "Illegal argument: '%s'.\nPlease provide a valid task number.", "10!");
        assertEquals(expected5, ex5.getMessage());

        // test 'todo' with invalid args
        ParseException ex6 = assertThrows(ParseException.class, () ->
                CommandParser.parseCommand("TODO /priority HIGH")
        );
        String expected6 = "task description or priority is missing!!";
        assertEquals(expected6, ex6.getMessage());

        // test 'event' with invalid args - no from date passed
        ParseException ex7 = assertThrows(ParseException.class, () ->
                CommandParser.parseCommand("event task Description /from /to 2025-12-12  /priority medium")
        );
        String expected7 = String.format(
                "'%s'. \nPlease enter a valid future date in this format: yyyy-MM-dd.", "");
        assertEquals(expected7, ex7.getMessage());

        // test 'deadline' with invalid args - date is earlier than TODAY
        ParseException ex8 = assertThrows(ParseException.class, () ->
                CommandParser.parseCommand("DeAdLine read book /by 2024-11-11  /priority lOw")
        );
        String expected8 = String.format(
                "'%s'. \nPlease enter a valid future date in this format: yyyy-MM-dd.", "2024-11-11");
        assertEquals(expected8, ex8.getMessage());

        // test 'find' with invalid args - 0 args
        ParseException ex9 = assertThrows(ParseException.class, () ->
                CommandParser.parseCommand("find    ")
        );
        String expected9 = String.format("%s is missing!!", "search details");
        assertEquals(expected9, ex9.getMessage());
    }
}
