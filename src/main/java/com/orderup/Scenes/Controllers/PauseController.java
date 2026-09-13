package com.orderup.Scenes.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;

import com.orderup.Application;
import com.orderup.Handlers.AudioManager;
import com.orderup.Scenes.Interfaces.PauseInterface;

/**
 * Controller for the pause menu scene.
 * <br><br>
 * Handles user interactions on the pause menu, including resuming
 * the game and returning to the main menu. Uses the {@link PauseInterface}
 * reference for FXGL menu operations.
 */
public class PauseController {

    @FXML
    private Slider musicVolumeSlider;

    @FXML
    private Slider soundVolumeSlider;

    /** Reference to the parent pause menu interface */
    private PauseInterface menu;

    @FXML
    private void initialize() {
        musicVolumeSlider.setValue(AudioManager.getMusicVolume());
        soundVolumeSlider.setValue(AudioManager.getAudioVolume());

        musicVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            AudioManager.setMusicVolume(newVal.doubleValue());
        });

        soundVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            AudioManager.setGlobalSoundVolume(newVal.doubleValue());
        });
    }

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
     * Resumes the game and then shows the Gantt chart overlay over the
     * running game scene. The overlay request is deferred to the next
     * game frame because the game scene only becomes visible after the
     * pause menu closes.
     */
    @FXML
    private void onShowGanttChart() {
        Application.requestGanttChart();
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
