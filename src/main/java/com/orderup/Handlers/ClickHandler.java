package com.orderup.Handlers;

// import com.almasb.fxgl.dsl.FXGL;
// import com.orderup.Factory.WaitingLineUIFactory.WaitingUIType;

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
        System.out.println("larp larp larp sahur");
    }
}
