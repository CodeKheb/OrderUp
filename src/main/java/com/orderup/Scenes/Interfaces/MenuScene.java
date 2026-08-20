package com.orderup.Scenes.Interfaces;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MenuScene extends VBox {

    public MenuScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/menu.fxml"));
            VBox root = loader.load();
            getChildren().add(root);
            getStylesheets().add(getClass().getResource("/stylesheets/stylesheet.css").toExternalForm());
            setAlignment(javafx.geometry.Pos.CENTER);
            setSpacing(20);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
