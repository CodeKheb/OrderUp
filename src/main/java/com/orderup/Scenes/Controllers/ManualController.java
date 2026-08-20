package com.orderup.Scenes.Controllers;

import com.orderup.Handlers.SceneManager;
import com.orderup.Scenes.Interfaces.MenuScene;
import javafx.fxml.FXML;

public class ManualController {

    @FXML
    private void onBack() {
        SceneManager.show(new MenuScene());
    }
}
