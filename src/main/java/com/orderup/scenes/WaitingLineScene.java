package com.orderup.scenes;

import com.orderup.SceneManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class WaitingLineScene extends VBox {

    public WaitingLineScene() {
        setAlignment(Pos.CENTER);
        setSpacing(20);

        Text title = new Text("Waiting Line");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Text placeholder = new Text("Customer line goes here");
        placeholder.setStyle("-fx-font-size: 16px;");

        Button takeOrderBtn = new Button("Take Order");
        takeOrderBtn.setPrefSize(150, 40);
        takeOrderBtn.setOnAction(e -> SceneManager.show(new OrderScene()));

        getChildren().addAll(title, placeholder, takeOrderBtn);
    }
}
