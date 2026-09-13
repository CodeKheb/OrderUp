package com.orderup.Uitility;

import com.orderup.Handlers.ClickHandler;

import javafx.scene.text.Font;

public class LoadFont {
    public static Font loadPixelFont(double size) {
        try {
            var url = ClickHandler.class.getResource("/assets/fonts/PressStart2P-Regular.ttf");
            if (url != null) {
                Font.loadFont(url.toExternalForm(), size);
                return Font.font("Press Start 2P", size);
            }
        } catch (Exception e) { }
        return Font.font("Courier New", size);
    }
}
