package com.orderup.Scenes.Interfaces;

import com.almasb.fxgl.app.scene.StartupScene;
import com.orderup.Uitility.ImageCache;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * Custom startup scene for OrderUp.
 * <br><br>
 * Replaces FXGL's default {@code FXGLStartupScene} (the blue engine
 * splash shown while the engine boots) with the OrderUp loading art,
 * using the same geometry as {@link LoadingInterface}. No animation —
 * it is displayed statically until the engine hands off to the intro.
 */
public class StartupInterface extends StartupScene {

    // Same geometry constants as LoadingInterface so every pre-game
    // screen frames the 720x460 art identically.
    private static final double BG_FIT_WIDTH = LoadingInterface.BG_FIT_WIDTH;
    private static final double BG_FIT_HEIGHT = LoadingInterface.BG_FIT_HEIGHT;
    private static final double BG_TRANSLATE_X = LoadingInterface.BG_TRANSLATE_X;
    private static final double BG_TRANSLATE_Y = LoadingInterface.BG_TRANSLATE_Y;

    /**
     * Constructs the static startup scene with the loading art.
     *
     * @param w app width in pixels
     * @param h app height in pixels
     */
    public StartupInterface(int w, int h) {
        super(w, h);

        ImageView backgroundView = new ImageView(
                ImageCache.get("/assets/textures/loading_background.png"));
        backgroundView.setFitWidth(BG_FIT_WIDTH);
        backgroundView.setFitHeight(BG_FIT_HEIGHT);
        backgroundView.setPreserveRatio(false);
        backgroundView.setSmooth(true);
        backgroundView.setTranslateX(BG_TRANSLATE_X);
        backgroundView.setTranslateY(BG_TRANSLATE_Y);

        StackPane layout = new StackPane(backgroundView);
        layout.setAlignment(Pos.TOP_LEFT);
        layout.setPrefWidth(w);
        layout.setPrefHeight(h);
        layout.setStyle("-fx-background-color: #dd9223;");

        getContentRoot().getChildren().add(layout);
    }
}
