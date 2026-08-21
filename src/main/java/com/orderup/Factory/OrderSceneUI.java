package com.orderup.Factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

/**
 * Entity factory for the order scene.
 * <br><br>
 * Defines and spawns entities used in the order screen:
 * a counter texture and a stove texture. Each entity type is registered
 * with FXGL's spawn system via {@link Spawns} annotations.
 */
public class OrderSceneUI implements EntityFactory {
    public enum OrderUIType {
        COUNTER,
        STOVE
    }

    @Spawns("counter")
    public Entity counter(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(OrderUIType.COUNTER)
                .at(0, 350)
                .view(FXGL.texture("counter.png", 0, 0))
                .scale(3.0, 3.0)
                .build();
    }

    @Spawns("stove")
    public Entity stove(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(OrderUIType.STOVE)
                .at(0, 320)
                .view(FXGL.texture("stove.png", 0, 0))
                .build();
    }

}
