package moonchester_data;

import java.time.LocalDateTime;
import java.util.ArrayList;

import moonchester_utils.MoonchesterDate;

/**
 * Represents the list of tasks managed by the user
 * Handles the storage, addition, deletion, and retrieval of Task objects
 */
public class UserList {

    private ArrayList<Task> userList = new ArrayList<>();
    /**
     * Constructs an empty UserList object
     * Intialises a new ArrayList to store Task objects
     */
    public UserList() {
        userList = new ArrayList<>();
    }

    /**
     * Constructs a UserList object with an existing list of tasks.
     * 
     * @param userListInit The initial ArrayList of Task objects to populate the UserList
     */
    public UserList(ArrayList<Task> userListInit) {
        this.userList = userListInit;
    }

    /**
     * Adds a task to the userList object
     * 
     * @param item Task to be added
     */
    public void addItem (Task item) {
        userList.add(item);
        assert this.getSize() > 0 : "Size of UserList should be more than 0";
        System.out.println("[+] Added : " + item.printString());
        System.out.println("[*] You have a total of " + this.getSize() + " task(s)");
        System.out.println("____________________________________________________________");
    }

    /**
     * Deletes a task from the userList object
     * 
     * @param itemIndex Index of the Task to be deleted
     * @param item Task to be deleted
     */
    public void deleteItem (int itemIndex, Task item) {
        userList.remove(itemIndex);
        System.out.println("[+] Deleted : " + item.printString());
        System.out.println("[*] You have a total of " + this.getSize() + " task(s)");
        System.out.println("____________________________________________________________");
    }

    /**
     * Returns the userList object as a new ArrayList<> object
     * 
     * @return ArrayList<Task> Current userList object
     */
    public ArrayList<Task> getList () {
        return new ArrayList<>(userList);
    }


    /**
     * Getlist based on the specific date given by user
     * This is method overloading, the difference between the 2 methods is that this method takes in a specific date (LocalDateTime object)
     * 
     * @param specificDate Specific Date that user specifies, in LocalDateTime object
     * @return queriedList ArrayList<Task> of Tasks that are due on a specific date
     */
    public ArrayList<Task> getList(LocalDateTime specificDate) {
        ArrayList<Task> queriedList = new ArrayList<>();
        for(Task curr : userList) {
            if (curr instanceof Deadline deadlineObject) {
                if (MoonchesterDate.compareDate(specificDate, deadlineObject.getByObject())) {
                    queriedList.add(deadlineObject);
                }
            }
            if (curr instanceof Event eventObject) {
                if (MoonchesterDate.compareDate(specificDate, eventObject.getFromObject())) {
                    queriedList.add(eventObject);
                }
            }
        }
        return queriedList;
    }


    /**
     * Getlist based on the specific keyword given by user
     * This is method overloading, the difference between the other methods is that this method takes a string - keyword.
     * 
     * @param keyword Specific keyword that user specifies
     * @return queriedList ArrayList<Task> of Tasks that contains the specific word
     */
    public ArrayList<Task> getList(String keyword) {
        ArrayList<Task> queriedList = new ArrayList<>();
        for(Task curr : userList) {
            if (curr instanceof Deadline deadlineObject) {
                if (deadlineObject.getDescription().contains(keyword) == true) {
                    queriedList.add(deadlineObject);
                }
            }
            if (curr instanceof Event eventObject) {
                if (eventObject.getDescription().contains(keyword) == true) {
                    queriedList.add(eventObject);
                }
            }
            if (curr instanceof Todo todoObject) {
                if (todoObject.getDescription().contains(keyword) == true) {
                    queriedList.add(todoObject);
                }
            }
        }
        return queriedList;
    }

    /**
     * Gets the specific task within the userList object
     * 
     * @param index Index of the specific task
     * @return Task The specific Task
     */
    public Task getSpecificTask(int index) {
        return this.userList.get(index-1);
    }

    /**
     * Get the size of the userlist
     * @return size Size of the userList object
     */
    public int getSize() {
        return this.userList.size();
    }



}