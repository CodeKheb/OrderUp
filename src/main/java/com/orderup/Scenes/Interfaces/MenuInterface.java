package com.orderup.Scenes.Interfaces;

import java.io.IOException;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.orderup.Scenes.Controllers.MenuController;
import com.orderup.Uitility.ImageCache;

import javafx.scene.image.ImageView;

/**
 * Main menu interface for OrderUp.
 * <br>
 * <br>
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

        // Reuse the shared, cached GIF so the menu never decodes a second copy
        ImageView backgroundView = (ImageView) root.lookup("#backgroundView");
        if (backgroundView != null) {
            backgroundView.setImage(ImageCache.get("/assets/textures/menu_animation.gif"));
        }

        // Get the controller and pass this menu reference
        MenuController controller = loader.getController();
        controller.setMenu(this);

        // Add the FXML content to FXGL's menu content root
        getContentRoot().getChildren().add(root);

        // Cap the cursor at 64x64 — a full-resolution decoded copy is
        // wasteful for a cursor and is re-loaded on every menu creation.
        Image image = new Image(
                getClass().getResource("/assets/textures/cursor.png").toExternalForm(),
                64, 64, true, true);

        ImageCursor cursor = new ImageCursor(image);

        getContentRoot().setCursor(cursor);
        FXGL.getGameScene().getRoot().setCursor(cursor);
    }

    /**
     * Opens the manual window as a popup.
     * <br>
     * <br>
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
