package com.orderup.Factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.orderup.Scenes.Components.CustomerAnimationComponent;

/**
 * Entity factory for customer entities.
 * <br><br>
 * Spawns customer entities with basic properties (type, position, bounding box)
 * and attaches a {@link CustomerAnimationComponent} for sprite animation.
 * Animation details (idle/walk channels, texture scaling) are fully managed
 * by the component, keeping this factory focused on entity creation only.
 */
public class CustomerFactory implements EntityFactory {

    /** Entity type used to query customer entities from the game world. */
    public enum CustomerType {
        CUSTOMER
    }

    // Frame configuration based on spritesheets
    private static final int FRAME_WIDTH = 32;
    private static final int FRAME_HEIGHT = 64;

    /**
     * Spawns a customer entity at the given position.
     *
     * <p>SpawnData must contain:
     * <ul>
     *   <li>{@code "customerId"} — integer, 1-indexed</li>
     * </ul>
     *
     * The entity is given a {@link CustomerType#CUSTOMER} type so it
     * can be retrieved with
     * {@code FXGL.getGameWorld().getEntitiesByType(CustomerType.CUSTOMER)}.
     */
    @Spawns("customer")
    public Entity customer(SpawnData data) {
        int customerId = data.hasKey("customerId") ? data.get("customerId") : 1;

        // Floor baseline alignment
        double floorY = 320.0;
        double spawnY = data.hasKey("y") ? data.get("y") : floorY;

        var entity = FXGL.entityBuilder(data)
                .type(CustomerType.CUSTOMER)
                .at(data.getX(), spawnY)
                .bbox(new HitBox(BoundingShape.box(FRAME_WIDTH, FRAME_HEIGHT)))
                .with(new CollidableComponent(true))
                .with(new CustomerAnimationComponent(customerId))
                .build();

        if (data.hasKey("targetX")) entity.setProperty("targetX", data.<Double>get("targetX"));
        if (data.hasKey("targetY")) entity.setProperty("targetY", data.<Double>get("targetY"));
        if (data.hasKey("arrivalTime")) entity.setProperty("arrivalTime", data.<Integer>get("arrivalTime"));
        if (data.hasKey("burstTime")) entity.setProperty("burstTime", data.<Integer>get("burstTime"));

        return entity;
    }
}