package com.orderup.Scenes.Interfaces;

import com.almasb.fxgl.dsl.FXGL;

import javafx.scene.layout.VBox;

/**
 * In-game scene representing the order screen.
 * <br><br>
 * Extends {@link VBox} and loads its layout from {@code order.fxml}.
 * This scene is shown via {@link com.orderup.Handlers.SceneManager} during
 * gameplay.
 */
public class OrderScene extends VBox {

    /**
     * Constructs the order scene by loading the FXML layout,
     * configuring alignment and spacing, and spawning counter and stove
     * entities via FXGL.
     */
    public OrderScene() {
        FXGL.spawn("counter");
        FXGL.spawn("stove");
        FXGL.spawn("back_button");
    }
}
