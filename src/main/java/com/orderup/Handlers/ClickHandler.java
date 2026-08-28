package com.orderup.Handlers;

import com.orderup.Application;

/**
 * Handles click events on game entities.
 * <br><br>
 * Provides static methods that are wired to FXGL entity click handlers
 * via {@link com.orderup.Factory.WaitingLineUIFactory}.
 */
public class ClickHandler {

    /**
     * Handles the order button click.
     * Shows the Gantt chart overlay with scheduling metrics.
     */
    public static void onOrder() {
        var scene = Application.getWaitingLineScene();
        if (scene == null) return;

        var processes = Application.getOriginalProcesses();
        if (processes == null) return;

        scene.showGanttOverlay(processes);
    }
}
