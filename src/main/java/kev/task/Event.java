package kev.task;

import kev.exception.KevException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * an event task that occurs on a specific date.
 */
public class Event extends Task {

    /** date the event occurs at. */
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;

    /**
     * creates an Event with both start and end date/time.
     *
     * @param description description of the event
     * @param startDate start date (yyyy-MM-dd)
     * @param startTime start time (HH:mm)
     * @param endDate end date (yyyy-MM-dd)
     * @param endTime end time (HH:mm)
     */
    public Event(String description, String startDate, String startTime,
                 String endDate, String endTime) throws KevException {
        super(description);
        this.startDate = LocalDate.parse(startDate);
        this.startTime = LocalTime.parse(startTime);
        this.endDate = LocalDate.parse(endDate);
        this.endTime = LocalTime.parse(endTime);

        if (this.endDate.isBefore(this.startDate) ||
                (this.endDate.equals(this.startDate) && this.endTime.isBefore(this.startTime))) {
            throw new KevException("Event must end after it starts.");
        }
    }

    public LocalDate getStartDate() { return startDate; }
    public LocalTime getStartTime() { return startTime; }
    public LocalDate getEndDate() { return endDate; }
    public LocalTime getEndTime() { return endTime; }

    /** to reschedule event with new start and end date/time.
     *
     * @param newStartDate updated start date (yyyy-MM-dd)
     * @param newStartTime updated start time (HH:mm)
     * @param newEndDate updated end date (yyyy-MM-dd)
     * @param newEndTime updated end time (HH:mm)
     * */
    public void reschedule(LocalDate newStartDate, LocalTime newStartTime,
                           LocalDate newEndDate, LocalTime newEndTime) throws KevException {
        if (newEndDate.isBefore(newStartDate) ||
                (newEndDate.equals(newStartDate) && newEndTime.isBefore(newStartTime))) {
            throw new KevException("Event must end after it starts.");
        }
        this.startDate = newStartDate;
        this.startTime = newStartTime;
        this.endDate = newEndDate;
        this.endTime = newEndTime;
    }

    /**
     * converts the event into a savable string format for file storage.
     *
     * @return Formatted string representing the event.
     */
    @Override
    public String toFileString() {
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | " +
                startDate + " " + startTime + " " + endDate + " " + endTime;
    }

    /**
     * returns a more easily referenced/readable representation of the event task.
     *
     * @return Display string for the event task.
     */
    @Override
    public String toString() {
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("MMM dd yyyy");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
        if (startDate.equals(endDate)) {
            // single-day event
            return "[E][" + getStatusIcon() + "] " + description +
                    " (" + startDate.format(dateFmt) + " " +
                    startTime.format(timeFmt) + "-" +
                    endTime.format(timeFmt) + ")";
        } else {
            // multi-day event
            return "[E][" + getStatusIcon() + "] " + description +
                    " (" + startDate.format(dateFmt) + " " +
                    startTime.format(timeFmt) + " to " +
                    endDate.format(dateFmt) + " " +
                    endTime.format(timeFmt) + ")";
        }
    }
}