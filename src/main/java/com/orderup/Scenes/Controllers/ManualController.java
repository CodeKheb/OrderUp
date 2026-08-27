package com.orderup.Scenes.Controllers;

import java.util.ArrayList;
import java.util.List;

import com.almasb.fxgl.dsl.FXGL;
import com.orderup.Application;
import com.orderup.Application.SceneType;
import com.orderup.Factory.ProcessGenerator;
import com.orderup.Models.CustomerProcess;
import com.orderup.Models.ProcessQueue;
import com.orderup.Scenes.Components.CustomerCard;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

/**
 * Controller for the manual scene.
 * <br><br>
 * Handles user interactions on the manual screen, including navigating
 * back to the main menu, adding customers via the {@link CustomerCard},
 * and starting a game with user-provided process values.
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

    /** The customer card — stored so onPlay can read the entered values. */
    private CustomerCard card;

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
        card = new CustomerCard();

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
        if (card.getAddedCount() == 0) {
            return;
        }
 
        ProcessGenerator gen = new ProcessGenerator();
        List<CustomerProcess> processes = new ArrayList<>();
 
        for (int i = 0; i < card.getAddedCount(); i++) {
            int at = Integer.parseInt(card.getArrivalTime(i));
            int bt = Integer.parseInt(card.getPatience(i));
            processes.add(gen.createManual(i + 1, at, bt));
        }
 
        Application.setProcessQueue(new ProcessQueue(processes));
        Application.setInitialScene(SceneType.WAITING_LINE);
        FXGL.getGameController().startNewGame();
    }
}
