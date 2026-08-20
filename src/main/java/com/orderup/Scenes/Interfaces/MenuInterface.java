package com.orderup.Scenes.Interfaces;

import java.io.IOException;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.orderup.Scenes.Controllers.MenuController;

/**
 * Main menu interface for OrderUp.
 * <br><br>
 * Extends {@link FXGLMenu} to integrate with FXGL's built-in scene system.
 * Loads the main menu layout from {@code menu.fxml} and connects it to
 * the {@link MenuController}.
 */
public class MenuInterface extends FXGLMenu {

    /**
     * Constructs the main menu by loading the FXML layout and
     * connecting the controller.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    public MenuInterface() throws IOException {
        super(MenuType.MAIN_MENU);

        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/menu.fxml"));
        Parent root = loader.load();

        // Get the controller and pass this menu reference
        MenuController controller = loader.getController();
        controller.setMenu(this);

        // Add the FXML content to FXGL's menu content root
        getContentRoot().getChildren().add(root);
    }

    /**
     * Opens the manual window as a popup.
     * <br><br>
     * Creates a new Stage to display the manual in a modal popup window,
     * similar to how ComProg handles instruction windows.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    public void openManualWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/manual.fxml"));
        Parent root = loader.load();

        Stage popupStage = new Stage();
        popupStage.setTitle("Manual");
        popupStage.setScene(new Scene(root));
        popupStage.initStyle(StageStyle.UTILITY);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.showAndWait();
    }
}
