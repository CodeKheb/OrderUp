package com.orderup.Scenes.Components;

import com.orderup.Models.CustomerProcess.CharacterType;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.StringConverter;

/**
 * A card for inputting one customer's arrival and burst time (Manual mode).
 *
 * <p>Shows an animated character with arrow buttons on either side (for changing
 * the character's sprite), two sliders (AT and BT), and an Add button that
 * confirms the entry and advances to the next customer.</p>
 *
 * <pre>
 * ┌──────────────────────────────────────────────┐
 * │              Customer 1                       │
 * │   [<]    [animated sprite]    [>]              │
 * │   Arrival (AT):  [====slider====]  7:00 A.M.  │
 * │   Patience (BT): [====slider====]  3           │
 * │                  [Add]                         │
 * └──────────────────────────────────────────────┘
 * </pre>
 */
public class CustomerCard extends VBox {

    private static final int CUSTOMER_COUNT = 6;

    /** Maximum value for Burst Time (Patience) slider. Change this to adjust the range. */
    private static final int MAX_BURST_TIME = 8;

    /** Minimum value for Arrival Time slider. */
    private static final int MIN_ARRIVAL_TIME = 0;

    /** Maximum value for Arrival Time slider. */
    private static final int MAX_ARRIVAL_TIME = 30;

    /** Sprite sheet frame dimensions (pixels). */
    private static final int FRAME_WIDTH = 128;
    private static final int FRAME_HEIGHT = 128;

    /** Display size for the character sprite in the card (pixels). */
    private static final int SPRITE_DISPLAY_SIZE = 250;

    /** Fill color for the slider progress bar. */
    private static final Color SLIDER_FILL_COLOR = Color.web("#4a90d9");

    /** Index of the customer currently being edited (0–5). */
    private int currentIndex = 0;

    /** Number of customers the user has confirmed via the Add button. */
    private int addedCount = 0;

    /** Displays "Customer N" in the header — updates as the user advances. */
    private final Text titleText = new Text();

    /** One slider per customer for Arrival Time. */
    private final Slider[] atSliders = new Slider[CUSTOMER_COUNT];

    /** One slider per customer for Patience (Burst Time). */
    private final Slider[] btSliders = new Slider[CUSTOMER_COUNT];

    /** Value labels showing the current slider value. */
    private final Text[] atValueLabels = new Text[CUSTOMER_COUNT];
    private final Text[] btValueLabels = new Text[CUSTOMER_COUNT];

    /** Fill rectangles for visual progress on sliders. */
    private final Rectangle[] atFills = new Rectangle[CUSTOMER_COUNT];
    private final Rectangle[] btFills = new Rectangle[CUSTOMER_COUNT];

    /** Character type chosen per customer (GIRL or MAN). */
    private final CharacterType[] characterTypes = new CharacterType[CUSTOMER_COUNT];

    /** Current sprite frame index for the animated display. */
    private int currentFrameIndex = 0;

    /** The ImageView showing the animated character sprite. */
    private final ImageView spriteView = new ImageView();

    /** Timeline driving the idle sprite animation. */
    private Timeline idleTimeline;

    /** Frames for the currently displayed character's idle animation. */
    private WritableImage[] currentIdleFrames;

    /** Horizontal rows holding the label + slider + value for AT and BT. */
    private final HBox atRow;
    private final HBox btRow;

    /** Button to confirm the current customer and advance to the next. */
    private final Button addBtn;

    public CustomerCard() {
        // Initialize all character types to GIRL by default
        for (int i = 0; i < CUSTOMER_COUNT; i++) {
            characterTypes[i] = CharacterType.GIRL;
        }

        // ── 1. Header: just the title ────────────────────────
        titleText.getStyleClass().add("customer-header-text");

        HBox header = new HBox(titleText);
        header.setAlignment(Pos.CENTER);
        header.getStyleClass().add("customer-header");
        header.setPadding(Insets.EMPTY);

        // ── 2. Character sprite with arrows on either side ───
        Button prevCharBtn = createArrowButton("\u276E");
        prevCharBtn.setOnAction(e -> changeCharacter(-1));

        spriteView.setFitWidth(SPRITE_DISPLAY_SIZE);
        spriteView.setFitHeight(SPRITE_DISPLAY_SIZE);
        spriteView.setPreserveRatio(true);

        VBox spriteBox = new VBox(spriteView);
        spriteBox.setAlignment(Pos.CENTER);
        spriteBox.setSpacing(0);
        spriteBox.setPadding(Insets.EMPTY);

        Button nextCharBtn = createArrowButton("\u276F");
        nextCharBtn.setOnAction(e -> changeCharacter(1));

        HBox characterRow = new HBox(15, prevCharBtn, spriteBox, nextCharBtn);
        characterRow.setAlignment(Pos.CENTER);
        characterRow.setPadding(new Insets(16, 0, 16, 0));
        VBox.setMargin(characterRow, new Insets(-80, 0, 0, 0));

        // ── 3. Sliders with value labels and fill bars ───────
        for (int i = 0; i < CUSTOMER_COUNT; i++) {
            atSliders[i] = createSlider(MIN_ARRIVAL_TIME, MAX_ARRIVAL_TIME);
            btSliders[i] = createSlider(1, MAX_BURST_TIME);
            atValueLabels[i] = createValueLabel();
            btValueLabels[i] = createValueLabel();
            atFills[i] = createFillBar();
            btFills[i] = createFillBar();
        }

        // Attach listeners to update value labels and fill bars
        for (int i = 0; i < CUSTOMER_COUNT; i++) {
            final int idx = i;
            atSliders[i].valueProperty().addListener((obs, oldVal, newVal) -> {
                updateATLabel(idx);
                updateFillBar(atFills[idx], atSliders[idx]);
            });
            btSliders[i].valueProperty().addListener((obs, oldVal, newVal) -> {
                updateBTLabel(idx);
                updateFillBar(btFills[idx], btSliders[idx]);
            });
        }

        atRow = buildSliderRow("Arrival (AT):", atSliders[0], atValueLabels[0], atFills[0]);
        btRow = buildSliderRow("Patience (BT):", btSliders[0], btValueLabels[0], btFills[0]);

        // Initialize labels
        updateATLabel(0);
        updateBTLabel(0);
        updateFillBar(atFills[0], atSliders[0]);
        updateFillBar(btFills[0], btSliders[0]);

        // ── 4. Add button ────────────────────────────────────
        addBtn = new Button("Add");
        addBtn.getStyleClass().add("add-btn");
        addBtn.setOnAction(e -> addCustomer());

        // ── 5. Assemble ──────────────────────────────────────
        this.getChildren().addAll(header, characterRow, atRow, btRow, addBtn);
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("customer-card");

        updateTitle();
        loadCharacterSprite();
    }

    // ── Navigation ────────────────────────────────────────────

    /**
     * Advances to the next customer after the user clicks Add.
     * If all 6 customers have been added, the button is disabled.
     */
    private void addCustomer() {
        addedCount++;

        if (currentIndex < CUSTOMER_COUNT - 1) {
            currentIndex++;
            swapSliders();
            updateTitle();
            loadCharacterSprite();
        } else {
            addBtn.setDisable(true);
            addBtn.setText("Done");
        }
    }

    /** Returns how many customers have been confirmed via the Add button. */
    public int getAddedCount() {
        return addedCount;
    }

    /**
     * Changes the character type (GIRL ↔ MAN) for the current customer.
     *
     * @param direction -1 for previous character, +1 for next character
     */
    private void changeCharacter(int direction) {
        CharacterType current = characterTypes[currentIndex];
        characterTypes[currentIndex] = (current == CharacterType.GIRL)
                ? CharacterType.MAN
                : CharacterType.GIRL;
        loadCharacterSprite();
    }

    // ── Value display helpers ─────────────────────────────────

    /**
     * Converts an AT slider value (0–30) to a time string.
     * 0 = 7:00 A.M., 30 = 5:00 P.M. Each unit = 20 minutes.
     */
    private String formatATValue(int value) {
        int totalMinutes = 7 * 60 + value * 20; // start at 7:00 AM, 20 min per unit
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        String period = hours >= 12 ? "P.M." : "A.M.";
        int displayHour = hours > 12 ? hours - 12 : hours;
        return String.format("%d:%02d %s (%d)", displayHour, minutes, period, value);
    }

    /** Updates the AT value label for the given customer index. */
    private void updateATLabel(int index) {
        int value = (int) atSliders[index].getValue();
        atValueLabels[index].setText(formatATValue(value));
    }

    /** Updates the BT value label for the given customer index. */
    private void updateBTLabel(int index) {
        int value = (int) btSliders[index].getValue();
        btValueLabels[index].setText(String.valueOf(value));
    }

    /** Updates the fill bar width based on the slider's current value. */
    private void updateFillBar(Rectangle fill, Slider slider) {
        double progress = (slider.getValue() - slider.getMin()) / (slider.getMax() - slider.getMin());
        fill.widthProperty().bind(slider.widthProperty().multiply(progress));
    }

    // ── Reading values ────────────────────────────────────────

    /**
     * Gets the Arrival Time selected for a specific customer.
     *
     * @param index customer index (0–5, where 0 = Customer 1)
     * @return the slider value as a string
     */
    public String getArrivalTime(int index) {
        return String.valueOf((int) atSliders[index].getValue());
    }

    /**
     * Gets the Patience (Burst Time) selected for a specific customer.
     *
     * @param index customer index (0–5, where 0 = Customer 1)
     * @return the slider value as a string
     */
    public String getPatience(int index) {
        return String.valueOf((int) btSliders[index].getValue());
    }

    /**
     * Gets the character type chosen for a specific customer.
     *
     * @param index customer index (0–5, where 0 = Customer 1)
     * @return the chosen character type
     */
    public CharacterType getCharacterType(int index) {
        return characterTypes[index];
    }

    // ── Sprite animation ──────────────────────────────────────

    /**
     * Loads and starts the idle animation for the current character type.
     * Stops any existing animation before starting the new one.
     */
    private void loadCharacterSprite() {
        if (idleTimeline != null) {
            idleTimeline.stop();
        }

        CharacterType type = characterTypes[currentIndex];
        String spriteFile = (type == CharacterType.GIRL)
                ? "girl1_idle.png"
                : "man1_idle.png";

        Image spriteSheet = new Image(getClass().getResourceAsStream("/assets/textures/" + spriteFile));
        currentIdleFrames = parseFrames(spriteSheet, type == CharacterType.GIRL ? 9 : 6);
        currentFrameIndex = 0;

        if (currentIdleFrames.length > 0) {
            spriteView.setImage(currentIdleFrames[0]);
        }

        // Create animation timeline that cycles through frames
        idleTimeline = new Timeline(new KeyFrame(Duration.millis(333), e -> {
            currentFrameIndex = (currentFrameIndex + 1) % currentIdleFrames.length;
            spriteView.setImage(currentIdleFrames[currentFrameIndex]);
        }));
        idleTimeline.setCycleCount(Timeline.INDEFINITE);
        idleTimeline.play();
    }

    /**
     * Parses a horizontal sprite sheet into individual frames.
     *
     * @param spriteSheet the full spritesheet image
     * @param frameCount  the number of frames in the sheet
     * @return an array of WritableImage frames
     */
    private WritableImage[] parseFrames(Image spriteSheet, int frameCount) {
        WritableImage[] frames = new WritableImage[frameCount];
        PixelReader reader = spriteSheet.getPixelReader();

        for (int i = 0; i < frameCount; i++) {
            frames[i] = new WritableImage(reader, i * FRAME_WIDTH, 0, FRAME_WIDTH, FRAME_HEIGHT);
        }

        return frames;
    }

    // ── Internal helpers ──────────────────────────────────────

    /**
     * Swaps the sliders and labels in each row to show the current customer's controls.
     */
    private void swapSliders() {
        atRow.getChildren().set(1, atSliders[currentIndex]);
        atRow.getChildren().set(2, atValueLabels[currentIndex]);
        btRow.getChildren().set(1, btSliders[currentIndex]);
        btRow.getChildren().set(2, btValueLabels[currentIndex]);

        // Update labels to reflect the new customer's slider values
        updateATLabel(currentIndex);
        updateBTLabel(currentIndex);
    }

    /** Updates the header to show "Customer N". */
    private void updateTitle() {
        titleText.setText("Customer " + (currentIndex + 1));
    }

    /** Creates a styled arrow button for character navigation. */
    private Button createArrowButton(String symbol) {
        Button btn = new Button(symbol);
        btn.getStyleClass().add("arrow-btn");
        return btn;
    }

    /** Creates a text label for displaying the slider value with fixed width. */
    private Text createValueLabel() {
        Text label = new Text("00:00 A.M. (00)");
        label.getStyleClass().add("slider-value-label");
        label.setWrappingWidth(180);
        return label;
    }

    /** Creates a thin rectangle used as a visual fill bar on the slider. */
    private Rectangle createFillBar() {
        Rectangle rect = new Rectangle(0, 6);
        rect.setFill(SLIDER_FILL_COLOR);
        rect.setArcWidth(4);
        rect.setArcHeight(4);
        rect.setOpacity(0.35);
        return rect;
    }

    /**
     * Creates a slider with the given range, snapped to integer ticks.
     * No tick labels or tick marks — value shown separately.
     *
     * @param min the minimum value
     * @param max the maximum value
     * @return a configured Slider
     */
    private Slider createSlider(int min, int max) {
        Slider slider = new Slider(min, max, min);
        slider.setSnapToTicks(true);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setBlockIncrement(1);
        slider.setShowTickLabels(false);
        slider.setShowTickMarks(false);
        slider.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(slider, Priority.ALWAYS);
        slider.getStyleClass().add("input-slider");
        return slider;
    }

    /**
     * Builds a horizontal row with a label, slider, fill bar, and value display.
     *
     * @param label      the label text
     * @param slider     the slider control
     * @param valueLabel the text showing the current value
     * @param fill       the fill bar rectangle
     * @return an HBox containing the label, slider, and value
     */
    private HBox buildSliderRow(String label, Slider slider, Text valueLabel, Rectangle fill) {
        Text labelText = new Text(label);
        labelText.getStyleClass().add("input-label");

        // Stack the fill bar behind the slider
        Region sliderContainer = new Region();
        sliderContainer.getStyleClass().add("slider-container");
        HBox.setHgrow(sliderContainer, Priority.ALWAYS);

        // Use a simple layout: label | slider | value
        HBox row = new HBox(12, labelText, slider, valueLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("input-row");
        HBox.setHgrow(slider, Priority.ALWAYS);
        return row;
    }
}
