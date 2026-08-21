package com.orderup.Factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

public class OrderSceneUI implements EntityFactory {
    public enum OrderUIType {
        COUNTER
    }

    @Spawns("counter")
    public Entity waitingLine(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(OrderUIType.COUNTER)
                .at(0, 350)
                .view(FXGL.texture("counter.png", 0, 0))
                .scale(3.0, 3.0)
                .build();
    }



}
