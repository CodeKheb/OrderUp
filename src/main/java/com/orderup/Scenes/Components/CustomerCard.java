package com.orderup.Scenes.Components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * A self-contained card that displays one customer's details.
 * <br><br>
 * The card looks roughly like this:
 * <pre>
 * ┌─────────────────┐
 * │ &lt;  Customer 1  &gt; │   ← header with arrows
 * │       O          │   ← stickman figure
 * │      /|\         │
 * │     / | \        │
 * │ Arrival (AT): []  │   ← input field
 * │ Patience (BT): [] │   ← input field
 * └─────────────────┘
 * </pre>
 */
public class CustomerCard extends VBox {

    private static final int CUSTOMER_COUNT = 6;

    private int currentIndex = 0;

    /** The "Customer N" text in the header — changes when you navigate */
    private final Text titleText = new Text();

    /** One text field per customer for Arrival Time */
    private final TextField[] atFields = new TextField[CUSTOMER_COUNT];

    /** One text field per customer for Patience (Burst Time) */
    private final TextField[] btFields = new TextField[CUSTOMER_COUNT];

    // rows that hold the input fields
    private final HBox atRow;
    private final HBox btRow;

    /**
     * Builds the entire card: header, stickman, and input fields.
     */
    public CustomerCard() {
        // ── 1. Build the header: < Customer N > ───────────────
        // The header is a horizontal row with three items:
        //   [<]   [Customer 1]   [>]
        // Clicking < or > navigates to the previous/next customer.

        Button prevBtn = createArrowButton("\u276E");  // ❮  left arrow
        prevBtn.setOnAction(e -> navigateCustomer(-1));

        titleText.getStyleClass().add("customer-header-text");

        Button nextBtn = createArrowButton("\u276F");  // ❯  right arrow
        nextBtn.setOnAction(e -> navigateCustomer(1));

        HBox header = new HBox(10, prevBtn, titleText, nextBtn);
        header.setAlignment(Pos.CENTER);
        header.getStyleClass().add("customer-header");

        // ── 2. Build the stickman figure ──────────────────────
        VBox stickmanBox = new VBox(StickmanFigure.create());
        stickmanBox.setAlignment(Pos.CENTER);

        // ── 3. Build the input fields ─────────────────────────
        for (int i = 0; i < CUSTOMER_COUNT; i++) {
            atFields[i] = createInputField();
            btFields[i] = createInputField();
        }

        // Build the two visible rows with the first customer's fields
        atRow = buildInputRow("Arrival (AT):", atFields[0]);
        btRow = buildInputRow("Patience (BT):", btFields[0]);

        // ── 4. Put it all together ────────────────────────────
        this.getChildren().addAll(header, stickmanBox, atRow, btRow);
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("customer-card");

        // Show the first customer's name
        updateTitle();
    }

    // ── Navigation ────────────────────────────────────────────

    /**
     * Moves to the next or previous customer and updates the display.
     */
    public void navigateCustomer(int direction) {
        // The modulo arithmetic here handles the wrap-around:
        //   (0 - 1 + 6) % 6 = 5  (wraps to last customer)
        //   (5 + 1 + 6) % 6 = 0  (wraps to first customer)
        currentIndex = (currentIndex + direction + CUSTOMER_COUNT) % CUSTOMER_COUNT;
        swapInputFields();
        updateTitle();
    }

    // ── Reading values ────────────────────────────────────────

    /**
     * Gets the Arrival Time entered for a specific customer.
     *
     * @param index customer index (0–5, where 0 = Customer 1)
     * @return the typed value, or "0" if the field is empty
     */
    public String getArrivalTime(int index) {
        return readField(atFields[index]);
    }

    /**
     * Gets the Patience (Burst Time) entered for a specific customer.
     *
     * @param index customer index (0–5, where 0 = Customer 1)
     * @return the typed value, or "0" if the field is empty
     */
    public String getPatience(int index) {
        return readField(btFields[index]);
    }

    // ── Internal helpers ──────────────────────────────────────

    /**
     * Swaps the TextFields inside the visible rows to show the
     * current customer's fields.
     * <br><br>
     * JavaFX lets you replace a child in a layout by setting the
     * list index directly. The old field stays alive (holding its
     * typed value) but is no longer displayed.
     *
     * <p>Children layout: 0=header, 1=stickmanBox, 2=atRow, 3=btRow</p>
     */
    private void swapInputFields() {
        // Replace index 1 (the TextField) in each row
        atRow.getChildren().set(1, atFields[currentIndex]);
        btRow.getChildren().set(1, btFields[currentIndex]);
    }

    /** Updates the "Customer N" header text to match the current index. */
    private void updateTitle() {
        titleText.setText("Customer " + (currentIndex + 1));
    }

    /**
     * Creates a styled navigation arrow button.
     *
     * @param symbol the arrow character (e.g. "❮" or "❯")
     * @return a ready-to-use Button
     */
    private Button createArrowButton(String symbol) {
        Button btn = new Button(symbol);
        btn.getStyleClass().add("arrow-btn");
        return btn;
    }

    /**
     * Creates a styled, empty text input field.
     * <br><br>
     * The prompt text ("0") shows as grey placeholder text when
     * the field is empty, hinting at what the user should type.
     *
     * @return a new TextField with the "input-field" CSS style
     */
    private TextField createInputField() {
        TextField field = new TextField();
        field.getStyleClass().add("input-field");
        field.setPromptText("0");
        return field;
    }

    /**
     * Builds a horizontal row with a label and a text field.
     * <br><br>
     * Example output:
     * <pre>
     * [Arrival (AT):] [_____]
     * </pre>
     *
     * @param label the label text (e.g. "Arrival (AT):")
     * @param field the TextField to display next to it
     * @return an HBox (horizontal layout) containing both
     */
    private HBox buildInputRow(String label, TextField field) {
        Text labelText = new Text(label);
        labelText.getStyleClass().add("input-label");

        HBox row = new HBox(8, labelText, field);  // 8px gap between them
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("input-row");
        return row;
    }

    /**
     * Safely reads the text from a field, returning "0" if empty.
     * <br><br>
     * We default to "0" because the scheduling algorithm needs a
     * number even if the user didn't type anything.
     *
     * @param field the TextField to read
     * @return trimmed text, or "0" if blank/null
     */
    private String readField(TextField field) {
        String value = field.getText();
        return (value == null || value.isBlank()) ? "0" : value.trim();
    }
}
