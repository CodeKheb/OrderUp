package com.orderup.Scenes.Controllers;

import javafx.fxml.FXML;

import com.orderup.Handlers.SceneManager;
import com.orderup.Scenes.Interfaces.OrderScene;

/**
 * Controller for the waiting line scene.
 * <br><br>
 * Handles user interactions on the waiting line screen, including
 * navigating to the order scene when taking a customer's order.
 */
public class WaitingLineController {

    /**
     * Navigates to the order scene by showing a new {@link OrderScene}
     * via the {@link SceneManager}.
     */
    @FXML
    private void onTakeOrder() {
        SceneManager.show(new OrderScene());
    }
}
