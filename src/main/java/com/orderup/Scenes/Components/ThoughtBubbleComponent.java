package com.orderup.Scenes.Components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;

import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * FXGL component that draws a comic-style thought bubble above a customer so
 * they can "tell" the player what they want to order.
 *
 * <p>The bubble is a soft cloud-shaped box containing the order text, with a
 * chain of shrinking dots trailing down toward the character's head (the classic
 * "thinking" look). It is rendered as a UI overlay node, repositioned every frame
 * to stay above the customer while they walk to and wait in the line, and removed
 * automatically when the customer leaves the game world after being served.</p>
 *
 * <p>All layout constants are in screen pixels. The bubble sits centered over the
 * entity, with the lowest thought dot just above the character's head.</p>
 */
public class ThoughtBubbleComponent extends Component {

    // ── Geometry ────────────────────────────────────────────
    /** Height of the bubble cloud body (px). */
    private static final double BUBBLE_HEIGHT = 46.0;

    /** Smallest width the bubble will ever be, even for short orders. */
    private static final double BUBBLE_MIN_WIDTH = 76.0;

    /** Horizontal padding between the order text and the bubble edge. */
    private static final double TEXT_PADDING_X = 14.0;

    /** Corner arc for the cloud body (both radii) — rounded pill look. */
    private static final double BUBBLE_ARC = BUBBLE_HEIGHT;

    /** Diameters of the thought dots, biggest (closest to bubble) first. */
    private static final double[] DOT_DIAMETERS = {18.0, 11.0, 7.0};

    /** Vertical gap between consecutive thought dots. */
    private static final double DOT_GAP = 4.0;

    /** Vertical gap between the bubble cloud and the first thought dot. */
    private static final double BUBBLE_TO_DOT_GAP = 3.0;

    /** Distance from the entity center up to the lowest point of the thought chain. */
    private static final double HEAD_ABOVE_ORIGIN = 70.0;

    // ── Styling ─────────────────────────────────────────────
    private static final Color BUBBLE_FILL = Color.web("#FFF8E7");
    private static final Color BUBBLE_BORDER = Color.web("#D9A45B");
    private static final Color TEXT_COLOR = Color.web("#6B3E16");

    private static final double CLOUD_STROKE_WIDTH = 2.0;
    private static final double DOT_STROKE_WIDTH = 1.5;

    private static final Font ORDER_FONT = Font.font("System", FontWeight.BOLD, 15);

    /** The order text this customer is "thinking". */
    private final String orderText;

    /** UI overlay node that renders the thought bubble. */
    private Group bubbleGroup;

    /** Width of the fully laid-out bubble, used to keep it centered. */
    private double contentWidth;

    /** Height of the bubble cloud + thought chain, used for vertical placement. */
    private double contentHeight;

    /**
     * Creates the thought bubble component for a customer's order.
     *
     * @param orderText the order shown in the bubble, e.g. {@code "Pizza"}
     */
    public ThoughtBubbleComponent(String orderText) {
        this.orderText = orderText;
    }

    /**
     * Builds the bubble node and drops it into the game scene's UI layer.
     * Called automatically when the component is added to the customer entity.
     */
    @Override
    public void onAdded() {
        bubbleGroup = new Group();
        bubbleGroup.setMouseTransparent(true);
        buildBubble();
        FXGL.getGameScene().addUINode(bubbleGroup);
        reposition();
    }

    /**
     * Keeps the bubble glued above the customer's head while they move around.
     */
    @Override
    public void onUpdate(double tpf) {
        reposition();
    }

    /**
     * Removes the bubble from the UI layer when the customer entity leaves the
     * game world (i.e. their order has been served and they walk off).
     */
    @Override
    public void onRemoved() {
        if (bubbleGroup != null) {
            FXGL.getGameScene().removeUINode(bubbleGroup);
            bubbleGroup = null;
        }
    }

    /** Positions the bubble centered horizontally over the entity and above its head. */
    private void reposition() {
        bubbleGroup.setTranslateX(entity.getX() - contentWidth - 1/ 2.0);
        bubbleGroup.setTranslateY(entity.getY() - HEAD_ABOVE_ORIGIN - contentHeight);
    }

    /**
     * Lays out the bubble cloud, the order label, and the trailing thought dots
     * into {@link #bubbleGroup}. Coordinates are relative to the group's top-left.
     */
    private void buildBubble() {
        // Order label (measured first so the cloud can size itself to the text)
        Text label = new Text(orderText);
        label.setFont(ORDER_FONT);
        label.setFill(TEXT_COLOR);

        double textWidth = label.getLayoutBounds().getWidth();
        double textHeight = label.getLayoutBounds().getHeight();

        contentWidth = Math.max(BUBBLE_MIN_WIDTH, textWidth + 2 * TEXT_PADDING_X);

        // Cloud body
        Rectangle cloud = new Rectangle(0, 0, contentWidth, BUBBLE_HEIGHT);
        cloud.setArcWidth(Math.min(contentWidth, BUBBLE_ARC));
        cloud.setArcHeight(BUBBLE_ARC);
        cloud.setFill(BUBBLE_FILL);
        cloud.setStroke(BUBBLE_BORDER);
        cloud.setStrokeWidth(CLOUD_STROKE_WIDTH);
        cloud.setEffect(new DropShadow(8, 2, 3, Color.rgb(0, 0, 0, 0.30)));
        bubbleGroup.getChildren().add(cloud);

        // Order label centered inside the cloud.
        // Text layout origin is its baseline, so lift it by the ascent first.
        double baselineOffset = -label.getLayoutBounds().getMinY();
        double labelTop = (BUBBLE_HEIGHT - textHeight) / 2.0;
        label.setTranslateX((contentWidth - textWidth) / 2.0);
        label.setTranslateY(labelTop + baselineOffset);
        bubbleGroup.getChildren().add(label);

        // Thought dots shrinking as they get closer to the character's head
        double cursorY = BUBBLE_HEIGHT + BUBBLE_TO_DOT_GAP;
        for (int i = 0; i < DOT_DIAMETERS.length; i++) {
            double diameter = DOT_DIAMETERS[i];
            Circle dot = new Circle(contentWidth / 2.0, cursorY + diameter / 2.0, diameter / 2.0);
            dot.setFill(BUBBLE_FILL);
            dot.setStroke(BUBBLE_BORDER);
            dot.setStrokeWidth(DOT_STROKE_WIDTH);
            bubbleGroup.getChildren().add(dot);

            cursorY += diameter;
            if (i < DOT_DIAMETERS.length - 1) {
                cursorY += DOT_GAP;
            }
        }

        contentHeight = cursorY;
    }
}
