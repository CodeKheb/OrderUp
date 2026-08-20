package com.orderup.Scenes.Controllers;

import com.orderup.Handlers.SceneManager;
import com.orderup.Scenes.Interfaces.WaitingLineScene;

import javafx.fxml.FXML;

public class OrderController {

    @FXML
    private void onBack() {
        SceneManager.show(new WaitingLineScene());
    }
}
