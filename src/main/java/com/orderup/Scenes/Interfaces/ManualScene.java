package com.orderup.Scenes.Interfaces;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

/**
 * In-game scene representing the manual/instructions screen.
 * <br><br>
 * Extends {@link VBox} and loads its layout from {@code manual.fxml}.
 * This scene is shown via {@link com.orderup.Handlers.SceneManager} during
 * gameplay.
 */
public class ManualScene extends VBox {

    private final EventHandler<KeyEvent> escBlocker = e -> {
        if (e.getCode() == KeyCode.ESCAPE) {
            e.consume();
        }
    };

    /**
     * Constructs the manual scene by loading the FXML layout
     * and configuring alignment and spacing.
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

        sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(KeyEvent.KEY_PRESSED, escBlocker);
            } else if (oldScene != null) {
                oldScene.removeEventFilter(KeyEvent.KEY_PRESSED, escBlocker);
            }
        });
    }
}
