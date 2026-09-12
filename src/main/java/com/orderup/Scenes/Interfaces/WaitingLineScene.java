package com.orderup.Scenes.Interfaces;

import java.util.List;

import com.almasb.fxgl.dsl.FXGL;
import com.orderup.Handlers.AudioManager;
import com.orderup.Models.CustomerProcess;
import com.orderup.Models.GanttCell;
import com.orderup.Models.GanttChart;
import com.orderup.Scenes.Components.GanttOverlay;

import com.almasb.fxgl.entity.Entity;
import com.orderup.Models.MenuItem;

import javafx.scene.layout.Pane;

/**
 * In-game scene representing the waiting line screen.
 * <br><br>
 * Uses FXGL entities for the game layer so interactive elements
 * can receive clicks without being blocked by UI overlays.
 */
public class WaitingLineScene extends Pane {

    /** The Gantt chart overlay, hidden by default. */
    private GanttOverlay ganttOverlay;

    /** The currently active rhythm pair entity, or null if none. */
    private Entity rhythmPair;

    /** Spawn zone for rhythm circles. */
    private static final int CIRCLE_ZONE_MIN_X = 1000;

    /** Spawn zone for rhythm circles. */
    private static final int CIRCLE_ZONE_MAX_X = 1280;

    /** Spawn zone for rhythm circles. */
    private static final int CIRCLE_ZONE_MIN_Y = 100;

    /** Spawn zone for rhythm circles. */
    private static final int CIRCLE_ZONE_MAX_Y = 600;

    /** Largest possible outer ring radius, so spawns never clip the zone edges. */
    private static final double CIRCLE_SPAWN_MARGIN = 150;

    /**
     * Constructs the waiting line scene by spawning FXGL entities
     * for the order button, waiting line, and order list.
     */
    public WaitingLineScene() {
        FXGL.spawn("background");
        FXGL.spawn("waiting_line");
    }

    /**
     * Shows the Gantt chart overlay with the given processes.
     * Generates the chart from the original process list and displays
     * the visual bar chart with scheduling metrics.
     *
     * @param processes the original customer processes (with full BT)
     */
    public void showGanttOverlay(List<CustomerProcess> processes) {
        GanttChart chart = new GanttChart();
        List<GanttCell> cells = chart.generateGanttChart(processes);

        ganttOverlay = new GanttOverlay(cells, processes, this::hideGanttOverlay);
        FXGL.getGameScene().addUINode(ganttOverlay);
    }

    /** Hides the Gantt chart overlay if it is currently showing. */
    public void hideGanttOverlay() {
        if (ganttOverlay != null) {
            FXGL.getGameScene().removeUINode(ganttOverlay);
            ganttOverlay = null;
        }
    }

    /** Returns whether the Gantt overlay is currently visible. */
    public boolean isGanttOverlayVisible() {
        return ganttOverlay != null;
    }

    /**
     * Spawns a rhythm pair (inner circle + closing outer ring) at a random
     * position within the spawn zone, sized to the given patience (BT).
     * <br><br>
     * Only one pair exists at a time (front customer only) — any previous
     * pair is removed first.
     *
     * @param burstTime the front customer's patience (BT)
     * @param order     the dish this customer ordered (drives the food icon
     *                  on the circle), or null for no icon
     */
    public void spawnRhythmCircle(int burstTime, MenuItem order) {
        removeRhythmCircle();

        double x = CIRCLE_ZONE_MIN_X + CIRCLE_SPAWN_MARGIN + Math.random()
                * (CIRCLE_ZONE_MAX_X - CIRCLE_ZONE_MIN_X - 2 * CIRCLE_SPAWN_MARGIN);
        double y = CIRCLE_ZONE_MIN_Y + CIRCLE_SPAWN_MARGIN + Math.random()
                * (CIRCLE_ZONE_MAX_Y - CIRCLE_ZONE_MIN_Y - 2 * CIRCLE_SPAWN_MARGIN);

        var data = new com.almasb.fxgl.entity.SpawnData(x, y);
        data.put("burstTime", burstTime);
        data.put("order", order);

        rhythmPair = FXGL.spawn("rhythm_pair", data);
    }

    /**
     * Removes the active rhythm pair entity.
     *
     * @return true if an active (i.e. never clicked) circle was removed,
     *         false if there was no circle or it was already clicked away
     */
    public boolean removeRhythmCircle() {
        if (rhythmPair != null) {
            boolean wasActive = rhythmPair.isActive();
            if (wasActive) {
                // Expired unclicked (patience/BT ran out) — red fade-in
                // cue as a standalone effect entity (the pair is removed
                // below). Clicked circles instead get the gold burst in
                // ClickHandler.
                AudioManager.missed();
                com.orderup.Handlers.EffectsHandler.spawnExpireFade(rhythmPair.getPosition(),
                        rhythmPair.getComponent(
                                com.orderup.Scenes.Components.RhythmComponent.class)
                                .getCurrentOuterRadius());
                rhythmPair.removeFromWorld();
            }
            rhythmPair = null;
            return wasActive;
        }
        return false;
    }


    /** Returns true if no rhythm pair is currently active. */
    public boolean isRhythmDone() {
        return rhythmPair == null || !rhythmPair.isActive();
    }
}
