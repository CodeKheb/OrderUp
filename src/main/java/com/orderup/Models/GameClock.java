package com.orderup.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * A text-based game clock using wall-clock time and JavaFX property binding.
 *
 * <p>Uses {@link System#nanoTime()} instead of frame-based {@code tpf},
 * so the clock ticks at a consistent real-time rate regardless of game
 * loop lag or entity spawning.</p>
 *
 * <p>Exposes an {@link IntegerProperty} so the clock value can be bound
 * to JavaFX UI nodes.</p>
 */
public class GameClock {

    /** The current game time in seconds, exposed as a property for binding. */
    private final IntegerProperty time = new SimpleIntegerProperty(0);

    /** Nano-time of the last update call. */
    private long lastNano;

    /** Accumulated sub-second time. */
    private double accumulator;

    /** The JavaFX Text node that renders the clock. */
    private final Text clockText;

    /**
     * Creates a GameClock starting at 00:00.
     */
    public GameClock() {
        this.lastNano = 0;
        this.accumulator = 0.0;
        this.clockText = new Text(formatTime(0));
        this.clockText.setFont(Font.font("Monospace", FontWeight.BOLD, 24));
        this.clockText.setFill(Color.BLACK);
    }

    /**
     * Call every frame from {@code onUpdate(double tpf)}.
     * Uses wall-clock time so the clock is unaffected by game loop lag.
     */
    public void update() {
        long now = System.nanoTime();
        if (lastNano == 0) {
            lastNano = now;
            return;
        }
        double elapsed = (now - lastNano) / 1_000_000_000.0;
        lastNano = now;
        accumulator += elapsed;
        if (accumulator >= 1.0) {
            accumulator -= 1.0;
            time.set(time.get() + 1);
            clockText.setText(formatTime(time.get()));
        }
    }

    /** Returns the current game time in seconds. */
    public int getTime() {
        return time.get();
    }

    /** Returns the time property for JavaFX binding. */
    public IntegerProperty timeProperty() {
        return time;
    }

    /** Returns the JavaFX Text node for adding to the UI scene. */
    public Text getClockText() {
        return clockText;
    }

    /** Resets the clock to 00:00. */
    public void reset() {
        time.set(0);
        accumulator = 0.0;
        lastNano = 0;
        clockText.setText(formatTime(0));
    }

    /** Formats seconds as "MM:SS" or "HH:MM:SS". */
    private String formatTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    public String toString() {
        return "GameClock{time=" + time.get() + ", display='" + clockText.getText() + "'}";
    }
}
