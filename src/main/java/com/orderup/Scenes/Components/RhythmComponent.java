package com.orderup.Scenes.Components;

import com.almasb.fxgl.entity.component.Component;
import com.orderup.Handlers.SceneManager;
import com.orderup.Models.GameClock;

import javafx.scene.shape.Circle;

public class RhythmComponent extends Component {

    /** Real seconds per simulation tick (1 tick = 1 BT unit). */
    private static final double SECONDS_PER_TICK = 1.0;

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
}
