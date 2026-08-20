package com.orderup.Handlers;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.Node;

/**
 * Utility class for managing scene transitions within the game.
 * <br><br>
 * Provides static methods to show UI nodes in the game scene
 * using FXGL's game scene management system.
 */
public class SceneManager {

    /**
     * Shows a given node in the game scene by clearing existing UI nodes
     * and adding the new one.
     *
     * @param view the JavaFX {@link Node} to display
     */
    public static void show(Node view) {
        FXGL.getGameScene().clearUINodes();
        FXGL.getGameScene().addUINode(view);
    }
}
