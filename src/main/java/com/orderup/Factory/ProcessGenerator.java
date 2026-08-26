package com.orderup.Factory;

import java.util.ArrayList;
import java.util.List;

import com.orderup.Models.CustomerProcess;

/**
 * Creates {@link CustomerProcess} instances for the game.
 *
 * <p>Used by both Play mode (random generation) and Manual mode (user-provided values).
 * In Play mode, a random number of processes are created with random arrival and burst
 * times. In Manual mode, each process is created from user input.</p>
 *
 * <p>In the scheduling-game analogy, this factory represents the "order tickets"
 * that enter the restaurant — either auto-generated or hand-written by the player.</p>
 */
public class ProcessGenerator {

    /** Minimum number of processes generated in Play mode. */
    private final int MINIMUM_SIZE = 2;

    /** Maximum number of processes generated in Play mode. */
    private final int MAXIMUM_SIZE = 6;

    /**
     * Generates a random list of customer processes for Play mode.
     *
     * <p>The list size is random between {@link #MINIMUM_SIZE} and {@link #MAXIMUM_SIZE}.
     * The first process always arrives at time 0. Subsequent processes get unique random
     * arrival times (1-8) and burst times (1-8).</p>
     *
     * @return a list of randomly generated customer processes
     */
    public List<CustomerProcess> createRandom(){
        int rangeSize = (MAXIMUM_SIZE - MINIMUM_SIZE) + 1;
        int listSize = (int) (Math.random() * rangeSize) + MINIMUM_SIZE;
        List<CustomerProcess> processes = new ArrayList<>();

        processes.add(new CustomerProcess(1, 0, (int) (Math.random() * 8) + 1));

        for (int i = 1; i < listSize; i++) {
            CustomerProcess newProcess;
            do { // Creates the process' unique arrival time and burst time
                int arrival = (int) (Math.random() * 8) + 1;
                int burst = (int) (Math.random() * 8) + 1;
                newProcess = new CustomerProcess((i + 1), arrival, burst);
            } // checks if a process' arrival time matches with any other process' arrival time
            while (processes.stream().anyMatch(newProcess::hasSameArrivalTime));
            processes.add(newProcess);
        }

        return processes;
    }

    /**
     * Creates a single customer process from user-provided values (Manual mode).
     *
     * @param customerId the unique identifier for this customer
     * @param AT         the arrival time (when the customer shows up)
     * @param BT         the burst time (how long the order takes)
     * @return a new CustomerProcess with the given values
     */
    public CustomerProcess createManual(int customerId, int AT, int BT){
        return new CustomerProcess(customerId, AT, BT);
    }
}
