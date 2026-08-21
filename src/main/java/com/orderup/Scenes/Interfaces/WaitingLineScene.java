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

    public WaitingLineScene() {
        FXGL.spawn("order_button");
        FXGL.spawn("waiting_line");
    }
}
