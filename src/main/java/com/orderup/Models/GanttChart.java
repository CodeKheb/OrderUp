package com.orderup.Models;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generates a First Come First Serve (FCFS) Gantt chart from a list of customer processes.
 *
 * <p>In the game, a {@link CustomerProcess} represents an order with an arrival time
 * (when the customer shows up) and a burst time (how long the order takes). This class
 * sorts processes by arrival time and schedules them back-to-back, producing a list of
 * {@link GanttCell}s that represent each order's time slot on the chart.</p>
 *
 * <p>Idle gaps are automatically inserted when a process arrives after the previous
 * one has finished (e.g., no customers in the restaurant).</p>
 */
public class GanttChart {

    /** All known processes, sorted by arrival time after generateGanttChart is called. */
    private List<CustomerProcess> availableProcesses;

    /** The resulting Gantt chart: an ordered list of time slots, one per scheduled process. */
    private List<GanttCell> ganttChart = new ArrayList<>();

    /**
     * Generates the full FCFS Gantt chart from a list of processes.
     *
     * <p>Processes are sorted by arrival time, then each is scheduled back-to-back.
     * If a process arrives after the previous one finishes, idle time is inserted
     * so the cell starts at the process's arrival time.</p>
     *
     * @param processes the list of customer processes to schedule
     * @return the ordered list of Gantt cells representing the schedule
     */
    public List<GanttCell> generateGanttChart(List<CustomerProcess> processes){

        availableProcesses = processes.stream()
                                    .sorted(Comparator.comparingInt(CustomerProcess::getArrivalTime))
                                    .collect(Collectors.toList());

        int currentTime = 0;

        for (CustomerProcess process : availableProcesses) {
            if (currentTime < process.getArrivalTime()) {
                // Insert idle cell for the gap
                GanttCell idleCell = new GanttCell(null, currentTime, process.getArrivalTime());
                ganttChart.add(idleCell);
                currentTime = process.getArrivalTime();
            }


            GanttCell ganttCell = new GanttCell(process, currentTime, currentTime + process.getBurstTime());
            ganttChart.add(ganttCell);

            currentTime += process.getBurstTime();
        }

        return ganttChart;
    }
}
