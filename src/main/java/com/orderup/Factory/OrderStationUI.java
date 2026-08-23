package com.orderup.Factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.orderup.Handlers.ClickHandler;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Entity factory for the order scene.
 * <br><br>
 * Defines and spawns entities used in the order screen:
 * a counter texture and a stove texture. Each entity type is registered
 * with FXGL's spawn system via {@link Spawns} annotations.
 */
public class OrderStationUI implements EntityFactory {
    public enum OrderUIType {
        COUNTER,
        STOVE,
        BACK_BUTTON
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

    @Spawns("back_button")
    public Entity backButton(SpawnData data) {
        Circle circle = new Circle(100, Color.WHITE);
        Text text = new Text("Back");
        text.setFont(Font.font(48));

        StackPane button = new StackPane(circle, text);

        Entity entity = FXGL.entityBuilder(data)
                .type(OrderUIType.BACK_BUTTON)
                .at(1000, 500)
                .viewWithBBox(button)
                .build();
        entity.getViewComponent().addOnClickHandler(e -> {
            ClickHandler.onWaitingLine();
            entity.removeFromWorld();
        });
        return entity;
    }
}
