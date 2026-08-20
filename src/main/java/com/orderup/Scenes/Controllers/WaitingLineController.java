package com.orderup.Scenes.Controllers;

import com.orderup.Handlers.SceneManager;
import com.orderup.Scenes.Interfaces.OrderScene;

import javafx.fxml.FXML;

public class WaitingLineController {

    @FXML
    private void onTakeOrder() {
        SceneManager.show(new OrderScene());
    }
}
