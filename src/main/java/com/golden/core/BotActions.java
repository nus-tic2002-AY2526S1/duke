package com.golden.core;

import com.golden.storage.Storage;
import com.golden.exceptions.validationErrors.IllegalArgumentException;
import com.golden.exceptions.storageErrors.StorageFileParseException;
import com.golden.task.Priority;
import com.golden.task.Task;

import java.time.LocalDate;
import java.util.List;

public class BotActions {
    CustomList myList;
    Storage storage;

    public BotActions(CustomList list, Storage storage) {
        this.myList = list;
        this.storage = storage;
    }

    private int getLastTaskNumber() {
        return myList.getSize();
    }

    public String listTasks() {
        // list is automatically sorted from High to Low priority on 'list' command
        myList.sortByPriorityDescending();
        return myList.getList();
    }

    public String constructAddTaskMsg() throws IllegalArgumentException {
        String message = "Got it. I've added this task:\n"
                + getTaskString(getLastTaskNumber())
                + "\nNow you have " + myList.getSize() + " task(s) in the list.";
        return message;
    }

    public String constructMarkItemMsg(int taskNumber, boolean isMark) throws IllegalArgumentException {
        String message;
        if (isMark) {
            message = "Nice! I have marked this task as done:\n";
            message += getTaskString(taskNumber);
        } else {
            message = "OK, I've marked this task as not done yet:\n";
            message += getTaskString(taskNumber);
        }
        return message;
    }

    public void mark(int taskNumber) throws IllegalArgumentException {
        myList.markTask(taskNumber, true);
    }

    public void unmark(int taskNumber) throws IllegalArgumentException  {
        myList.markTask(taskNumber, false);
    }

    public boolean addTodo(String task, Priority priority) {
        assert task != null && !task.isBlank() : "Task description must not be null or blank";
        assert priority != null : "Priority must not be null";
        return myList.addTodo(task, priority);
    }

    public boolean addDeadline(String task, LocalDate deadlineBy, Priority priority) {
        assert task != null && !task.isBlank() : "Task description must not be null or blank";
        assert deadlineBy != null : "By date must not be null";
        assert priority != null : "Priority must not be null";
        return myList.addDeadline(task, deadlineBy, priority);
    }

    public boolean addEvent(String task, LocalDate eventFrom, LocalDate eventTo, Priority priority) {
        assert task != null && !task.isBlank() : "Task description must not be null or blank";
        assert eventFrom != null : "From date must not be null";
        assert eventTo != null : "To date must not be null";
        assert priority != null : "Priority must not be null";
        return myList.addEvent(task, eventFrom, eventTo, priority);
    }

    public void delete(int taskNumber) throws IllegalArgumentException {
        myList.deleteTask(taskNumber);
    }

    public String constructDeletedTaskMsg(String toDeleteTask) {
        String message = "Done! I have removed this task:\n";
        message += toDeleteTask;
        message += "\nYou have: " + myList.getSize() + " pending tasks.";
        return message;
    }

    public String getTaskString(int taskNumber) throws IllegalArgumentException {
        return myList.getTask(taskNumber).toString();
    }

    public void saveTaskList() throws StorageFileParseException {
        storage.writeToFile(myList);
    }

    public List<Task> searchTasks(String searchParams){
        assert searchParams != null && !searchParams.isBlank() : "Search parameters must not be null or blank";
        return myList.findByKeyword(searchParams);
    }
}
