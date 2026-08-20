package com.orderup.scenes.interfaces;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class OrderScene extends VBox {

    public OrderScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/order.fxml"));
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
