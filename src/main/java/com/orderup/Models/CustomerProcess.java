package com.orderup.Models;


public class CustomerProcess implements Comparable<CustomerProcess> {

	private final int customerId;
	private final int arrivalTime;
	private final int burstTime;

	/** Creates a customer process with its scheduling values. */
	public CustomerProcess(int customerId, int arrivalTime, int burstTime) {

		if (customerId < 0) throw new IllegalArgumentException("Customer ID cannot be negative");
		if (arrivalTime < 0) throw new IllegalArgumentException("Arrival time cannot be negative");
		if (burstTime <= 0) throw new IllegalArgumentException("Burst time must be greater than zero");

		this.customerId = customerId;
		this.arrivalTime = arrivalTime;
		this.burstTime = burstTime;
	}

	public int getCustomerId() {
		return customerId;
	}

	public int getArrivalTime() {
		return arrivalTime;
	}

	public int getBurstTime() {
		return burstTime;
	}

	/** Returns whether this process has arrived by the given time. */
	public boolean isReadyAt(int currentTime) {
		return currentTime >= arrivalTime;
	}

	/** Calculates when the process finishes if it starts at the given time. */
	public int getCompletionTime(int startTime) {
		if (startTime < arrivalTime) {
			throw new IllegalArgumentException("Start time cannot be before arrival time");
		}
		return startTime + burstTime;
	}

	/** Calculates time spent waiting before this process starts. */
	public int getWaitingTime(int startTime) {
		if (startTime < arrivalTime) {
			throw new IllegalArgumentException("Start time cannot be before arrival time");
		}
		return startTime - arrivalTime;
	}

    /** Returns whether this process has the same arrival time */
 	public boolean hasSameArrivalTime(CustomerProcess otherProcess) {
 		return this.arrivalTime == otherProcess.arrivalTime;
 	}

	/** Sorts by arrival time, then by customer ID for stable ordering. */
	@Override
	public int compareTo(CustomerProcess other) {
		int cmp = Integer.compare(this.arrivalTime, other.arrivalTime);
		if (cmp != 0) return cmp;
		return Integer.compare(this.customerId, other.customerId);
	}

	@Override
	public String toString() {
		return "CustomerProcess{" +
				"customerId=" + customerId +
				", arrivalTime=" + arrivalTime +
				", burstTime=" + burstTime +
				'}';
	}
}
