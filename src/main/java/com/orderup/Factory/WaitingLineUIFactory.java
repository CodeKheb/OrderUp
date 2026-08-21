package com.orderup.Factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.ui.UI;
import com.orderup.Handlers.SceneManager;
import com.orderup.Scenes.Interfaces.OrderScene;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class WaitingLineUIFactory implements EntityFactory{
    public enum UIType {
        ORDER_BUTTON,
        WAITING_LINE
    }

    @Spawns("order_button")
    public Entity orderButton(SpawnData data) {
        Circle circle = new Circle(100, Color.GREEN);
        Text text = new Text("Order Button");

        StackPane button = new StackPane(circle, text);

        Entity entity = FXGL.entityBuilder(data)
            .type(UIType.ORDER_BUTTON)
            .at(1000, 500)
            .viewWithBBox(button)
            .build();
        entity.getViewComponent().addOnClickHandler(e -> {
            SceneManager.show(new OrderScene());
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
}
