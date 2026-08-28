package com.orderup.Models;


public class CustomerProcess implements Comparable<CustomerProcess> {

	public enum CharacterType {
		GIRL, MAN
	}

	private final int customerId;
	private final int arrivalTime;
	private int burstTime;
	private final CharacterType characterType;

	/** Creates a customer process with its scheduling values. */
	public CustomerProcess(int customerId, int arrivalTime, int burstTime) {
		this(customerId, arrivalTime, burstTime, (customerId % 2 != 0) ? CharacterType.GIRL : CharacterType.MAN);
	}

	/** Creates a customer process with its scheduling values and a chosen character type. */
	public CustomerProcess(int customerId, int arrivalTime, int burstTime, CharacterType characterType) {

		if (customerId < 0) throw new IllegalArgumentException("Customer ID cannot be negative");
		if (arrivalTime < 0) throw new IllegalArgumentException("Arrival time cannot be negative");
		this.customerId = customerId;
		this.arrivalTime = arrivalTime;
		this.burstTime = burstTime;
		this.characterType = characterType;
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

	public void setBurstTime(int burstTime) {
		this.burstTime = burstTime;
	}

	public CharacterType getCharacterType() {
		return characterType;
	}

	/** Returns whether this process's burst time has been fully served. */
	public boolean isBurstComplete() {
		return burstTime <= 0;
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
				", characterType=" + characterType +
				'}';
	}
}
