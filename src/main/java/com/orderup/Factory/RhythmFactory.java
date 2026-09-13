package com.orderup.Factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

import com.orderup.Handlers.ClickHandler;
import com.orderup.Models.CustomerProcess.CharacterType;
import com.orderup.Models.MenuItem;
import com.orderup.Scenes.Components.RhythmComponent;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.Group;

/**
 * Entity factory for the rhythm minigame.
 * <br>
 * <br>
 * Spawns a single "rhythm pair" entity: a small filled inner circle and a
 * stroke-only outer ring stacked at the same position. The outer ring
 * closes in on the inner circle over a duration derived from the front
 * customer's patience (BT) — see {@link RhythmComponent}.
 */
public class RhythmFactory implements EntityFactory {

    public enum RhythmType {
        RHYTHM_PAIR,
        COFFEE,
        BURGER,
        PIZZA,
        FRIES,
    }

    private static final double INNER_RADIUS = 55;
    private static final double INNER_STROKE_RADIUS = 60;
    private static final double RADIUS_PER_BT = 8;
    private static final double MAX_OUTER_RADIUS = 180;
    private static final double ICON_SCALE = 1; // DO NOT TOUCH, IF YOU WANT CENTERED

    /**
     * Spawns the rhythm pair (inner circle + closing outer ring) at the
     * position given in {@code data}.
     * <br>
     * <br>
     * The outer ring's starting radius scales with the customer's BT, and
     * the closing duration equals the BT in seconds, so the ring always
     * reaches the inner circle exactly when patience (BT) hits 0.
     *
     * data keys:
     * <ul>
     * <li>{@code x}, {@code y} — center position of the pair</li>
     * <li>{@code burstTime} — customer's patience (BT)</li>
     * </ul>
     */
    @Spawns("rhythm_pair")
    public Entity rhythmPair(SpawnData data) {
        int burstTime = data.get("burstTime");
        CharacterType characterType = data.hasKey("characterType")
                ? data.get("characterType")
                : null;

        double innerRadius = INNER_RADIUS;
        double startOuterRadius = Math.min(
                INNER_RADIUS + burstTime * RADIUS_PER_BT,
                MAX_OUTER_RADIUS);

        Circle inner = new Circle(innerRadius);
        inner.setFill(Color.web("#cc5114"));

        Circle innerStroke = new Circle(INNER_STROKE_RADIUS);
        innerStroke.setFill(Color.web("#D9A45B"));

        Circle outer = new Circle(startOuterRadius);
        outer.setFill(null);
        outer.setStroke(Color.web("#D9A45B"));
        outer.setStrokeWidth(4);

        // Center the icon
        Group view = new Group(innerStroke, inner, outer);
        MenuItem order = data.get("order");
        if (order != null) {
            var icon = FXGL.texture(order.name().toLowerCase() + ".png");
            double iconW = icon.getImage().getWidth();
            double iconH = icon.getImage().getHeight();
            icon.setTranslateX(-(iconW * ICON_SCALE) / 2.0);
            icon.setTranslateY(-(iconH * ICON_SCALE) / 2.0);
            icon.setScaleX(ICON_SCALE);
            icon.setScaleY(ICON_SCALE);
            view.getChildren().add(icon);
        }

        Entity entity = FXGL.entityBuilder(data)
                .type(RhythmType.RHYTHM_PAIR)
                .viewWithBBox(view)
                .with(new RhythmComponent(outer, startOuterRadius, innerRadius, burstTime))
                .onClick(e -> ClickHandler.CircleClicked(e))
                .zIndex(50)
                .build();

        if (characterType != null) {
            entity.setProperty("characterType", characterType);
        }

        return entity;
    }
}
