package com.orderup.Scenes.Interfaces;

import java.io.IOException;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import com.orderup.Handlers.SceneManager;
import com.orderup.Models.GameClock;
import com.orderup.Scenes.Controllers.PauseController;
import com.orderup.Uitility.ImageCache;

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
     * The dialog uses {@code loading_background.png} as a full-screen
     * backdrop instead of a plain dim.
     */
    public void exitToMainMenu() {
        // -- Message text --
        Text message = new Text("Return to Main Menu?");
        message.getStyleClass().add("confirm-text");

        // -- Buttons --
        Button btnYes = new Button("Yes");
        Button btnNo = new Button("No");
        btnYes.getStyleClass().add("confirm-btn");
        btnNo.getStyleClass().add("confirm-btn");

        btnYes.setOnAction(e -> {
            getGameController().gotoMainMenu();
        });

        // -- Panel holding message + buttons --
        // No cream background — the text and buttons float on the image,
        // exactly like the loading screen shows it.
        VBox panel = new VBox(20, message, btnYes, btnNo);
        panel.setAlignment(Pos.CENTER);
        panel.setMaxSize(VBox.USE_PREF_SIZE, VBox.USE_PREF_SIZE);

        // -- Full-screen image backdrop wrapping the panel --
        // Built exactly like LoadingInterface: a viewport-sized overlay
        // containing the art stretched to 1440x810 and shifted -80/-45,
        // pinned to the top-left so the geometry cannot drift with the
        // parent's layout. The FXGL menu content root does not stretch
        // children, hence the explicit viewport-sized container.
        ImageView backdropView = new ImageView(
                ImageCache.get("/assets/textures/loading_background.png"));
        backdropView.setFitWidth(LoadingInterface.BG_FIT_WIDTH);
        backdropView.setFitHeight(LoadingInterface.BG_FIT_HEIGHT);
        backdropView.setPreserveRatio(false);
        backdropView.setSmooth(true);
        backdropView.setTranslateX(LoadingInterface.BG_TRANSLATE_X);
        backdropView.setTranslateY(LoadingInterface.BG_TRANSLATE_Y);
        StackPane.setAlignment(backdropView, Pos.TOP_LEFT);

        confirmOverlay = new StackPane(backdropView, panel);
        confirmOverlay.setAlignment(Pos.CENTER);
        // Pin to the exact viewport: the 1440x810 backdrop child would
        // otherwise inflate the StackPane's preferred size (children's
        // pref = their fit size), and centering inside that oversized
        // canvas shifts the panel right/down by exactly the overscan
        // offsets (+80, +45).
        confirmOverlay.setMinSize(FXGL.getAppWidth(), FXGL.getAppHeight());
        confirmOverlay.setMaxSize(FXGL.getAppWidth(), FXGL.getAppHeight());
        confirmOverlay.setPrefSize(FXGL.getAppWidth(), FXGL.getAppHeight());

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
