package whisperwind.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeleteOperationTest {

    @Test
    void testFromString_singleNumber_returnsSingle() {
        assertEquals(DeleteOperation.SINGLE, DeleteOperation.fromString("1"));
    }

    @Test
    void testFromString_commaSeparated_returnsBulk() {
        assertEquals(DeleteOperation.BULK, DeleteOperation.fromString("1,2,3"));
    }

    @Test
    void testFromString_completedKeyword_returnsCompleted() {
        assertEquals(DeleteOperation.COMPLETED, DeleteOperation.fromString("completed"));
    }

    @Test
    void testFromString_allKeyword_returnsAll() {
        assertEquals(DeleteOperation.ALL, DeleteOperation.fromString("all"));
    }

    @Test
    void testFromString_patternWithStar_returnsPattern() {
        assertEquals(DeleteOperation.PATTERN, DeleteOperation.fromString("book*"));
    }

    @Test
    void testFromString_unknownInput_returnsUnknown() {
        assertEquals(DeleteOperation.UNKNOWN, DeleteOperation.fromString("invalid"));
    }
}