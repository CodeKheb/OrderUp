package com.orderup.Scenes.Interfaces;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

/**
 * In-game scene representing the manual/instructions screen.
 * <br><br>
 * Extends {@link VBox} and loads its layout from {@code manual.fxml}.
 * This scene is shown via {@link com.orderup.Handlers.SceneManager} during
 * gameplay.
 */
public class ManualScene extends VBox {

    /**
     * Constructs the manual scene by loading the FXML layout,
     * applying the stylesheet, and configuring alignment and spacing.
     */
    public ManualScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/manual.fxml"));
            Parent root = loader.load();
            getChildren().add(root);
            setAlignment(javafx.geometry.Pos.CENTER);
            setSpacing(20);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
