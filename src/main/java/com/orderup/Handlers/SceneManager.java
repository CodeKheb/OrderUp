package com.orderup.Handlers;

import com.almasb.fxgl.dsl.FXGL;
import com.orderup.Models.GameClock;
import com.orderup.Models.RhythmScore;

import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Utility class for managing scene transitions within the game.
 * <br><br>
 * Provides static methods to show UI nodes in the game scene
 * using FXGL's game scene management system.
 */
public class SceneManager {

    /** The game clock to display as a UI overlay. */
    private static GameClock gameClock;

    /**
     * Sets the GameClock to display on every scene.
     * Call once from Application.initGame() before showing scenes.
     *
     * @param clock the game clock instance
     */
    public static void setGameClock(GameClock clock) {
        gameClock = clock;
    }

    /**
     * Returns the GameClock instance, or null if not set yet.
     *
     * @return the game clock instance
     */
    public static GameClock getGameClock() {
        return gameClock;
    }

    /**
     * Shows a given node in the game scene by clearing existing UI nodes
     * and adding the new one. Also adds the clock UI if a GameClock is set.
     *
     * @param view the JavaFX {@link Node} to display
     */
    public static void show(Node view) {
        FXGL.getGameScene().clearUINodes();
        FXGL.getGameScene().addUINode(view);
    }

    /**
     * Builds and adds the clock text with a styled background rectangle.
     */
    public static void showClockUI() {
        if (gameClock == null) return;

        Text clockText = gameClock.getClockText();
        clockText.setFill(Color.WHITE);

        Rectangle clockBg = new Rectangle(260, 70);
        clockBg.setArcWidth(10);
        clockBg.setArcHeight(10);
        clockBg.setFill(Color.web("#1a1a1a"));
        clockBg.setStroke(Color.web("#cc5114"));
        clockBg.setStrokeWidth(2);
        clockBg.setStrokeType(StrokeType.INSIDE);

        StackPane clockPane = new StackPane(clockBg, clockText);
        clockPane.setTranslateX(1280 / 2.2);
        clockPane.setTranslateY(720 / 20);

        FXGL.getGameScene().addUINode(clockPane);
    }

    /**
     * Builds and adds the score text with a styled background rectangle
     */
    public static void showScoreUI() {
        Text scoreText = new Text();
        scoreText.textProperty().bind(
                Bindings.createStringBinding(
                        () -> "Score " + RhythmScore.getScore(),
                        RhythmScore.scoreProperty()));
        scoreText.setFont(Font.font("Monospace", FontWeight.BOLD, 30));
        scoreText.setFill(Color.WHITE);

        Rectangle scoreBg = new Rectangle(200, 70);
        scoreBg.setArcWidth(10);
        scoreBg.setArcHeight(10);
        scoreBg.setFill(Color.web("#1a1a1a"));
        scoreBg.setStroke(Color.web("#cc5114"));
        scoreBg.setStrokeWidth(2);
        scoreBg.setStrokeType(StrokeType.INSIDE);

        StackPane scorePane = new StackPane(scoreBg, scoreText);

        // -- Combo badge ------------------------------------------------
        Text comboText = new Text();
        comboText.textProperty().bind(
                Bindings.createStringBinding(
                        () -> "×" + RhythmScore.formatMultiplier(
                                RhythmScore.multiplierFor(RhythmScore.getCombo())),
                        RhythmScore.comboProperty()));
        comboText.setFont(Font.font("Monospace", FontWeight.BOLD, 26));
        comboText.setFill(Color.web("#D9A45B"));

        Rectangle comboBg = new Rectangle(70, 50);
        comboBg.setArcWidth(10);
        comboBg.setArcHeight(10);
        comboBg.setFill(Color.web("#1a1a1a"));
        comboBg.setStroke(Color.web("#D9A45B"));
        comboBg.setStrokeWidth(2);
        comboBg.setStrokeType(StrokeType.INSIDE);

        StackPane comboBadge = new StackPane(comboBg, comboText);

        // Hide the badge when there is no combo.
        comboBadge.visibleProperty().bind(RhythmScore.comboProperty().greaterThanOrEqualTo(2));
        comboBadge.opacityProperty().bind(
                Bindings.when(comboBadge.visibleProperty()).then(1.0).otherwise(0.0));

        // -- Layout: badge left of the score panel -----------------------
        HBox scoreBar = new HBox(10);
        scoreBar.getChildren().addAll(comboBadge, scorePane);
        scoreBar.setTranslateX(960);
        scoreBar.setTranslateY(720 / 20);
        HBox.setMargin(comboBadge, new Insets(0, 0, 0, 0));

        FXGL.getGameScene().addUINode(scoreBar);
    }
}
