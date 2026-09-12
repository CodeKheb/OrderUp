package com.orderup.Factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

import com.orderup.Scenes.Components.RhythmComponent;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.Group;

/**
 * Entity factory for the rhythm minigame.
 * <br><br>
 * Spawns a single "rhythm pair" entity: a small filled inner circle and a
 * stroke-only outer ring stacked at the same position. The outer ring
 * closes in on the inner circle over a duration derived from the front
 * customer's patience (BT) — see {@link RhythmComponent}.
 */
public class RhythmFactory implements EntityFactory {

    public enum RhythmType {
        RHYTHM_PAIR,
    }

    private static final double INNER_RADIUS = 35;
    private static final double RADIUS_PER_BT = 12;
    private static final double MAX_OUTER_RADIUS = 150;

    /**
     * Spawns the rhythm pair (inner circle + closing outer ring) at the
     * position given in {@code data}.
     * <br><br>
     * The outer ring's starting radius scales with the customer's BT, and
     * the closing duration equals the BT in seconds, so the ring always
     * reaches the inner circle exactly when patience (BT) hits 0.
     *
     * data keys:
     * <ul>
     *   <li>{@code x}, {@code y} — center position of the pair</li>
     *   <li>{@code burstTime} — customer's patience (BT)</li>
     * </ul>
     */
    @Spawns("rhythm_pair")
    public Entity rhythmPair(SpawnData data) {
        int burstTime = data.get("burstTime");

        double innerRadius = INNER_RADIUS;
        double startOuterRadius = Math.min(
                INNER_RADIUS + burstTime * RADIUS_PER_BT,
                MAX_OUTER_RADIUS);

        Circle inner = new Circle(innerRadius, Color.web("#cc5114"));
        Circle outer = new Circle(startOuterRadius);
        outer.setFill(null);
        outer.setStroke(Color.web("#D9A45B"));
        outer.setStrokeWidth(4);

        Group view = new Group(inner, outer);

        return FXGL.entityBuilder(data)
                .type(RhythmType.RHYTHM_PAIR)
                .view(view)
                .with(new RhythmComponent(outer, startOuterRadius, innerRadius, burstTime))
                .zIndex(50)
                .build();
    }
}
