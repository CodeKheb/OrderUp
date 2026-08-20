package com.orderup.Scenes.Controllers;

import com.orderup.Handlers.SceneManager;
import com.orderup.Scenes.Interfaces.ManualScene;
import com.orderup.Scenes.Interfaces.WaitingLineScene;

import javafx.fxml.FXML;

public class MenuController {

    @FXML
    private void onPlay() {
        SceneManager.show(new WaitingLineScene());
    }

    @FXML
    private void onManual() {
        SceneManager.show(new ManualScene());
    }
}
