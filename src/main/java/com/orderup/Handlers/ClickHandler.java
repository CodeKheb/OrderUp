package com.orderup.Handlers;

import com.almasb.fxgl.dsl.FXGL;
import com.orderup.Factory.WaitingLineUIFactory.UIType;
import com.orderup.Scenes.Interfaces.OrderScene;

public class ClickHandler {
    public static void onOrder() {
        SceneManager.show(new OrderScene());
        FXGL.getGameWorld().getSingleton(UIType.WAITING_LINE).removeFromWorld();
    }
}
