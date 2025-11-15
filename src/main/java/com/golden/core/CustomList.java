package com.golden.core;

import com.golden.config.StaticConfig;
import com.golden.task.Task;
import com.golden.task.Todo;
import com.golden.task.Deadline;
import com.golden.task.Event;
import com.golden.task.Priority;
import com.golden.exceptions.validationErrors.IllegalArgumentException;
import com.golden.util.ValidationHelper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CustomList implements Iterable<Task> {
    // Changed from Task[] to ArrayList
    private static ArrayList<Task> taskList;

    public CustomList(ArrayList<Task> tasks){
        this.taskList = (tasks == null) ? new ArrayList<>() : tasks;
    }
    protected int getSize(){
        return taskList.size();
    }
    private boolean addTask(Task task){
        assert task != null;
        // checks tasklist capacity before adding new task
        if (taskList.size() < StaticConfig.MAX_TASKS) {
            taskList.add(task);
            return true;
        } else {
            return false;
        }
    }
    protected boolean addTodo(String taskDescription, Priority priority) {
        return addTask(new Todo(taskDescription, priority));
    }
    protected boolean addDeadline(String taskDescription, LocalDate endDate, Priority priority) {
        return addTask(new Deadline(taskDescription, endDate, priority));
    }
    protected boolean addEvent(String taskDescription, LocalDate startDate,
                            LocalDate endDate, Priority priority) {
        return addTask(new Event(taskDescription, startDate, endDate, priority));
    }
    protected String getList() {
        if (taskList.isEmpty()){
            return "Great news! You have no pending tasks right now.";
        }
        String message = "Here are the tasks in your list:\n";
        for (int i = 0; i < taskList.size(); i++){
            int number = i+1;
            message += String.format("%d.", number);
            message += taskList.get(i).toString();
            if (number != getSize()){
                message += "\n";
            }
        }
        return message;
    }
    protected void markTask(int taskNumber, boolean isMark) throws IllegalArgumentException {
        ValidationHelper.isNumberInRange(taskList.size(), taskNumber);
        int taskIndex = taskNumber - 1;
        taskList.get(taskIndex).setIsDone(isMark);
    }
    protected Task getTask(int taskNumber) throws IllegalArgumentException {
        ValidationHelper.isNumberInRange(taskList.size(), taskNumber);
        int taskIndex = taskNumber - 1;
        return taskList.get(taskIndex);
    }
    protected void deleteTask(int taskNumber) throws IllegalArgumentException {
        ValidationHelper.isNumberInRange(taskList.size(), taskNumber);
        int taskIndex = taskNumber - 1;
        taskList.remove(taskIndex);
    }
    @Override
    public java.util.Iterator<Task> iterator() {
        return taskList.iterator();
    }

    public List<Task> findByKeyword(String keyword){
        List<Task> matches = new ArrayList<>();
        if (keyword == null || keyword.trim().isBlank()){
            return matches;
        }

        final String normalisedStr = keyword.toLowerCase();

        for (Task t : this){
            if (t == null){
                continue;
            }
            String desc = t.getTaskDescription();
            if (desc != null && desc.toLowerCase().contains(normalisedStr)){
                matches.add(t);
            }
        }
        return matches;
    }

    // Feature to sort list by priority level (weights)
    private static int weightOf(Task t) {
        if (t == null) return Integer.MIN_VALUE; // push nulls to the start in ASC
        Priority p = t.getPriority();
        return (p == null) ? Integer.MIN_VALUE : p.getWeight();
    }

    private static final Comparator<Task> BY_PRIORITY_ASC =
            Comparator.comparingInt(CustomList::weightOf);

    private static final Comparator<Task> BY_PRIORITY_DESC =
            BY_PRIORITY_ASC.reversed();

    // --- In-place sort (mutates taskList) ---
    public void sortByPriorityAscending() {
        taskList.sort(BY_PRIORITY_ASC);
    }

    public void sortByPriorityDescending() {
        taskList.sort(BY_PRIORITY_DESC);
    }

}