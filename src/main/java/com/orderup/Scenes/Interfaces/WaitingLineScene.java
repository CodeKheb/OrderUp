package com.orderup.Scenes.Interfaces;

import com.almasb.fxgl.dsl.FXGL;

import javafx.scene.layout.Pane;

/**
 * In-game scene representing the waiting line screen.
 * <br><br>
 * Uses FXGL entities for the game layer so interactive elements
 * can receive clicks without being blocked by UI overlays.
 */
public class WaitingLineScene extends Pane {

    /**
     * Constructs the waiting line scene by spawning FXGL entities
     * for the order button, waiting line, and order list.
     */
    public WaitingLineScene() {
        FXGL.spawn("order_button");
        FXGL.spawn("waiting_line");
        FXGL.spawn("order_list");

    }
}
