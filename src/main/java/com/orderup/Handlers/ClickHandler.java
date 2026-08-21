package com.orderup.Handlers;

import com.almasb.fxgl.dsl.FXGL;
import com.orderup.Factory.WaitingLineUIFactory.UIType;
import com.orderup.Factory.OrderSceneUI.OrderUIType;
import com.orderup.Scenes.Interfaces.OrderScene;
import com.orderup.Scenes.Interfaces.WaitingLineScene;

/**
 * Handles click events on game entities and manages scene transitions.
 * <br><br>
 * Provides static methods that are wired to FXGL entity click handlers
 * via {@link com.orderup.Factory.WaitingLineUIFactory}. Transitions
 * between scenes by using {@link SceneManager} and removing associated
 * entities from the game world.
 */
public class ClickHandler {

    /**
     * Handles the order button click by transitioning to the order scene
     * and removing waiting line entities from the game world.
     */
    public static void onOrder() {
        SceneManager.show(new OrderScene());
        FXGL.getGameWorld().getSingleton(UIType.WAITING_LINE).removeFromWorld();
        FXGL.getGameWorld().getSingleton(UIType.ORDER_LIST).removeFromWorld();
    }

    /**
     * Handles the back-to-waiting-line transition by showing the waiting line
     * scene and removing order scene entities (counter, stove) from the game world.
     */
    public static void onWaitingLine() {
        SceneManager.show(new WaitingLineScene());
        FXGL.getGameWorld().getSingleton(OrderUIType.COUNTER).removeFromWorld();
        FXGL.getGameWorld().getSingleton(OrderUIType.STOVE).removeFromWorld();
    }
}
