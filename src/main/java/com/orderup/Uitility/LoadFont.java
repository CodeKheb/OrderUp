package com.orderup.Uitility;

import com.orderup.Handlers.ClickHandler;
import com.orderup.Scenes.Components.GanttOverlay;

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

    public static String loadPixelFont() {
        try {
            var url = GanttOverlay.class.getResource("/assets/fonts/PressStart2P-Regular.ttf");
            if (url != null) {
                Font.loadFont(url.toExternalForm(), 16);
                return "Press Start 2P";
            }
        } catch (Exception e) {
            // fall through
        }
        return "Courier New";
    }
}
