package com.orderup.Scenes.Interfaces;

import com.almasb.fxgl.app.scene.IntroScene;
import com.orderup.Uitility.ImageCache;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 * Custom intro scene for OrderUp.
 * <br>
 * <br>
 * Shows the OrderUp loading art immediately (same geometry as
 * {@link LoadingInterface}, no fade on the image itself), and plays a
 * title-card text sequence ("A First Come First Serve Algorithm..."
 * then "Order Up") fading in and out on top of it, styled with the
 * shared app stylesheet. Finishes with a fade to black to mask the
 * handoff to the loading screen.
 */
public class IntroInterface extends IntroScene {

    // Same geometry constants as LoadingInterface so every pre-game
    // screen frames the 720x460 art identically.
    private static final double BG_FIT_WIDTH = LoadingInterface.BG_FIT_WIDTH;
    private static final double BG_FIT_HEIGHT = LoadingInterface.BG_FIT_HEIGHT;
    private static final double BG_TRANSLATE_X = LoadingInterface.BG_TRANSLATE_X;
    private static final double BG_TRANSLATE_Y = LoadingInterface.BG_TRANSLATE_Y;

    /** Path to the shared app stylesheet, relative to the classpath root. */
    private static final String STYLESHEET_PATH = "/stylesheets/stylesheet.css";

    /** Duration of each title-card fade phase and its hold (seconds). */
    private static final double TITLE_FADE_IN_SECONDS = 0.5;
    private static final double TITLE_HOLD_SECONDS = 1.0;
    private static final double TITLE_FADE_OUT_SECONDS = 0.5;

    /** Duration of the final fade to black before handing off. */
    private static final double FADE_TO_BLACK_SECONDS = 0.6;

    /**
     * Callback fired the instant the intro sequence finishes (right as
     * {@link #finishIntro()} is called). Set this from {@code onPreInit}
     * to synchronize things like background music with the intro's
     * actual end, instead of guessing a fixed delay.
     */
    public static Runnable onIntroFinished;

    /** Text layers for the title-card sequence. */
    private final Text presentsText;
    private final Text titleText;

    /** Solid black overlay used for the final fade-to-black. */
    private final Rectangle blackout;

    /** Constructs the intro scene with the loading art shown immediately. */
    public IntroInterface() {
        int w = com.almasb.fxgl.dsl.FXGL.getAppWidth();
        int h = com.almasb.fxgl.dsl.FXGL.getAppHeight();

        getContentRoot().getStylesheets().add(
                getClass().getResource(STYLESHEET_PATH).toExternalForm());

        ImageView backgroundView = new ImageView(
                ImageCache.get("/assets/textures/loading_background.png"));
        backgroundView.setFitWidth(BG_FIT_WIDTH);
        backgroundView.setFitHeight(BG_FIT_HEIGHT);
        backgroundView.setPreserveRatio(false);
        backgroundView.setSmooth(true);
        backgroundView.setTranslateX(BG_TRANSLATE_X);
        backgroundView.setTranslateY(BG_TRANSLATE_Y);

        StackPane introLayout = new StackPane(backgroundView);
        introLayout.setAlignment(Pos.TOP_LEFT);
        introLayout.setPrefWidth(w);
        introLayout.setPrefHeight(h);

        presentsText = new Text("A First Come First Serve Algorithm...");
        presentsText.getStyleClass().add("subtitle");
        presentsText.setTextAlignment(TextAlignment.CENTER);
        presentsText.setOpacity(0.0);

        titleText = new Text("Order Up");
        titleText.getStyleClass().add("menu-title");
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setOpacity(0.0);

        StackPane titleLayout = new StackPane(presentsText, titleText);
        titleLayout.setAlignment(Pos.CENTER);
        titleLayout.setPrefWidth(w);
        titleLayout.setPrefHeight(h);

        // Full-screen black rectangle, on top of everything, invisible
        // until the very end of the sequence.
        blackout = new Rectangle(BG_FIT_WIDTH, BG_FIT_HEIGHT, Color.BLACK);
        blackout.setTranslateX(BG_TRANSLATE_X);
        blackout.setTranslateY(BG_TRANSLATE_Y);
        blackout.setOpacity(0.0);
        blackout.setMouseTransparent(true);

        getContentRoot().getChildren().addAll(introLayout, titleLayout, blackout);
    }

    /**
     * Plays the title-card sequence (presents, then game title) over
     * the already-visible loading art, fades to black, then finishes
     * the intro so FXGL proceeds to the loading screen and fires
     * {@link #onIntroFinished}, if set.
     */
    @Override
    public void startIntro() {
        FadeTransition presentsFadeIn = new FadeTransition(
                Duration.seconds(TITLE_FADE_IN_SECONDS), presentsText);
        presentsFadeIn.setFromValue(0.0);
        presentsFadeIn.setToValue(1.0);

        PauseTransition presentsHold = new PauseTransition(
                Duration.seconds(TITLE_HOLD_SECONDS));

        FadeTransition presentsFadeOut = new FadeTransition(
                Duration.seconds(TITLE_FADE_OUT_SECONDS), presentsText);
        presentsFadeOut.setFromValue(1.0);
        presentsFadeOut.setToValue(0.0);

        FadeTransition titleFadeIn = new FadeTransition(
                Duration.seconds(TITLE_FADE_IN_SECONDS), titleText);
        titleFadeIn.setFromValue(0.0);
        titleFadeIn.setToValue(1.0);

        PauseTransition titleHold = new PauseTransition(
                Duration.seconds(TITLE_HOLD_SECONDS));

        FadeTransition titleFadeOut = new FadeTransition(
                Duration.seconds(TITLE_FADE_OUT_SECONDS), titleText);
        titleFadeOut.setFromValue(1.0);
        titleFadeOut.setToValue(0.0);

        FadeTransition fadeToBlack = new FadeTransition(
                Duration.seconds(FADE_TO_BLACK_SECONDS), blackout);
        fadeToBlack.setFromValue(0.0);
        fadeToBlack.setToValue(1.0);
        fadeToBlack.setOnFinished(e -> {
            finishIntro();
            if (onIntroFinished != null) {
                onIntroFinished.run();
            }
        });

        new SequentialTransition(
                presentsFadeIn, presentsHold, presentsFadeOut,
                titleFadeIn, titleHold, titleFadeOut,
                fadeToBlack).play();
    }
}
