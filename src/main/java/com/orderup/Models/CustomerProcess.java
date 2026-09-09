package com.orderup.Models;


public class CustomerProcess implements Comparable<CustomerProcess> {

	public enum CharacterType {
		GIRL1, GIRL2, GIRL3, MAN1, MAN2, MAN3;

		/** Returns true if this is any girl variant. */
		public boolean isGirl() {
			return this == GIRL1 || this == GIRL2 || this == GIRL3;
		}

		/** Returns true if this is any man variant. */
		public boolean isMan() {
			return this == MAN1 || this == MAN2 || this == MAN3;
		}

		/** Returns the sprite variant index (1-3) for this character type. */
		public int getSpriteIndex() {
			return switch (this) {
				case GIRL1, MAN1 -> 1;
				case GIRL2, MAN2 -> 2;
				case GIRL3, MAN3 -> 3;
			};
		}

		/** Returns the prefix ("girl" or "man") for this character type. */
		public String getPrefix() {
			return isGirl() ? "girl" : "man";
		}

		/** Returns the full sprite name (e.g. "girl2_idle.png"). */
		public String getSpriteFile(String suffix) {
			return getPrefix() + getSpriteIndex() + "_" + suffix;
		}

		/** Returns the number of idle frames for this character type. */
		public int getIdleFrameCount() {
			if (isGirl()) {
				return switch (this) {
					case GIRL1 -> 9;
					case GIRL2 -> 7;
					case GIRL3 -> 6;
					default -> 9;
				};
			}
			return 6; // man idle frames
		}

		/** Returns all character types in display order (girl1, man1, girl2, man2, girl3, man3). */
		public static CharacterType[] displayOrder() {
			return new CharacterType[] { GIRL1, MAN1, GIRL2, MAN2, GIRL3, MAN3 };
		}
	}

	private final int customerId;
	private final int arrivalTime;
	private int burstTime;
	private final CharacterType characterType;

	/** Creates a copy of an existing customer process. */
	public CustomerProcess(CustomerProcess other) {
		this.customerId = other.customerId;
		this.arrivalTime = other.arrivalTime;
		this.burstTime = other.burstTime;
		this.characterType = other.characterType;
	}

	/** Creates a customer process with its scheduling values. */
	public CustomerProcess(int customerId, int arrivalTime, int burstTime) {
		this(customerId, arrivalTime, burstTime, CharacterType.displayOrder()[(customerId - 1) % 6]);
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
