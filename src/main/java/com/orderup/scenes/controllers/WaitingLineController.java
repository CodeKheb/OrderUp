package com.orderup.scenes.controllers;

import com.orderup.handlers.SceneManager;
import com.orderup.scenes.interfaces.OrderScene;
import javafx.fxml.FXML;

public class WaitingLineController {

    @FXML
    private void onTakeOrder() {
        SceneManager.show(new OrderScene());
    }
}
