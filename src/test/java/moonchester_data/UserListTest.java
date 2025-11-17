package moonchester_data;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserListTest {

    private UserList userList;


    /**
     * Tests if a new instance of UserList is created for each test
     */
    @BeforeEach
    public void createNewUserList() {
        userList = new UserList();
    }


    /**
     * Test adding 2 tasks to the userList
     * Checks if the size of userList is 2 after adding 2
     */
    @Test
    public void testaddItem() {
        Todo todo1 = new Todo("Task 1");
        Todo todo2 = new Todo("Task 2");
        userList.addItem(todo1);
        userList.addItem(todo2);

        assertEquals(2, userList.getSize(), "Size should be 2 after adding two tasks");
        assertEquals(todo1, userList.getSpecificTask(1));
        assertEquals(todo2, userList.getSpecificTask(2));
    }

    /**
     * Tests if userList and another copy of userList are independent
     */
    @Test
    public void testGetList() {
        Todo todo = new Todo("Testing if userList and fakeList are independent - fakeList is a copy of userList");
        userList.addItem(todo);
        ArrayList<Task> fakeList = userList.getList();
        fakeList.clear();
        assertEquals(1, userList.getSize(), "Clearing returned list should not clear internal list");
    }
    
}
