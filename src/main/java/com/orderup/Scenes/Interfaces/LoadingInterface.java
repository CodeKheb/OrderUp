package com.orderup.Scenes.Interfaces;

import com.almasb.fxgl.app.scene.LoadingScene;
import com.almasb.fxgl.dsl.FXGL;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Loading scene for OrderUp.
 * <br><br>
 * Extends {@link LoadingScene} to display a simple loading screen
 * while the game initializes. Uses FXGL's {@link FXGL} for getting
 * application dimensions.
 */
public class LoadingInterface extends LoadingScene {

    /**
     * Constructs the loading scene with a centered "Loading..." text
     * and a solid background color.
     */
    public LoadingInterface() {
        int w = FXGL.getAppWidth();
        int h = FXGL.getAppHeight();

        // Create loading text
        Text loadingText = new Text("Loading...");
        loadingText.setFill(Color.LIGHTGRAY);
        loadingText.setFont(Font.font(72));

        // Create layout container
        VBox loadingLayout = new VBox(loadingText);
        loadingLayout.setAlignment(Pos.CENTER);
        loadingLayout.setPrefWidth(w);
        loadingLayout.setPrefHeight(h);
        loadingLayout.setStyle("-fx-background-color: #1a1a2e;");

        getContentRoot().getChildren().add(loadingLayout);
    }
}
