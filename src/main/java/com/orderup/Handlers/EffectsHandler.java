package com.orderup.Handlers;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * Spawns standalone one-shot visual effects that play at a position and
 * remove themselves from the game world when finished.
 */
public class EffectsHandler {

    /** How long the burst effect plays (seconds). */
    private static final double BURST_DURATION = 0.35;

    /** Stroke width of the burst ring (pixels). */
    private static final double BURST_STROKE_WIDTH = 6;

    /** How much larger than the start radius the burst ring grows (pixels). */
    private static final double BURST_GROW = 40;

    /** Duration of each fade phase of the expire effect (seconds). */
    private static final double FADE_IN_DURATION = 0.15;

    /**
     * Spawns a quick expanding, fading ring at the given position —
     * played when a rhythm circle disappears.
     * <br><br>
     * The ring scales up from the circle's current outer radius while
     * fading out, then removes its entity from the game world.
     *
     * @param position center of the effect (the circle's position)
     * @param startRadius radius the burst ring starts at, usually the
     *                    disappearing circle's current outer radius
     */
    public static void spawnBurst(Point2D position, double startRadius) {
        double start = Math.max(startRadius, 1);

        Circle ring = new Circle(start);
        ring.setFill(null);
        ring.setStroke(Color.web("#D9A45B"));
        ring.setStrokeWidth(BURST_STROKE_WIDTH);

        Entity effect = FXGL.entityBuilder()
                .at(position)
                .view(ring)
                .zIndex(55)
                .build();
        FXGL.getGameWorld().addEntity(effect);

        // Grow the ring outward (scale is relative to the start radius).
        ScaleTransition grow = new ScaleTransition(Duration.seconds(BURST_DURATION), ring);
        grow.setToX((start + BURST_GROW) / start);
        grow.setToY((start + BURST_GROW) / start);

        FadeTransition fade = new FadeTransition(Duration.seconds(BURST_DURATION), ring);
        fade.setToValue(0.0);

        PauseTransition lifetime = new PauseTransition(Duration.seconds(BURST_DURATION));
        lifetime.setOnFinished(e -> effect.removeFromWorld());

        new ParallelTransition(grow, fade, lifetime).play();
    }

    /**
     * Spawns a red ring that fades in over the spot where a rhythm
     * circle expired unclicked (patience ran out) — a visual "order
     * failed" cue, distinct from the gold burst of a player click.
     * <br><br>
     * After fading in, the ring holds briefly and fades out, then its
     * entity removes itself from the game world.
     *
     * @param position center of the effect (the expired circle's position)
     * @param radius   ring radius — usually the expired circle's current
     *                 outer radius
     */
    public static void spawnExpireFade(Point2D position, double radius) {
        double r = Math.max(radius, 1);

        Circle ring = new Circle(r);
        ring.setFill(null);
        ring.setStroke(Color.web("#FF3B30"));
        ring.setStrokeWidth(6);
        ring.setOpacity(0.0);

        Entity effect = FXGL.entityBuilder()
                .at(position)
                .view(ring)
                .zIndex(55)
                .build();
        FXGL.getGameWorld().addEntity(effect);

        // Phase 1: fade the red ring in over the dying circle.
        FadeTransition fadeIn = new FadeTransition(
                Duration.seconds(FADE_IN_DURATION), ring);
        fadeIn.setToValue(1.0);

        // Phase 2: hold briefly at full red, then fade out.
        PauseTransition hold = new PauseTransition(
                Duration.seconds(FADE_IN_DURATION * 2));
        hold.setOnFinished(e -> {
            FadeTransition fadeOut = new FadeTransition(
                    Duration.seconds(FADE_IN_DURATION), ring);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(done -> effect.removeFromWorld());
            fadeOut.play();
        });

        fadeIn.play();
        hold.play();
    }
}
