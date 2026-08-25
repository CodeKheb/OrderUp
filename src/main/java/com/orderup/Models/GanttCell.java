package com.orderup.Models;

/**
 * A single block on the Gantt chart representing one scheduled process.
 *
 * <p>Each cell maps a {@link CustomerProcess} to a time interval [startTime, endTime)
 * on the chart. Cells are created by {@link GanttChart} when a process is assigned
 * a time slot during FCFS scheduling.</p>
 *
 * <p>In the scheduling-game analogy, a GanttCell is the colored bar segment
 * showing which customer is being served and for how long.</p>
 */
public class GanttCell {

    /** The customer process occupying this time slot. */
    private CustomerProcess process;

    /** The game-clock time when this process starts being served. */
    private int startTime;

    /** The game-clock time when this process finishes being served. */
    private int endTime;

    /**
     * Creates a Gantt cell for the given process and time interval.
     *
     * @param process   the customer process scheduled in this cell
     * @param startTime when the process starts (inclusive)
     * @param endTime   when the process finishes (exclusive)
     */
    public GanttCell(CustomerProcess process, int startTime, int endTime) {
        this.process = process;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /** Returns the customer process scheduled in this cell. */
    public CustomerProcess getProcessId() {
        return process;
    }

    /** Sets the customer process for this cell. */
    public void setProcessId(CustomerProcess process) {
        this.process = process;
    }

    /** Returns the time this cell starts on the chart. */
    public int getStartTime() {
        return startTime;
    }

    /** Sets the start time for this cell. */
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    /** Returns the time this cell ends on the chart. */
    public int getEndTime() {
        return endTime;
    }

    /** Sets the end time for this cell. */
    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}