package com.orderup;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.orderup.scenes.MenuScene;
import com.orderup.handlers.SceneManager;

public class Application extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Order Up");
        settings.setVersion("1.0");
    }

    @Override
    protected void initUI() {
        SceneManager.show(new MenuScene());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
