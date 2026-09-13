package com.orderup.Models;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Manages a text-based FCFS scheduling queue display.
 *
 * <p>
 * Processes are displayed with their arrival time as a readable AM clock
 * (e.g. "7:00 AM (0)"). Each column is a separately positioned text node
 * at a fixed pixel x-offset, so alignment never depends on the font
 * actually being monospace (Font.font("Monospace") can fall back to a
 * proportional font, which previously broke the char-count padding).
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

    /** X offset of the Arrival Time column (pixels from the group origin). */
    private static final double COL_ARRIVAL_X = 130;

    /** Left edge of the Patience column zone (pixels). */
    private static final double COL_PATIENCE_X = 300;

    /** Width of the Patience column zone; its text is centered inside it (pixels). */
    private static final double COL_PATIENCE_WIDTH = 100;

    /** All processes that have arrived, in order. */
    private final List<CustomerProcess> processes = new ArrayList<>();

    /** The JavaFX Group holding all text nodes. Add this to the scene. */
    private final Group displayGroup = new Group();

    /** Individual process rows. */
    private final List<Node> processRows = new ArrayList<>();

    /**
     * Creates a ProcessDisplay.
     *
     * @param gameClock    the game clock (used to convert ticks to AM time)
     * @param maxBurstTime unused, kept for future progress bar
     */
    public ProcessDisplay(GameClock gameClock, int maxBurstTime) {
        // Header columns at the exact same pixel offsets as the data rows:
        // "ORDER UP" and "Arrival Time" are left-aligned at their column
        // start; "Patience" is centered within its zone. Original orange
        // header color preserved.
        Color headerColor = Color.web("#cc5114");
        Group header = new Group(
                columnText("ORDER UP", 0, 0, headerColor, HEADER_FONT),
                columnText("Arrival Time", COL_ARRIVAL_X, 0, headerColor, HEADER_FONT),
                columnText("Patience", COL_PATIENCE_X, COL_PATIENCE_WIDTH, headerColor, HEADER_FONT));

        displayGroup.setLayoutX(300);

        displayGroup.getChildren().add(header);
    }

    /**
     * Builds one column cell. When {@code centeredZoneWidth} is greater
     * than zero the text is horizontally centered within a zone of that
     * width starting at {@code x}; otherwise it is left-aligned at
     * {@code x}.
     *
     * @param content           the text content
     * @param x                 pixel x-offset of the column
     * @param centeredZoneWidth zone width to center within, or 0 for left-aligned
     * @param color             the text fill color
     * @param font              the font to use
     * @return the positioned text node
     */
    private static Text columnText(String content, double x,
                                   double centeredZoneWidth, Color color, Font font) {
        Text text = new Text(content);
        text.setFont(font);
        text.setFill(color);
        if (centeredZoneWidth > 0) {
            // Centering via wrapping-width + CENTER alignment: JavaFX
            // centers the (shorter) line inside the wrapping width, giving
            // pixel-exact centering independent of the font.
            text.setWrappingWidth(centeredZoneWidth);
            text.setTextAlignment(TextAlignment.CENTER);
        }
        text.setTranslateX(x);
        return text;
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
     * Refreshes all rows to reflect current processes.
     */
    private void refreshDisplay(int currentTick) {
        for (Node row : processRows) {
            displayGroup.getChildren().remove(row);
        }
        processRows.clear();

        double y = LINE_HEIGHT + 4; // start below header

        for (int i = 0; i < processes.size(); i++) {
            CustomerProcess p = processes.get(i);
            String atTime = formatArrivalTime(p.getArrivalTime());

            // Each column is its own node at a fixed pixel x — single vs
            // double-digit AT/IDs can never shift the later columns.
            // The patience digit is centered in its zone so it sits under
            // the middle of the "Patience" header. Original colors kept:
            // first row white + bold, the rest light grey.
            Font font = PROCESS_FONT;
            Color color = Color.web("#cccccc");
            if (i == 0) {
                font = Font.font("Monospace", FontWeight.BOLD, 16);
                color = Color.WHITE;
            }

            Group row = new Group(
                    columnText("Customer " + p.getCustomerId(), 0, 0, color, font),
                    columnText(atTime + " (" + p.getArrivalTime() + ")",
                            COL_ARRIVAL_X, 0, color, font),
                    columnText(String.valueOf(p.getBurstTime()),
                            COL_PATIENCE_X, COL_PATIENCE_WIDTH, color, font));

            row.setTranslateY(y);
            displayGroup.getChildren().add(row);
            processRows.add(row);
            y += LINE_HEIGHT;
        }

        if (processRows.isEmpty()) {
            Text empty = new Text("No processes yet...");
            empty.setFont(PROCESS_FONT);
            empty.setFill(Color.web("#888888"));
            empty.setTranslateY(y);
            displayGroup.getChildren().add(empty);
            processRows.add(empty);
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
    }

	/** Checks if a process with this customer ID is already displayed. */
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
