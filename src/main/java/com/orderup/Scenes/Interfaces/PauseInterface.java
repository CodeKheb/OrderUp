package com.orderup.Scenes.Interfaces;

import java.io.IOException;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import com.orderup.Handlers.SceneManager;
import com.orderup.Models.GameClock;
import com.orderup.Scenes.Controllers.PauseController;

import static com.almasb.fxgl.dsl.FXGL.getGameController;

/**
 * Pause menu interface for OrderUp.
 * <br><br>
 * Extends {@link FXGLMenu} with {@link MenuType#GAME_MENU} to provide
 * an in-game pause menu. Loads the pause layout from {@code pause.fxml}
 * and connects it to the {@link PauseController}.
 */
public class PauseInterface extends FXGLMenu {

    /** Confirmation overlay shown over the pause menu */
    private StackPane confirmOverlay;

    /**
     * Constructs the pause menu by loading the FXML layout and
     * connecting the controller.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    public PauseInterface() throws IOException {
        super(MenuType.GAME_MENU);

        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/pause.fxml"));
        Parent root = loader.load();

        // Get the controller and pass this menu reference
        PauseController controller = loader.getController();
        controller.setMenu(this);

        // Add the FXML content to FXGL's menu content root
        getContentRoot().getChildren().add(root);

        // Hide the overlay if pause menu closes
        getContentRoot().sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene == null) {
                hideConfirmOverlay();
            }
        });
    }

    /**
     * Resumes the game by firing the resume action inherited from FXGLMenu.
     */
    public void resume() {
        fireResume();
    }

    /**
     * Called every time the pause menu opens (ESC). Pauses the game clock
     * so time does not advance in the background.
     */
    @Override
    public void onEnteredFrom(com.almasb.fxgl.scene.Scene prev) {
        super.onEnteredFrom(prev);
        GameClock clock = SceneManager.getGameClock();
        if (clock != null) {
            clock.pause();
        }
    }

    /**
     * Called every time the pause menu closes (ESC again or Resume).
     * Resumes the game clock without a time jump.
     */
    @Override
    public void onExitingTo(com.almasb.fxgl.scene.Scene next) {
        super.onExitingTo(next);
        GameClock clock = SceneManager.getGameClock();
        if (clock != null) {
            clock.resume();
        }
    }

    /**
     * Exits the game to the main menu with a confirmation dialog.
     */
    public void exitToMainMenu() {
        // -- Message text --
        Text message = new Text("Are you sure you want to return to the main menu?");
        message.getStyleClass().add("confirm-text");

        // -- Buttons --
        Button btnYes = new Button("Yes");
        Button btnNo = new Button("No");
        btnYes.getStyleClass().add("confirm-btn");
        btnNo.getStyleClass().add("confirm-btn");

        btnYes.setOnAction(e -> getGameController().gotoMainMenu());

        // -- Panel holding message + buttons --
        VBox panel = new VBox(20, message, btnYes, btnNo);
        panel.getStyleClass().add("confirm-panel");

        // -- Full-screen dim overlay wrapping the panel --
        confirmOverlay = new StackPane(panel);
        confirmOverlay.getStyleClass().add("confirm-overlay");
        confirmOverlay.setAlignment(Pos.CENTER);

        confirmOverlay.getStylesheets().add(
            getClass().getResource("/stylesheets/stylesheet.css").toExternalForm()
        );

        btnNo.setOnAction(e -> hideConfirmOverlay());

        getContentRoot().getChildren().add(confirmOverlay);
    }

    /** Removes the confirmation overlay from the pause menu, if shown. */
    private void hideConfirmOverlay() {
        if (confirmOverlay != null) {
            getContentRoot().getChildren().remove(confirmOverlay);
            confirmOverlay = null;
        }
    }
}
