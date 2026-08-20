package com.orderup.scenes.controllers;

import com.orderup.handlers.SceneManager;
import com.orderup.scenes.interfaces.WaitingLineScene;
import javafx.fxml.FXML;

public class OrderController {

    @FXML
    private void onBack() {
        SceneManager.show(new WaitingLineScene());
    }
}
