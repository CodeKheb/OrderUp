package com.orderup.Factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Entity factory for the waiting line scene.
 * <br><br>
 * Defines and spawns interactive entities used in the waiting line screen:
 * an order button with a click handler, a waiting line background texture,
 * and an order list texture. Each entity type is registered with FXGL's
 * spawn system via {@link Spawns} annotations.
 */
public class WaitingLineUIFactory implements EntityFactory {
    public enum WaitingUIType {
        WAITING_LINE,
        BACKGROUND,
        RHYTHM_BACKGROUND,
        CLOCK
    }

    @Spawns("background")
    public Entity background(SpawnData data) {
        Rectangle rect = new Rectangle(1280, 1000, Color.BLACK);
        return FXGL.entityBuilder(data)
                .type(WaitingUIType.BACKGROUND)
                .viewWithBBox(rect)
                .zIndex(-100)
                .build();
    }

    @Spawns("waiting_line")
    public Entity waitingLine(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(WaitingUIType.WAITING_LINE)
                .view(FXGL.texture("waiting_line.png", 950, 480))
                .at(330, 0)
                .zIndex(-90)
                .build();
    }

    @Spawns("clock")
    public Entity clock(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(WaitingUIType.CLOCK)
                .view(FXGL.texture("clock.png", 390, 200))
                .at(850, 520)
                .zIndex(-90)
                .build();
    }


    @Spawns("rhythm_background")
    public Entity rhythmBackground(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(WaitingUIType.RHYTHM_BACKGROUND)
                .view(FXGL.texture("rhythm_background.png", 330, 780))
                .at(0, 0)
                .zIndex(-100)
                .build();
    }




}
