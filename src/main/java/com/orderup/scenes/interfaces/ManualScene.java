package com.orderup.scenes.interfaces;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ManualScene extends VBox {

    public ManualScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/manual.fxml"));
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
