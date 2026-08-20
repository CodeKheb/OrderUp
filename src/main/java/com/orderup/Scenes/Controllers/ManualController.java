package com.orderup.Scenes.Controllers;

import com.almasb.fxgl.dsl.FXGL;

import javafx.fxml.FXML;

/**
 * Controller for the manual scene.
 * <br><br>
 * Handles user interactions on the manual screen, including
 * navigating back to the main menu via FXGL's game controller.
 */
public class ManualController {

    /**
     * Navigates back to the main menu by calling FXGL's
     * {@code gotoMainMenu()} method through the game controller.
     */
    @FXML
    private void onBack() {
        FXGL.getGameController().gotoMainMenu();
    }
}
