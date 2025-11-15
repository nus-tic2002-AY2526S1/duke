package com.golden.task;

import com.golden.util.FormatHelper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task{
    private LocalDate endDate;

    public Deadline(String description, LocalDate by, Priority priority) {
        super(description, priority);
        setBy(by);
    }
    public Deadline(String description, boolean isDone, LocalDate by, Priority priority) {
        super(description, isDone, priority);
        setBy(by);
    }

    // Getters
    protected LocalDate getEndDate(){
        return this.endDate;
    }

    // Setters
    protected void setBy(LocalDate by){
        this.endDate = by;
    }

    /**
     * Method to generate the relevant type character for serialising the task or representing the task
     *
     * @return a character unique to 'Deadline' task type
     */
    @Override
    protected char renderTypeTag() {
        return 'D';
    }

    @Override
    public String serialise(){
        return String.format("%c | %c | %s | %s | %s",
                renderTypeTag(), renderStatusDigit(), getTaskDescription(),
                getEndDate(), toPriorityString(getPriority()));
    }
    @Override
    public String toString() {
        return String.format("\t[%c][%c] %s (by: %s) (%s)",
                renderTypeTag(), renderStatusCharacter(), getTaskDescription(),
                FormatHelper.displayAsMMMdyyyy(getEndDate()), toPriorityString(getPriority()));
    }
}