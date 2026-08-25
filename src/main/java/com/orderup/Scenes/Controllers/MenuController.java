package com.orderup.Scenes.Controllers;

import java.util.List;

import com.almasb.fxgl.dsl.FXGL;
import com.orderup.Application;
import com.orderup.Application.SceneType;
import com.orderup.Factory.ProcessGenerator;
import com.orderup.Models.CustomerProcess;
import com.orderup.Models.ProcessQueue;
import com.orderup.Scenes.Interfaces.MenuInterface;

import javafx.fxml.FXML;

/**
 * Controller for the main menu scene.
 * <br><br>
 * Handles user interactions on the main menu, including starting
 * the game, viewing the manual, and quitting. Uses the {@link MenuInterface}
 * reference for FXGL menu operations.
 */
public class MenuController {

    /** Reference to the parent menu interface */
    private MenuInterface menu;

    /**
     * Sets the parent menu interface reference.
     * <br><br>
     * Called by {@link MenuInterface} after loading the FXML to
     * establish a connection between the controller and the menu.
     *
     * @param menu the parent {@link MenuInterface} instance
     */
    public void setMenu(MenuInterface menu) {
        this.menu = menu;
    }

    /**
     * Starts the game and shows the waiting line scene.
     */
    @FXML
    private void onPlay() {
        ProcessGenerator gen = new ProcessGenerator();
        List<CustomerProcess> processes = gen.createRandom();

        Application.setProcessQueue(new ProcessQueue(processes));

        Application.setInitialScene(SceneType.WAITING_LINE);
        FXGL.getGameController().startNewGame();
    }

    /**
     * Starts the game and shows the manual scene.
     */
    @FXML
    private void onManual() {
        Application.setInitialScene(SceneType.MANUAL);
        FXGL.getGameController().startNewGame();
    }

    /**
     * Exits the game application.
     */
    @FXML
    private void onQuit() {
        FXGL.getGameController().exit();
    }
}
