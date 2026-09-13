package com.orderup.Handlers;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import com.orderup.Models.CustomerProcess.CharacterType;
import com.orderup.Models.RhythmScore;
import com.orderup.Scenes.Components.RhythmComponent;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Handles click events on game entities.
 * <br><br>
 * Provides static methods that are wired to FXGL entity click handlers
 */
public class ClickHandler {

    /** Rise distance of the score popup (pixels). */
    private static final double POPUP_RISE = 45;

    /** Lifetime of the score popup (seconds). */
    private static final double POPUP_DURATION = 0.7;

    /** Font size of a regular "+N" popup. */
    private static final double POPUP_FONT_SIZE = 22;

    /** Font size of the "PERFECT!" popup. */
    private static final double POPUP_FONT_SIZE_PERFECT = 30;

    /**
     * Handles a click on a rhythm circle — awards points based on how
     * close the outer ring is to the inner circle, applies the combo
     * multiplier, shows a floating score popup at the circle's
     * position, then removes the circle pair from the game world.
     * <br><br>
     * Perfect timing (ring exactly on the inner circle, within the
     * grace window) scores {@value RhythmScore#MAX_POINTS} base points
     * and grows the combo (×1, ×1.5, ×2, … capped at
     * ×{@value RhythmScore#MAX_MULTIPLIER}); other clicks score scaled
     * base points and break the combo.
     *
     * @param circle the rhythm pair entity that was clicked
     */
    public static void CircleClicked(Entity circle) {
        int points = 0;
        boolean perfect = false;
        double multiplier = 1.0;

        AudioManager.click();

        RhythmComponent rhythm = circle.getComponent(RhythmComponent.class);
        if (rhythm != null) {
            perfect = rhythm.isPerfect();

            if (perfect) {
                CharacterType characterType = circle.<CharacterType>getPropertyOptional("characterType").orElse(null);
                if (characterType != null) {
                    AudioManager.playPerfect(characterType);
                }
                int combo = RhythmScore.registerPerfect();
                multiplier = RhythmScore.multiplierFor(combo);
            } else {
                RhythmScore.breakCombo();
            }

            int base = (int) Math.round(
                    RhythmScore.MAX_POINTS * rhythm.getAccuracy());
            points = (int) Math.round(base * multiplier);
            RhythmScore.add(points);
        }

        spawnScorePopup(circle.getPosition(), points, perfect, multiplier);

        EffectsHandler.spawnBurst(circle.getPosition(),
                rhythm != null ? rhythm.getCurrentOuterRadius() : 40);

        circle.removeFromWorld();
    }

    /**
     * Spawns a floating score popup that rises and fades out above the
     * given position. Perfect hits show "PERFECT!" (plus the combo
     * multiplier once it exceeds ×1) in gold; other hits show "+N" in
     * white.
     *
     * @param position center position to anchor the popup at
     * @param points points awarded for this hit
     * @param perfect whether this hit was within the perfect grace window
     * @param multiplier combo multiplier applied to this hit
     */
    private static void spawnScorePopup(Point2D position, int points,
                                        boolean perfect, double multiplier) {
        String label;
        Color color;
        double fontSize;

        if (perfect) {
            label = (multiplier > 1.0)
                    ? "PERFECT! ×" + RhythmScore.formatMultiplier(multiplier)
                    : "PERFECT!";
            color = Color.web("#D9A45B");
            fontSize = POPUP_FONT_SIZE_PERFECT;
        } else {
            label = "+" + points;
            color = Color.WHITE;
            fontSize = POPUP_FONT_SIZE;
        }

        Text text = new Text(label);
        text.setFont(Font.font("Monospace", FontWeight.BOLD, fontSize));
        text.setFill(color);
        // Center the text on the popup's position.
        text.setTranslateX(-text.getLayoutBounds().getWidth() / 2);
        text.setTranslateY(fontSize / 2.0);

        Entity popup = FXGL.entityBuilder()
                .at(position)
                .view(text)
                .zIndex(60)
                .build();
        FXGL.getGameWorld().addEntity(popup);

        riseAndFade(popup, text);
    }

    /**
     * Animates a popup entity: rises by {@link #POPUP_RISE} pixels while
     * fading out, then removes itself from the game world.
     *
     * @param popup the popup entity
     * @param text the text node to fade
     */
    private static void riseAndFade(Entity popup, Text text) {
        TranslateTransition rise = new TranslateTransition(
                Duration.seconds(POPUP_DURATION), text);
        rise.setByY(-POPUP_RISE);

        FadeTransition fade = new FadeTransition(
                Duration.seconds(POPUP_DURATION), text);
        fade.setToValue(0.0);

        PauseTransition lifetime = new PauseTransition(
                Duration.seconds(POPUP_DURATION));
        lifetime.setOnFinished(e -> popup.removeFromWorld());

        new ParallelTransition(rise, fade, lifetime).play();
    }

}
