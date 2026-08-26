package com.orderup.Factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Entity factory for customer entities.
 * <br><br>
 * Each customer is rendered as a colored placeholder rectangle.
 * The color is determined by the customer's ID so that every
 * customer in the waiting line is visually distinct.
 * <br><br>
 * When real character textures are available, replace the
 * {@link Rectangle} with {@code FXGL.texture(...)}.
 */
public class CustomerFactory implements EntityFactory {

    /** Entity type used to query customer entities from the game world. */
    public enum CustomerType {
        CUSTOMER
    }

    /** Distinct colors for 6 customers. Cycles if more. */
    public static final Color[] COLORS = {
        Color.RED,
        Color.DODGERBLUE,
        Color.LIMEGREEN,
        Color.ORANGE,
        Color.MEDIUMPURPLE,
        Color.HOTPINK
    };

    /** Width of the placeholder rectangle. */
    private static final double WIDTH = 50;

    /** Height of the placeholder rectangle. */
    private static final double HEIGHT = 70;

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
        Color color = COLORS[(customerId - 1) % COLORS.length];

        Rectangle rect = new Rectangle(WIDTH, HEIGHT, color);
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(1);

        var entity = FXGL.entityBuilder(data)
                .type(CustomerType.CUSTOMER)
                .viewWithBBox(rect)
                .with(new CollidableComponent(true))
                .build();

        if (data.hasKey("targetX")) entity.setProperty("targetX", data.<Double>get("targetX"));
        if (data.hasKey("targetY")) entity.setProperty("targetY", data.<Double>get("targetY"));
        if (data.hasKey("arrivalTime")) {
            entity.setProperty("arrivalTime", data.<Integer>get("arrivalTime"));
            System.out.println(data.<Integer>get("arrivalTime"));
        }
        if (data.hasKey("burstTime")) entity.setProperty("burstTime", data.<Integer>get("burstTime"));

        return entity;
    }
}
