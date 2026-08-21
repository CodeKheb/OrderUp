package com.orderup.Scenes.Controllers;

import javafx.fxml.FXML;

import com.orderup.Handlers.ClickHandler;
import com.orderup.Handlers.SceneManager;
import com.orderup.Scenes.Interfaces.WaitingLineScene;

/**
 * Controller for the order scene.
 * <br><br>
 * Handles user interactions on the order screen, including
 * navigating back to the waiting line scene.
 */
public class OrderController {

    /**
     * Navigates back to the waiting line scene by showing
     * a new {@link WaitingLineScene} via the {@link SceneManager}.
     */
    @FXML
    private void onBack() {
        ClickHandler.onWaitingLine();
    }
}
