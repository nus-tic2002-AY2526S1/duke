package nerunerune.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import nerunerune.exception.NeruneruneException;
import nerunerune.task.Deadline;
import nerunerune.task.Event;
import nerunerune.task.Task;
import nerunerune.task.Todo;

public class ParserTest {

    // getCommand tests
    @Test
    public void getCommand_TestGetFirstWordFromString() {
        assertEquals("todo", Parser.getCommand("todo buy books"));
        assertEquals("deadline", Parser.getCommand("deadline homework /by 01-02-2025 1500"));
        assertEquals("event", Parser.getCommand("event meeting /from 01-02-2025 1500 /to 01-02-2025 1570"));
    }

    @Test
    public void getCommand_singleWord_returnsWord() {
        assertEquals("list", Parser.getCommand("list"));
        assertEquals("bye", Parser.getCommand("bye"));
    }

    @Test
    public void getCommand_uppercaseInput_returnsLowercase() {
        assertEquals("todo", Parser.getCommand("TODO buy books"));
        assertEquals("list", Parser.getCommand("LIST"));
    }

    // getArguments tests
    @Test
    public void getArguments_TestGetRemainingSentence() {
        assertEquals("buy books", Parser.getArguments("todo buy books"));
        assertEquals("homework /by 01-02-2025 1500", Parser.getArguments("deadline homework /by 01-02-2025 1500"));
        assertEquals("meeting /from 01-02-2025 1500 /to 01-02-2025 1570", Parser.getArguments("event meeting /from 01-02-2025 1500 /to 01-02-2025 1570"));
    }

    @Test
    public void getArguments_noArguments_returnsEmptyString() {
        assertEquals("", Parser.getArguments("list"));
        assertEquals("", Parser.getArguments("bye"));
    }

    @Test
    public void getArguments_multipleSpaces_returnsTrimmedArguments() {
        assertEquals("buy books", Parser.getArguments("todo    buy books"));
    }

    // parseTodo tests
    @Test
    public void parseTodo_validInput_returnTodoInstance() {
        Task task = Parser.parseTodo("buy books");
        assertInstanceOf(Todo.class, task);
    }

    // parseDeadline tests
    @Test
    public void parseDeadline_validInput_returnDeadlineInstance() throws IOException, NeruneruneException {
        Task task = Parser.parseDeadline("buy books /by 01-02-2025 1500");
        assertInstanceOf(Deadline.class, task);
    }

    // parseEvent tests
    @Test
    public void parseEvent_validInput_returnEventInstance() throws IOException, NeruneruneException {
        Task task = Parser.parseEvent("meeting  /from 01-02-2025 1500 /to 01-02-2025 1530");
        assertInstanceOf(Event.class, task);
    }

    @Test
    public void parseDeadline_validInput_correctDescription() throws Exception {
        Task task = Parser.parseDeadline("homework /by 01-02-2025 1500");
        assertEquals("homework", task.getDescription());
    }

    @Test
    public void parseEvent_validInput_correctDescription() throws Exception {
        Task task = Parser.parseEvent("meeting /from 01-02-2025 1500 /to 01-02-2025 1600");
        assertEquals("meeting", task.getDescription());
    }

    // parseTaskLine tests
    @Test
    public void parseTaskLine_todoFormat_returnTodo() throws Exception {
        Task task = Parser.parseTaskLine("T | 0 | buy books");
        assertInstanceOf(Todo.class, task);
        assertEquals("buy books", task.getDescription());
        assertFalse(task.getIsDone());
    }

    @Test
    public void parseTaskLine_todoMarkedDone_returnCompletedTodo() throws Exception {
        Task task = Parser.parseTaskLine("T | 1 | buy books");
        assertTrue(task.getIsDone());
    }

    @Test
    public void parseTaskLine_deadlineFormat_returnDeadline() throws Exception {
        Task task = Parser.parseTaskLine("D | 0 | homework | Aug 02 2025 1845");
        assertInstanceOf(Deadline.class, task);
        assertEquals("homework", task.getDescription());
    }

    @Test
    public void parseTaskLine_eventFormat_returnEvent() throws Exception {
        Task task = Parser.parseTaskLine("E | 0 | meeting | Aug 03 2025 1400 | Aug 03 2025 1530");
        assertInstanceOf(Event.class, task);
        assertEquals("meeting", task.getDescription());
    }

    @Test
    public void parseTodo_emptyDescription_createTodoWithEmptyDescription() {
        Task task = Parser.parseTodo("");
        assertEquals("", task.getDescription());
    }

    @Test
    public void parseTodo_specialCharacters_preserveCharacters() {
        Task task = Parser.parseTodo("buy @#$ symbols!");
        assertEquals("buy @#$ symbols!", task.getDescription());
    }

    // test throw exceptions
    @Test
    public void parseDeadline_missingByDelimiter_throwIOException() {
        assertThrows(IOException.class, () -> Parser.parseDeadline("homework 01-02-2025 1500"));
    }

    @Test
    public void parseEvent_missingFromDelimiter_throwIOException() {
        assertThrows(IOException.class, () -> Parser.parseEvent("meeting /to 01-02-2025 1500"));
    }

    @Test
    public void parseEvent_missingToDelimiter_throwIOException() {
        assertThrows(IOException.class, () -> Parser.parseEvent("meeting /from 01-02-2025 1500"));
    }

    @Test
    public void parseEvent_wrongDelimiterOrder_throwIOException() {
        assertThrows(IOException.class, () -> Parser.parseEvent("meeting /to 01-02-2025 1500 /from 01-02-2025 1600"));
    }

    @Test
    public void parseTaskLine_corruptedLine_throwIOException() {
        assertThrows(IOException.class, () -> Parser.parseTaskLine("T | 0"));
    }

    @Test
    public void parseTaskLine_unknownTaskType_throwIOException() {
        assertThrows(IOException.class, () -> Parser.parseTaskLine("X | 0 | unknown task"));
    }
}