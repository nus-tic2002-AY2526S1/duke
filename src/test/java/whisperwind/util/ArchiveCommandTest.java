package whisperwind.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ArchiveCommand enum functionality
 */
class ArchiveCommandTest {

    @Test
    void testFromString_allCommand_returnsArchiveAll() {
        assertEquals(ArchiveCommand.ARCHIVE_ALL, ArchiveCommand.fromString("all"));
    }

    @Test
    void testFromString_completedCommand_returnsArchiveCompleted() {
        assertEquals(ArchiveCommand.ARCHIVE_COMPLETED, ArchiveCommand.fromString("completed"));
    }

    @Test
    void testFromString_todoCommand_returnsArchiveTodo() {
        assertEquals(ArchiveCommand.ARCHIVE_TODO, ArchiveCommand.fromString("todo"));
    }

    @Test
    void testFromString_deadlineCommand_returnsArchiveDeadline() {
        assertEquals(ArchiveCommand.ARCHIVE_DEADLINE, ArchiveCommand.fromString("deadline"));
    }

    @Test
    void testFromString_eventCommand_returnsArchiveEvent() {
        assertEquals(ArchiveCommand.ARCHIVE_EVENT, ArchiveCommand.fromString("event"));
    }

    @Test
    void testFromString_listCommand_returnsListArchives() {
        assertEquals(ArchiveCommand.LIST_ARCHIVES, ArchiveCommand.fromString("list"));
    }

    @Test
    void testFromString_viewCommand_returnsViewArchive() {
        assertEquals(ArchiveCommand.VIEW_ARCHIVE, ArchiveCommand.fromString("view 1"));
        assertEquals(ArchiveCommand.VIEW_ARCHIVE, ArchiveCommand.fromString("view 2"));
    }

    @Test
    void testFromString_unknownCommand_returnsUnknown() {
        assertEquals(ArchiveCommand.UNKNOWN, ArchiveCommand.fromString("invalid"));
        assertEquals(ArchiveCommand.UNKNOWN, ArchiveCommand.fromString(""));
        assertEquals(ArchiveCommand.UNKNOWN, ArchiveCommand.fromString(null));
    }

    @Test
    void testCommandProperties() {
        assertEquals("all", ArchiveCommand.ARCHIVE_ALL.getCommand());
        assertEquals("Archive all tasks", ArchiveCommand.ARCHIVE_ALL.getDescription());

        assertEquals("completed", ArchiveCommand.ARCHIVE_COMPLETED.getCommand());
        assertEquals("Archive completed tasks", ArchiveCommand.ARCHIVE_COMPLETED.getDescription());
    }
}