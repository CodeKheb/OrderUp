package com.orderup.Scenes.Components;

import com.almasb.fxgl.entity.component.Component;
import com.orderup.Handlers.SceneManager;
import com.orderup.Models.GameClock;

import javafx.scene.shape.Circle;

public class RhythmComponent extends Component {

    /** Real seconds per simulation tick (1 tick = 1 BT unit). */
    private static final double SECONDS_PER_TICK = 1.0;

    /** Clicks within this many seconds of the ring fully closing count as a perfect hit. */
    private static final double PERFECT_GRACE_SECONDS = 1;

    /** The outer ring node whose radius is animated. */
    private final Circle outerCircle;

    /** Radius the outer ring starts at (pixels) — corresponds to full BT. */
    private final double startOuterRadius;

    /** Radius of the inner circle — the ring's final target (pixels). */
    private final double innerRadius;

    /** The customer's total patience (BT); total closing time = BT × SECONDS_PER_TICK. */
    private final int totalBurstTime;

    /** Clock time (smooth real seconds) when the customer arrived at the counter. */
    private double startTime;

    /** Clock time (smooth real seconds) when patience expires (= startTime + duration). */
    private double endTime;

    /** Set once the clock anchor times have been initialized. */
    private boolean anchored = false;

    /**
     * Creates the closing-ring animation component.
     *
     * @param outerCircle the outer ring node to animate
     * @param startOuterRadius radius the ring starts at (full BT)
     * @param innerRadius radius of the inner circle (closing target)
     * @param totalBurstTime the customer's total patience (BT)
     */
    public RhythmComponent(Circle outerCircle, double startOuterRadius,
                           double innerRadius, int totalBurstTime) {
        this.outerCircle = outerCircle;
        this.startOuterRadius = startOuterRadius;
        this.innerRadius = innerRadius;
        this.totalBurstTime = Math.max(totalBurstTime, 1);
    }

    /**
     * Anchors the animation to the game clock. Called on the first
     * {@link #onUpdate(double)} after the entity exists; the ring closes
     * over the next {@code totalBT} seconds of smooth clock time.
     */
    private void anchor() {
        GameClock clock = SceneManager.getGameClock();
        double now = (clock != null) ? clock.getSmoothSeconds() : 0.0;

        startTime = now;
        endTime = now + totalBurstTime * SECONDS_PER_TICK;
        anchored = true;
    }

    /** Recomputes the ring radius from the game clock every frame. */
    @Override
    public void onUpdate(double tpf) {
        if (!anchored) {
            anchor();
        }

        GameClock clock = SceneManager.getGameClock();
        if (clock == null) return;

        double now = clock.getSmoothSeconds();
        double duration = endTime - startTime;
        double timeLeft = endTime - now;

        if (timeLeft <= 0) {
            outerCircle.setRadius(innerRadius);
            return;
        }

        double radius = innerRadius
                + (startOuterRadius - innerRadius) * (timeLeft / duration);
        outerCircle.setRadius(radius);
    }

    /**
     * Returns the click accuracy as a fraction in [0, 1], where 1 means
     * the outer ring exactly overlaps the inner circle (perfect timing)
     * and 0 means the ring is still fully open. Clicks after the ring
     * has closed score 0.
     * <br><br>
     * A perfect-hit grace window applies: clicks within
     * {@value #PERFECT_GRACE_SECONDS}s of the ring fully closing count
     * as perfect (accuracy 1.0), on both the early and late side.
     * <br><br>
     * Accuracy is derived from the same clock-driven radius the ring is
     * animated with, so it always matches what is on screen.
     */
    public double getAccuracy() {
        if (!anchored) {
            return 0.0;
        }

        GameClock clock = SceneManager.getGameClock();
        if (clock == null) {
            return 0.0;
        }

        double duration = totalBurstTime * SECONDS_PER_TICK;
        double timeLeft = endTime - clock.getSmoothSeconds();

        // Perfect-hit grace window, on both sides of the closing moment.
        if (Math.abs(timeLeft) <= PERFECT_GRACE_SECONDS) {
            return 1.0;
        }

        // Too late — the ring has fully closed and the hit window passed.
        if (timeLeft < -PERFECT_GRACE_SECONDS) {
            return 0.0;
        }

        return 1.0 - (timeLeft / duration);
    }

    /**
     * Returns whether a click at this moment falls inside the perfect
     * grace window (i.e. {@link #getAccuracy()} returns exactly 1.0).
     */
    public boolean isPerfect() {
        return getAccuracy() >= 1.0;
    }
}
