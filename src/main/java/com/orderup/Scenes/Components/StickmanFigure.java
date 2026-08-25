package com.orderup.Scenes.Components;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * A simple outline of a person, drawn with basic JavaFX shapes.
 */
public final class StickmanFigure {
    /** Horizontal center of the entire figure */
    private static final double CENTER_X = 40;

    /** How far from the top the center of the head circle sits */
    private static final double HEAD_Y = 15;

    /** Radius (half-width) of the head circle */
    private static final double HEAD_RADIUS = 10;

    /** Y position where the neck meets the torso */
    private static final double NECK_Y = HEAD_Y + HEAD_RADIUS; // 25

    /** Y position at the bottom of the torso (where legs start) */
    private static final double TORSO_BOTTOM = 55;

    /** Y position where the arms attach to the torso */
    private static final double ARM_Y = 35;

    /** How far left/right each arm extends from the center */
    private static final double ARM_LENGTH = 18;

    /** How far down each leg extends from the torso */
    private static final double LEG_LENGTH = 18;

    /** Stroke (outline) color for all body parts */
    private static final Color STROKE_COLOR = Color.web("#555");

    /** Line thickness for all body parts */
    private static final double STROKE_WIDTH = 2;

    private StickmanFigure() {}

    /**
     * Creates a stick figure and returns it as a JavaFX {@link Group}.
     * <br><br>
     * A {@code Group} is just a container that holds multiple shapes
     * and treats them as one object. You add it to a pane the same
     * way you'd add a single button or text label.
     *
     * @return a {@link Group} containing a circle (head), three lines
     *         (torso, arms), and two lines (legs)
     */
    public static Group create() {

        // ── Head ──────────────────────────────────────────────
        // A circle with no fill (transparent inside) and a visible
        // outline. setFill(null) means "don't color the inside."
        Circle head = new Circle(CENTER_X, HEAD_Y, HEAD_RADIUS);
        head.setFill(null);                       // transparent inside
        head.setStroke(STROKE_COLOR);             // outline color
        head.setStrokeWidth(STROKE_WIDTH);        // outline thickness

        // ── Torso (the vertical line from neck to waist) ──────
        Line torso = new Line(CENTER_X, NECK_Y, CENTER_X, TORSO_BOTTOM);
        torso.setStroke(STROKE_COLOR);
        torso.setStrokeWidth(STROKE_WIDTH);

        // ── Arms (one horizontal line, left-to-right) ─────────
        // Both arms share the same Y position and extend equally
        // to the left and right of center.
        Line arms = new Line(
            CENTER_X - ARM_LENGTH, ARM_Y,   // left hand
            CENTER_X + ARM_LENGTH, ARM_Y    // right hand
        );
        arms.setStroke(STROKE_COLOR);
        arms.setStrokeWidth(STROKE_WIDTH);

        // ── Left leg (diagonal from waist down-left) ──────────
        Line leftLeg = new Line(
            CENTER_X, TORSO_BOTTOM,                      // hip
            CENTER_X - LEG_LENGTH, TORSO_BOTTOM + LEG_LENGTH  // foot
        );
        leftLeg.setStroke(STROKE_COLOR);
        leftLeg.setStrokeWidth(STROKE_WIDTH);

        // ── Right leg (diagonal from waist down-right) ────────
        Line rightLeg = new Line(
            CENTER_X, TORSO_BOTTOM,                       // hip
            CENTER_X + LEG_LENGTH, TORSO_BOTTOM + LEG_LENGTH  // foot
        );
        rightLeg.setStroke(STROKE_COLOR);
        rightLeg.setStrokeWidth(STROKE_WIDTH);

        return new Group(head, torso, arms, leftLeg, rightLeg);
    }
}
