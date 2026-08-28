package com.orderup.Scenes.Interfaces;

import java.util.List;

import com.almasb.fxgl.dsl.FXGL;
import com.orderup.Models.CustomerProcess;
import com.orderup.Models.GanttCell;
import com.orderup.Models.GanttChart;
import com.orderup.Scenes.Components.GanttOverlay;

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

    /**
     * Constructs the waiting line scene by spawning FXGL entities
     * for the order button, waiting line, and order list.
     */
    public WaitingLineScene() {
        FXGL.spawn("background");
        FXGL.spawn("order_button");
        FXGL.spawn("waiting_line");
        FXGL.spawn("order_list");
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
}
