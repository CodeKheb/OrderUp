package com.orderup.Scenes.Interfaces;

import java.io.IOException;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import com.orderup.Scenes.Controllers.PauseController;

import static com.almasb.fxgl.dsl.FXGL.getDialogService;
import static com.almasb.fxgl.dsl.FXGL.getGameController;

/**
 * Pause menu interface for OrderUp.
 * <br><br>
 * Extends {@link FXGLMenu} with {@link MenuType#GAME_MENU} to provide
 * an in-game pause menu. Loads the pause layout from {@code pause.fxml}
 * and connects it to the {@link PauseController}.
 */
public class PauseInterface extends FXGLMenu {

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
    }

    /**
     * Resumes the game by firing the resume action inherited from FXGLMenu.
     */
    public void resume() {
        fireResume();
    }

    /**
     * Exits the game to the main menu with a confirmation dialog.
     * <br><br>
     * Overrides the default exit behavior to show a confirmation box
     * before returning to the main menu.
     */
    public void exitToMainMenu() {
        getDialogService().showConfirmationBox(
            "Are you sure you want to return to the main menu?",
            (Boolean answer) -> {
                if (answer) {
                    getGameController().gotoMainMenu();
                }
            }
        );
    }
}
