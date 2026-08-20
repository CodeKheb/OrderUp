package com.orderup.Scenes.Interfaces;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

/**
 * In-game scene representing the waiting line screen.
 * <br><br>
 * Extends {@link VBox} and loads its layout from {@code waitingline.fxml}.
 * This scene is shown via {@link com.orderup.Handlers.SceneManager} during
 * gameplay.
 */
public class WaitingLineScene extends VBox {

    /**
     * Constructs the waiting line scene by loading the FXML layout,
     * applying the stylesheet, and configuring alignment and spacing.
     */
    public WaitingLineScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/waitingline.fxml"));
            Parent root = loader.load();
            getChildren().add(root);
            setAlignment(javafx.geometry.Pos.CENTER);
            setSpacing(20);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
