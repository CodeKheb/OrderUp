package com.orderup.Models;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Manages a text-based FCFS scheduling queue display.
 *
 * <p>
 * Processes are displayed with their arrival time as a readable AM clock
 * (e.g. "7:00 AM (0)"). No burst time logic for now — just a list of arrived
 * processes.
 * </p>
 */
public class ProcessDisplay {

    /** Base hour in 24h format (7 AM = 7). */
    private static final int BASE_HOUR = 7;

    /** Minutes per arrival-time tick (20 minutes). */
    private static final int MINUTES_PER_TICK = 20;

    /** Font for process text lines. */
    private static final Font PROCESS_FONT = Font.font("Monospace", FontWeight.NORMAL, 16);

    /** Font for the header. */
    private static final Font HEADER_FONT = Font.font("Monospace", FontWeight.BOLD, 18);

    /** Vertical spacing between text lines. */
    private static final double LINE_HEIGHT = 28;

    /** All processes that have arrived, in order. */
    private final List<CustomerProcess> processes = new ArrayList<>();

    /** The JavaFX Group holding all text nodes. Add this to the scene. */
    private final Group displayGroup = new Group();

    /** Header text. */
    private final Text headerText;

    /** Individual process text lines. */
    private final List<Text> processLines = new ArrayList<>();

    /** Reference to the game clock for time conversion. */
    private final GameClock gameClock;

    /**
     * Creates a ProcessDisplay.
     *
     * @param gameClock    the game clock (used to convert ticks to AM time)
     * @param maxBurstTime unused, kept for future progress bar
     */
    public ProcessDisplay(GameClock gameClock, int maxBurstTime) {
        this.gameClock = gameClock;

        this.headerText = new Text("ORDER UP    Arrival Time   Patience");
        this.headerText.setFont(HEADER_FONT);
        this.headerText.setFill(Color.web("#cc5114"));
        displayGroup.getChildren().add(headerText);
    }

    /**
     * Call each game-clock tick to refresh the display.
     *
     * @param currentTick the current tick (0 = 7:00 AM, 1 = 7:20 AM, etc.)
     */
    public void update(int currentTick) {
        refreshDisplay(currentTick);
    }

    /**
     * Adds a newly arrived process to the display list.
     *
     * @param process the arrived process
     */
    public void addProcess(CustomerProcess process) {
        processes.add(process);
    }

    /**
     * Refreshes all text nodes to reflect current processes.
     */
    private void refreshDisplay(int currentTick) {
        for (Text line : processLines) {
            displayGroup.getChildren().remove(line);
        }
        processLines.clear();

        double y = LINE_HEIGHT + 4; // start below header

        for (int i = 0; i < processes.size(); i++) {
            CustomerProcess p = processes.get(i);
            String atTime = formatArrivalTime(p.getArrivalTime());

            Text line = new Text("Customer " + p.getCustomerId() + "    " + atTime + " (" + p.getArrivalTime() + ")"
                    + "        " + p.getBurstTime());
            line.setFont(PROCESS_FONT);

            if (i == 0) {
                line.setFill(Color.WHITE);
                line.setFont(Font.font("Monospace", FontWeight.BOLD, 16));
            } else {
                line.setFill(Color.web("#cccccc"));
            }

            line.setTranslateY(y);
            displayGroup.getChildren().add(line);
            processLines.add(line);
            y += LINE_HEIGHT;
        }

        if (processLines.isEmpty()) {
            Text empty = new Text("No processes yet...");
            empty.setFont(PROCESS_FONT);
            empty.setFill(Color.web("#888888"));
            empty.setTranslateY(y);
            displayGroup.getChildren().add(empty);
            processLines.add(empty);
        }
    }

    /**
     * Converts a raw arrival-time tick to a readable AM clock string.
     * AT 0 = 7:00 AM, AT 1 = 7:20 AM, AT 2 = 7:40 AM, etc.
     *
     * @param arrivalTick the raw arrival time (0, 1, 2, ...)
     * @return formatted string like "7:00 AM"
     */
    public static String formatArrivalTime(int arrivalTick) {
        int totalMinutes = BASE_HOUR * 60 + arrivalTick * MINUTES_PER_TICK;
        int hours24 = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        String amPm;
        int hours12;
        if (hours24 == 0) {
            hours12 = 12;
            amPm = "AM";
        } else if (hours24 < 12) {
            hours12 = hours24;
            amPm = "AM";
        } else if (hours24 == 12) {
            hours12 = 12;
            amPm = "PM";
        } else {
            hours12 = hours24 - 12;
            amPm = "PM";
        }

        return String.format("%02d:%02d %s", hours12, minutes, amPm);
    }	/** Checks if a process with this customer ID is already displayed. */
	public boolean containsProcess(int customerId) {
		return processes.stream().anyMatch(p -> p.getCustomerId() == customerId);
	}

	/** Removes a process by customer ID from the display list. */
	public void removeProcess(int customerId) {
		processes.removeIf(p -> p.getCustomerId() == customerId);
	}

    /** Returns the JavaFX Group containing all display text nodes. */
    public Group getDisplayGroup() {
        return displayGroup;
    }

    /** Returns the number of processes displayed. */
    public int getQueueSize() {
        return processes.size();
    }

    /** Returns true if no processes have been added. */
    public boolean isEmpty() {
        return processes.isEmpty();
    }
}
