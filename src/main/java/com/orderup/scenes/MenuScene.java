package com.orderup.scenes;

import com.orderup.handlers.SceneManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MenuScene extends VBox {

    public MenuScene() {
        setAlignment(Pos.CENTER);
        setSpacing(20);

        Text title = new Text("First Serve");
        title.setStyle("-fx-font-size: 36px; -fx-font-weight: bold;");

        Button playBtn = new Button("Play");
        playBtn.setPrefSize(150, 40);
        playBtn.setOnAction(e -> SceneManager.show(new WaitingLineScene()));

        Button manualBtn = new Button("Manual");
        manualBtn.setPrefSize(150, 40);
        manualBtn.setOnAction(e -> SceneManager.show(new ManualScene()));

        getChildren().addAll(title, playBtn, manualBtn);
    }
}
