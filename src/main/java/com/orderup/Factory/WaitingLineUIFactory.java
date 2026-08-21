package com.orderup.Factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

import com.orderup.Handlers.SceneManager;
import com.orderup.Scenes.Interfaces.OrderScene;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class WaitingLineUIFactory implements EntityFactory{
    public enum UIType {
        ORDER_BUTTON,
    }

    @Spawns("order_button")
    public Entity orderButton(SpawnData data) {
        Circle circle = new Circle(100, Color.GREEN);
        Text text = new Text("Order Button");

        StackPane button = new StackPane(circle, text);

        Entity entity = FXGL.entityBuilder(data)
            .at(1000, 500)
            .viewWithBBox(button)
            .build();
        entity.getViewComponent().addOnClickHandler(e -> {
            SceneManager.show(new OrderScene());
            entity.removeFromWorld();
        });
        return entity;
    }
}
