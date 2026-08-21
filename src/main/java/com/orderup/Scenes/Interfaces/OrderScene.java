package com.orderup.Scenes.Interfaces;

import java.io.IOException;

import com.almasb.fxgl.dsl.FXGL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
     * applying the stylesheet, and configuring alignment and spacing.
     */
    public OrderScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/order.fxml"));
            Parent root = loader.load();
            getChildren().add(root);
            setAlignment(javafx.geometry.Pos.CENTER);
            setSpacing(20);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FXGL.spawn("counter");
        FXGL.spawn("stove");
    }
}
