package com.golden.task;

import com.golden.util.FormatHelper;

import java.time.LocalDate;

public class Event extends Task{
    LocalDate startDate;
    LocalDate endDate;

    public Event(String description, LocalDate from, LocalDate to, Priority priority) {
        super(description, priority);
        setStartDate(from);
        setEndDate(to);
    }
    public Event(String description, boolean isDone, LocalDate from, LocalDate to, Priority priority) {
        super(description, isDone, priority);
        setStartDate(from);
        setEndDate(to);
    }

    // Getters
    protected LocalDate getStartDate(){
        return this.startDate;
    }
    protected LocalDate getEndDate(){
        return this.endDate;
    }

    // Setters
    protected void setStartDate(LocalDate start){
        this.startDate = start;
    }
    protected void setEndDate(LocalDate end){
        this.endDate = end;
    }

    /**
     * Method to generate the relevant type character for serialising the task or representing the task
     *
     * @return a character unique to 'Event' task type
     */
    @Override
    protected char renderTypeTag(){
        return 'E';
    }

    @Override
    public String serialise(){
        return String.format("%c | %c | %s | %s | %s | %s", renderTypeTag(), renderStatusDigit(),
                getTaskDescription(), getStartDate(), getEndDate(), toPriorityString(getPriority()));
    }
    @Override
    public String toString() {
        return String.format("\t[%c][%c] %s (from: %s to: %s) (%s)", renderTypeTag(),
                renderStatusCharacter(), getTaskDescription(),
                FormatHelper.displayAsMMMdyyyy(getStartDate()), FormatHelper.displayAsMMMdyyyy(getEndDate()),
                toPriorityString(getPriority()));
    }
}