package com.orderup.scenes;

import com.orderup.SceneManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class OrderScene extends VBox {

    public OrderScene() {
        setAlignment(Pos.CENTER);
        setSpacing(20);

        Text title = new Text("Order Scene");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Text placeholder = new Text("Receipt and cooking area");
        placeholder.setStyle("-fx-font-size: 16px;");

        Button backBtn = new Button("Back to Waiting Line");
        backBtn.setPrefSize(180, 40);
        backBtn.setOnAction(e -> SceneManager.show(new WaitingLineScene()));

        getChildren().addAll(title, placeholder, backBtn);
    }
}
