package com.orderup.Scenes.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import javafx.util.Duration;

/**
 * FXGL Component that manages sprite animation for customer entities.
 * <br><br>
 * Determines the character variant (male/female) based on the customer's ID,
 * then sets up idle and walking animation channels accordingly. The animated
 * texture is attached to the entity's view in {@link #onAdded()}.
 */
public class CustomerAnimationComponent extends Component {

    /** Single frame width of the spritesheet (pixels). */
    private static final int FRAME_WIDTH = 128;

    /** Single frame height of the spritesheet (pixels). */
    private static final int FRAME_HEIGHT = 128;

    /** Scale multiplier for the sprite on screen. */
    private static final double SPRITE_SCALE = 2.5;

    private final AnimationChannel idleAnim;
    private final AnimationChannel walkAnim;
    private final AnimatedTexture texture;

    /**
     * Creates the animation component for a customer.
     *
     * @param customerId the customer's ID; odd IDs use the girl sprite,
     *                   even IDs use the man sprite
     */
    public CustomerAnimationComponent(int customerId) {
        boolean isGirl = (customerId % 2 != 0);

        idleAnim = isGirl
                ? new AnimationChannel(FXGL.image("girl1_idle.png"), 9, FRAME_WIDTH, FRAME_HEIGHT, Duration.seconds(3), 0, 8)
                : new AnimationChannel(FXGL.image("man1_idle.png"), 6, FRAME_WIDTH, FRAME_HEIGHT, Duration.seconds(3), 0, 5);

        walkAnim = isGirl
                ? new AnimationChannel(FXGL.image("girl1_walk.png"), 12, FRAME_WIDTH, FRAME_HEIGHT, Duration.seconds(1), 0, 11)
                : new AnimationChannel(FXGL.image("man1_walk.png"), 10, FRAME_WIDTH, FRAME_HEIGHT, Duration.seconds(1), 0, 9);

        texture = new AnimatedTexture(walkAnim);
        texture.loop();
    }

    /**
     * Called when the component is attached to an entity.
     * Adds the animated texture to the entity's view and applies the sprite scale.
     */
    @Override
    public void onAdded() {
        // Center the sprite on the entity's position by translating
        // by half the frame dimensions in both axes.
        texture.setTranslateX(-FRAME_WIDTH / 2.0);
        texture.setTranslateY(-FRAME_HEIGHT / 2.0);
        entity.getViewComponent().addChild(texture);
        entity.setScaleX(SPRITE_SCALE);
        entity.setScaleY(SPRITE_SCALE);
    }

    /**
     * Returns the idle animation channel.
     */
    public AnimationChannel getIdleAnim() {
        return idleAnim;
    }

    /**
     * Returns the walk animation channel.
     */
    public AnimationChannel getWalkAnim() {
        return walkAnim;
    }

    /**
     * Returns the animated texture node.
     */
    public AnimatedTexture getTexture() {
        return texture;
    }

    /**
     * Switches to the idle animation if not already playing.
     */
    public void playIdle() {
        if (texture.getAnimationChannel() != idleAnim) {
            texture.loopAnimationChannel(idleAnim);
        }
    }

    /**
     * Switches to the walk animation if not already playing.
     */
    public void playWalk() {
        if (texture.getAnimationChannel() != walkAnim) {
            texture.loopAnimationChannel(walkAnim);
        }
    }
}
