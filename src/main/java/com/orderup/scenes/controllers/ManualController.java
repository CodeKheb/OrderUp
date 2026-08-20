package com.orderup.scenes.controllers;

import com.orderup.handlers.SceneManager;
import com.orderup.scenes.interfaces.MenuScene;
import javafx.fxml.FXML;

public class ManualController {

    @FXML
    private void onBack() {
        SceneManager.show(new MenuScene());
    }
}
