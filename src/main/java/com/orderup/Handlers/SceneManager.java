package com.orderup.Handlers;

import com.almasb.fxgl.dsl.FXGL;
import com.orderup.Models.GameClock;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
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
}
