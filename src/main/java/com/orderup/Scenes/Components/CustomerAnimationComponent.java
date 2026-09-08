package com.orderup.Scenes.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import javafx.util.Duration;

/**
 * FXGL Component that manages sprite animation for customer entities.
 * <br><br>
 * Supports multiple character variants (girl1-3, man1-3) selected by
 * {@code spriteIndex}. The animated texture is attached to the entity's
 * view in {@link #onAdded()}.
 */
public class CustomerAnimationComponent extends Component {

    /** Single frame width of the spritesheet (pixels). */
    private static final int FRAME_WIDTH = 128;

    /** Single frame height of the spritesheet (pixels). */
    private static final int FRAME_HEIGHT = 128;

    /** Scale multiplier for the sprite on screen. */
    private static final double SPRITE_SCALE = 2.5;

    /** Number of available sprite variants for each gender. */
    private static final int MAX_SPRITE_VARIANT = 3;

    /** Idle frame counts per sprite variant: [girl1, girl2, girl3] */
    private static final int[] GIRL_IDLE_FRAMES = {9, 7, 6};

    /** Idle frame counts are the same for all man variants */
    private static final int MAN_IDLE_FRAMES = 6;

    /** Walk frame counts are the same for all variants of each gender */
    private static final int GIRL_WALK_FRAMES = 12;
    private static final int MAN_WALK_FRAMES = 10;

    private final AnimationChannel idleAnim;
    private final AnimationChannel walkAnim;
    private final AnimatedTexture texture;
    private final boolean isGirl;
    private final int spriteIndex;

    /**
     * Creates the animation component for a customer.
     * <br><br>
     * The sprite variant is cycled automatically based on customer ID:
     * <ul>
     *   <li>Customer 1 (girl) → girl1</li>
     *   <li>Customer 2 (man)  → man1</li>
     *   <li>Customer 3 (girl) → girl2</li>
     *   <li>Customer 4 (man)  → man2</li>
     *   <li>Customer 5 (girl) → girl3</li>
     *   <li>Customer 6 (man)  → man3</li>
     * </ul>
     *
<<<<<<< Updated upstream
     * CURRENTLY: only supports male and female characters in binary order (odd IDs = female, even IDs = male)
     * TODO: add support for explicit multiple character types
     *
     * @param customerId the customer's ID; odd IDs use the girl sprite,
     *                   even IDs use the man sprite
=======
     * @param customerId the customer's ID (1-based); used to determine gender
     *                   and cycle through sprite variants
>>>>>>> Stashed changes
     */
    public CustomerAnimationComponent(int customerId) {
        this(customerId, ((customerId - 1) / 2) % MAX_SPRITE_VARIANT + 1);
    }

    /**
     * Creates the animation component for a customer with an explicit sprite variant.
     *
     * @param customerId the customer's ID; used to determine gender
     * @param spriteIndex the sprite variant index (1-based, 1-3)
     */
    public CustomerAnimationComponent(int customerId, int spriteIndex) {
        this.isGirl = (customerId % 2 != 0);
        this.spriteIndex = Math.max(1, Math.min(spriteIndex, MAX_SPRITE_VARIANT));
        String prefix = isGirl ? "girl" : "man";
        String variant = prefix + this.spriteIndex + "_";

        // Frame counts differ between girl and man spritesheets
        // Girl idle varies per variant (girl1=9, girl2=7, girl3=6)
        int idleFrames = isGirl ? GIRL_IDLE_FRAMES[this.spriteIndex - 1] : MAN_IDLE_FRAMES;
        int walkFrames = isGirl ? GIRL_WALK_FRAMES : MAN_WALK_FRAMES;

        idleAnim = new AnimationChannel(
                FXGL.image(variant + "idle.png"), idleFrames, FRAME_WIDTH, FRAME_HEIGHT,
                Duration.seconds(3), 0, idleFrames - 1);

        walkAnim = new AnimationChannel(
                FXGL.image(variant + "walk.png"), walkFrames, FRAME_WIDTH, FRAME_HEIGHT,
                Duration.seconds(1), 0, walkFrames - 1);

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
     * Returns the sprite variant index (1-3).
     */
    public int getSpriteIndex() {
        return spriteIndex;
    }

    /**
     * Returns true if this is a girl character.
     */
    public boolean isGirl() {
        return isGirl;
    }

    /**
     * Returns the maximum number of sprite variants available.
     */
    public static int getMaxSpriteVariant() {
        return MAX_SPRITE_VARIANT;
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
