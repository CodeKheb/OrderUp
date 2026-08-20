package com.orderup.scenes.interfaces;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class WaitingLineScene extends VBox {

    public WaitingLineScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/waitingline.fxml"));
            VBox root = loader.load();
            getChildren().setAll(root.getChildren());
            getStylesheets().add(getClass().getResource("/stylesheets/stylesheet.css").toExternalForm());
            setAlignment(javafx.geometry.Pos.CENTER);
            setSpacing(20);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
