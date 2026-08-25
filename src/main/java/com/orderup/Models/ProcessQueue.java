package com.orderup.Models;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages the list of {@link CustomerProcess} for a game session.
 *
 * <p>Acts as the shared state between the menu (which populates it) and the game
 * loop (which reads from it). After processes are created by {@link com.orderup.Factory.ProcessGenerator},
 * they are stored here and queried each game-clock tick to determine which customers
 * have arrived and are ready to be spawned into the waiting line.</p>
 *
 * <p>In the scheduling-game analogy, this is the "order queue" — the pile of
 * tickets waiting to be picked up by the kitchen.</p>
 */
public class ProcessQueue {

    /** All customer processes for the current game session. */
    private List<CustomerProcess> processList;

    /**
     * Creates a process queue with the given list of processes.
     *
     * @param processList the initial set of customer processes
     */
    public ProcessQueue(List<CustomerProcess> processList) {
        this.processList = processList;
    }

    /** Returns the full list of processes in this queue. */
    public List<CustomerProcess> getProcessList() {
        return processList;
    }

    /** Replaces the process list (e.g., when restarting a game). */
    public void setProcessList(List<CustomerProcess> processList) {
        this.processList = processList;
    }

    /**
     * Returns all processes whose arrival time is at or before the given game clock.
     *
     * <p>Called each tick by the game loop to determine which customers should
     * be spawned into the waiting line. A process is considered "arrived" when
     * its arrival time is less than or equal to the current game-clock time.</p>
     *
     * @param gameClock the current game-clock time
     * @return the list of processes that have arrived by this time
     */
    public List<CustomerProcess> getArrivedProcesses(int gameClock){
        List<CustomerProcess> arrivedProcesses = processList.stream()
                                                    .filter(process -> process.getArrivalTime() <= gameClock)
                                                    .sorted()
                                                    .collect(Collectors.toList());
        
        return arrivedProcesses;
    }
}
