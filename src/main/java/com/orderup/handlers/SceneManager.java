package com.orderup.handlers;

import com.almasb.fxgl.dsl.FXGLForKtKt;
import javafx.scene.Node;

public class SceneManager {

    public static void show(Node view) {
        FXGLForKtKt.getGameScene().clearUINodes();
        FXGLForKtKt.getGameScene().addUINode(view);
    }
}
