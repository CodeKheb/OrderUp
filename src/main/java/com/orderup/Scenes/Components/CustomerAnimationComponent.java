package com.orderup.Scenes.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import com.orderup.Models.CustomerProcess.CharacterType;

import javafx.util.Duration;

/**
 * FXGL Component that manages sprite animation for customer entities.
 * <br><br>
 * Supports multiple character variants (girl1-3, man1-3) selected by
 * {@code CharacterType}. The animated texture is attached to the entity's
 * view in {@link #onAdded()}.
 */
public class CustomerAnimationComponent extends Component {

    /** Single frame width of the spritesheet (pixels). */
    private static final int FRAME_WIDTH = 128;

    /** Single frame height of the spritesheet (pixels). */
    private static final int FRAME_HEIGHT = 128;

    /** Scale multiplier for the sprite on screen. */
    private static final double SPRITE_SCALE = 2.5;

    /** Scale multiplier for male sprites (larger to match girl proportions). */
    private static final double MAN_SPRITE_SCALE = 2.8;

    /** Walk frame counts are the same for all variants of each gender */
    private static final int GIRL_WALK_FRAMES = 12;
    private static final int MAN_WALK_FRAMES = 10;

    private final AnimationChannel idleAnim;
    private final AnimationChannel walkAnim;
    private final AnimatedTexture texture;
    private final boolean isGirl;
    private final int spriteIndex;

    /**
     * Creates the animation component for a customer with the given character type.
     * <br><br>
     * The character type determines the sprite variant:
     * <ul>
     *   <li>GIRL1 → girl1, GIRL2 → girl2, GIRL3 → girl3</li>
     *   <li>MAN1 → man1, MAN2 → man2, MAN3 → man3</li>
     * </ul>
     *
     * @param customerId the customer's ID (1-based)
     * @param characterType the character type (GIRL1-3, MAN1-3) specifying sprite variant
     */
    public CustomerAnimationComponent(int customerId, CharacterType characterType) {
        this.isGirl = characterType.isGirl();
        this.spriteIndex = characterType.getSpriteIndex();
        String variant = characterType.getPrefix() + this.spriteIndex + "_";

        // Frame counts differ between girl and man spritesheets
        // Girl idle varies per variant (girl1=9, girl2=7, girl3=6)
        int idleFrames = characterType.getIdleFrameCount();
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
        double scale = isGirl ? SPRITE_SCALE : MAN_SPRITE_SCALE;
        entity.setScaleX(scale);
        entity.setScaleY(scale);
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
