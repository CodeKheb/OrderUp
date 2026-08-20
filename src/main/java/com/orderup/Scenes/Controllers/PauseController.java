package com.orderup.Scenes.Controllers;

import javafx.fxml.FXML;

import com.orderup.Scenes.Interfaces.PauseInterface;

/**
 * Controller for the pause menu scene.
 * <br><br>
 * Handles user interactions on the pause menu, including resuming
 * the game and returning to the main menu. Uses the {@link PauseInterface}
 * reference for FXGL menu operations.
 */
public class PauseController {

    /** Reference to the parent pause menu interface */
    private PauseInterface menu;

    /**
     * Sets the parent pause menu interface reference.
     * <br><br>
     * Called by {@link PauseInterface} after loading the FXML to
     * establish a connection between the controller and the menu.
     *
     * @param menu the parent {@link PauseInterface} instance
     */
    public void setMenu(PauseInterface menu) {
        this.menu = menu;
    }

    /**
     * Resumes the game by calling the pause menu's resume method
     * inherited from FXGLMenu.
     */
    @FXML
    private void onResume() {
        menu.resume();
    }

    /**
     * Exits the game to the main menu with a confirmation dialog.
     * Delegates to the pause menu's {@link PauseInterface#exitToMainMenu()} method.
     */
    @FXML
    private void onMainMenu() {
        menu.exitToMainMenu();
    }
}
