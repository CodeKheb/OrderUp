package com.orderup.Scenes.Controllers;

import com.almasb.fxgl.dsl.FXGL;
import com.orderup.Application;
import com.orderup.Application.SceneType;
import com.orderup.Scenes.Components.CustomerCard;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

/**
 * Controller for the manual scene.
 * <br><br>
 * Handles user interactions on the manual screen, including
 * navigating back to the main menu via FXGL's game controller.
 */
public class ManualController {

    /**
     * The VBox in manual.fxml where the customer card gets placed.
     * <br><br>
     * {@code @FXML} tells JavaFX: "find the element with this name
     * in the .fxml file and assign it to this variable." The name
     * must match the {@code fx:id} in the FXML exactly.
     */
    @FXML
    private VBox cardContainer;

    /**
     * Called automatically by JavaFX after the FXML is loaded.
     * <br><br>
     * This is where we create the customer card and drop it into
     * the scene. Think of it as the "setup" step — it runs once
     * when the screen first appears.
     */
    @FXML
    public void initialize() {
        // Create the card (header + stickman + inputs + navigation)
        CustomerCard card = new CustomerCard();

        // Add it to the VBox placeholder defined in manual.fxml
        cardContainer.getChildren().add(card);
    }

    /**
     * Navigates back to the main menu by calling FXGL's
     * {@code gotoMainMenu()} method through the game controller.
     */
    @FXML
    private void onBack() {
        FXGL.getGameController().gotoMainMenu();
    }

    /**
     * Starts the game and shows the waiting line scene.
     */
    @FXML
    private void onPlay() {
        Application.setInitialScene(SceneType.WAITING_LINE);
        FXGL.getGameController().startNewGame();
    }
}
