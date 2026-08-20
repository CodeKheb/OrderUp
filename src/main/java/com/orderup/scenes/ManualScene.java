package com.orderup.scenes;

import com.orderup.handlers.SceneManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ManualScene extends VBox {

    public ManualScene() {
        setAlignment(Pos.CENTER);
        setSpacing(20);

        Text title = new Text("Manual Setup");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Text placeholder = new Text("Input Name, Arrival Time, Burst Time");
        placeholder.setStyle("-fx-font-size: 16px;");

        Button backBtn = new Button("Back to Menu");
        backBtn.setPrefSize(150, 40);
        backBtn.setOnAction(e -> SceneManager.show(new MenuScene()));

        getChildren().addAll(title, placeholder, backBtn);
    }
}
