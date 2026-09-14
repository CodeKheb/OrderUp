package com.orderup.Scenes.Interfaces;

import com.almasb.fxgl.app.scene.LoadingScene;
import com.almasb.fxgl.dsl.FXGL;
import com.orderup.Uitility.ImageCache;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Loading scene for OrderUp.
 * <br><br>
 * Extends {@link LoadingScene} to display a static background image with
 * a "Loading..." text while the game initializes. Uses a static PNG
 * (not the menu's animated gif) so that gif only ever starts playing
 * once, exactly when {@link MenuInterface} is constructed.
 */
public class LoadingInterface extends LoadingScene {

    // Background geometry copied from menu.fxml / manual.fxml so the
    // loading screen renders the 720x460 art exactly where those scenes
    // render menu_animation.gif: stretched to 1440x810 and shifted
    // -80/-45 (overscan cover). Shared with the pause-menu confirm
    // overlay so both screens frame the art identically.
    public static final double BG_FIT_WIDTH = 1440;
    public static final double BG_FIT_HEIGHT = 810;
    public static final double BG_TRANSLATE_X = -80;
    public static final double BG_TRANSLATE_Y = -45;

    /**
     * Constructs the loading scene with a centered "Loading..." text
     * over a static background image.
     */
    public LoadingInterface() {
        int w = FXGL.getAppWidth();
        int h = FXGL.getAppHeight();

        ImageView backgroundView = new ImageView(
                ImageCache.get("/assets/textures/loading_background.png"));
        backgroundView.setFitWidth(BG_FIT_WIDTH);
        backgroundView.setFitHeight(BG_FIT_HEIGHT);
        backgroundView.setPreserveRatio(false);
        backgroundView.setSmooth(true);
        backgroundView.setTranslateX(BG_TRANSLATE_X);
        backgroundView.setTranslateY(BG_TRANSLATE_Y);

        Text loadingText = new Text("Loading...");
        loadingText.setFill(Color.WHITE);
        loadingText.setFont(Font.font(72));

        StackPane loadingLayout = new StackPane(backgroundView, loadingText);
        StackPane.setAlignment(backgroundView, Pos.TOP_LEFT);
        loadingLayout.setAlignment(Pos.CENTER);
        loadingLayout.setPrefWidth(w);
        loadingLayout.setPrefHeight(h);
        loadingLayout.setStyle("-fx-background-color: #dd9223;");

        getContentRoot().getChildren().add(loadingLayout);
    }
}
