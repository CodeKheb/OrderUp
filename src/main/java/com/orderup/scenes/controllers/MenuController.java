package com.orderup.scenes.controllers;

import com.orderup.handlers.SceneManager;
import com.orderup.scenes.interfaces.WaitingLineScene;
import com.orderup.scenes.interfaces.ManualScene;
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
