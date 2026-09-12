package com.orderup.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Tracks the accumulated rhythm minigame score and combo for the
 * current game session.
 * <br><br>
 * Points are awarded per circle based on click timing accuracy:
 * a perfect hit (ring exactly on the inner circle, within the grace
 * window) is worth {@value #MAX_POINTS} base points, scaling down with
 * timing error and to zero when the ring has already closed.
 * <br><br>
 * Consecutive perfect hits build a combo multiplier
 * (×1, ×1.5, ×2, … capped at ×{@value #MAX_MULTIPLIER}); any
 * non-perfect click or a circle expiring unclicked breaks the combo.
 * There is deliberately no UI for this yet — the values are kept for
 * later display/GRASP use.
 */
public class RhythmScore {

    /** Score for a perfectly-timed click (ring == inner circle). */
    public static final int MAX_POINTS = 100;

    /** Multiplier gained per consecutive perfect hit after the first. */
    public static final double COMBO_STEP = 0.5;

    /** Cap for the combo multiplier. */
    public static final double MAX_MULTIPLIER = 3.0;

    /** Accumulated score for the current session, exposed for live UI binding. */
    private static final IntegerProperty score = new SimpleIntegerProperty(0);

    /** Current consecutive perfect-hit count, exposed for live UI binding. */
    private static final IntegerProperty combo = new SimpleIntegerProperty(0);

    /** Best consecutive perfect-hit count this session. */
    private static int bestCombo = 0;

    /** Pure model class — not meant to be instantiated. */
    private RhythmScore() {
    }

    /**
     * Adds points to the accumulated score.
     *
     * @param points points to add (may be negative or zero)
     */
    public static void add(int points) {
        score.set(score.get() + points);
    }

    /**
     * Registers a perfect hit, growing the combo.
     *
     * @return the new consecutive perfect-hit count
     */
    public static int registerPerfect() {
        int next = combo.get() + 1;
        combo.set(next);
        bestCombo = Math.max(bestCombo, next);
        return next;
    }

    /** Breaks the combo (non-perfect click or a circle expiring unclicked). */
    public static void breakCombo() {
        combo.set(0);
    }

    /**
     * Returns the combo multiplier for a given consecutive perfect-hit
     * count: ×1 for the first hit, then +{@value #COMBO_STEP} per extra
     * consecutive hit, capped at ×{@value #MAX_MULTIPLIER}.
     *
     * @param consecutivePerfects consecutive perfect-hit count (≥ 1)
     */
    public static double multiplierFor(int consecutivePerfects) {
        double multiplier = 1.0 + COMBO_STEP * (consecutivePerfects - 1);
        return Math.min(multiplier, MAX_MULTIPLIER);
    }

    /** Returns the accumulated score for the current session. */
    public static int getScore() {
        return score.get();
    }

    /** Returns the score property for JavaFX binding (same pattern as {@link GameClock}). */
    public static IntegerProperty scoreProperty() {
        return score;
    }

    /** Returns the current consecutive perfect-hit count. */
    public static int getCombo() {
        return combo.get();
    }

    /** Returns the combo property for JavaFX binding (same pattern as {@link GameClock}). */
    public static IntegerProperty comboProperty() {
        return combo;
    }

    /** Returns the best consecutive perfect-hit count this session. */
    public static int getBestCombo() {
        return bestCombo;
    }

    /**
     * Formats a multiplier without a trailing ".0" (e.g. "2" not "2.0").
     */
    public static String formatMultiplier(double multiplier) {
        return (multiplier == Math.floor(multiplier))
                ? String.valueOf((int) multiplier)
                : String.valueOf(multiplier);
    }

    /** Resets score and combo (called on new game). */
    public static void reset() {
        score.set(0);
        combo.set(0);
        bestCombo = 0;
    }
}
