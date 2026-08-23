package com.orderup.Factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.orderup.Handlers.ClickHandler;

/**
 * Entity factory for the waiting line scene.
 * <br><br>
 * Defines and spawns interactive entities used in the waiting line screen:
 * an order button with a click handler, a waiting line background texture,
 * and an order list texture. Each entity type is registered with FXGL's
 * spawn system via {@link Spawns} annotations.
 */
public class WaitingLineUIFactory implements EntityFactory {
    public enum UIType {
        ORDER_BUTTON,
        ORDER_LIST,
        WAITING_LINE
    }

    @Spawns("order_button")
    public Entity orderButton(SpawnData data) {
        Entity entity = FXGL.entityBuilder(data)
                .type(UIType.ORDER_BUTTON)
                .at(1050, 650)
                .view(FXGL.texture("order_button.png", 0, 0))
                .scale(2.0, 2.0)
                .build();
        entity.getViewComponent().addOnClickHandler(e -> {
            ClickHandler.onOrder();
            entity.removeFromWorld();
        });
        return entity;
    }

    @Spawns("waiting_line")
    public Entity waitingLine(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(UIType.WAITING_LINE)
                .view(FXGL.texture("waiting_line.png", 0, 0))
                .scale(2.0, 1.5)
                .build();
    }

    @Spawns("order_list")
    public Entity orderList(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(UIType.ORDER_LIST)
                .at(1100, 0)
                .view(FXGL.texture("order_list.png", 0, 0))
                .scale(2.2, 2.2)
                .build();
    }
}
