package com.orderup.Handlers;

import com.almasb.fxgl.dsl.FXGL;
import com.orderup.Factory.WaitingLineUIFactory.UIType;
import com.orderup.Factory.OrderSceneUI.OrderUIType;
import com.orderup.Scenes.Interfaces.OrderScene;
import com.orderup.Scenes.Interfaces.WaitingLineScene;

public class ClickHandler {
    public static void onOrder() {
        SceneManager.show(new OrderScene());
        FXGL.getGameWorld().getSingleton(UIType.WAITING_LINE).removeFromWorld();
        FXGL.getGameWorld().getSingleton(UIType.ORDER_LIST).removeFromWorld();
    }

    // TEMPORARY !! WILL REPLACE FXML
    public static void onWaitingLine() {
        SceneManager.show(new WaitingLineScene());
        FXGL.getGameWorld().getSingleton(OrderUIType.COUNTER).removeFromWorld();
    }
}
