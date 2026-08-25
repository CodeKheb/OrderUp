package com.orderup.Scenes.Components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * A card for inputting one customer's arrival and burst time (Manual mode).
 *
 * <p>Shows a stickman with arrow buttons on either side (for changing
 * the character's sprite), two input fields (AT and BT), and an Add
 * button that confirms the entry and advances to the next customer.</p>
 *
 * <pre>
 * ┌─────────────────┐
 * │   Customer 1     │
 * │ [<]  O  [>]      │   ← arrows change sprite
 * │      /|\         │
 * │     / | \        │
 * │ Arrival (AT): [] │
 * │ Patience (BT):[] │
 * │      [Add]       │   ← confirms & advances
 * └─────────────────┘
 * </pre>
 */
public class CustomerCard extends VBox {

    private static final int CUSTOMER_COUNT = 6;

    /** Index of the customer currently being edited (0–5). */
    private int currentIndex = 0;

    /** Number of customers the user has confirmed via the Add button. */
    private int addedCount = 0;

    /** Displays "Customer N" in the header — updates as the user advances. */
    private final Text titleText = new Text();

    /** One text field per customer for Arrival Time. */
    private final TextField[] atFields = new TextField[CUSTOMER_COUNT];

    /** One text field per customer for Patience (Burst Time). */
    private final TextField[] btFields = new TextField[CUSTOMER_COUNT];

    /** Horizontal rows holding the label + text field for AT and BT. */
    private final HBox atRow;
    private final HBox btRow;

    /** Button to confirm the current customer and advance to the next. */
    private final Button addBtn;

    public CustomerCard() {
        // ── 1. Header: just the title ────────────────────────
        titleText.getStyleClass().add("customer-header-text");

        HBox header = new HBox(titleText);
        header.setAlignment(Pos.CENTER);
        header.getStyleClass().add("customer-header");

        // ── 2. Stickman with arrows on either side ───────────
        // Arrows change the character's sprite, not the customer index.
        Button prevSpriteBtn = createArrowButton("\u276E");
        prevSpriteBtn.setOnAction(e -> changeSprite(-1));

        VBox stickmanBox = new VBox(StickmanFigure.create());
        stickmanBox.setAlignment(Pos.CENTER);

        Button nextSpriteBtn = createArrowButton("\u276F");
        nextSpriteBtn.setOnAction(e -> changeSprite(1));

        HBox characterRow = new HBox(10, prevSpriteBtn, stickmanBox, nextSpriteBtn);
        characterRow.setAlignment(Pos.CENTER);

        // ── 3. Input fields ──────────────────────────────────
        for (int i = 0; i < CUSTOMER_COUNT; i++) {
            atFields[i] = createInputField();
            btFields[i] = createInputField();
        }

        atRow = buildInputRow("Arrival (AT):", atFields[0]);
        btRow = buildInputRow("Patience (BT):", btFields[0]);

        // ── 4. Add button ────────────────────────────────────
        addBtn = new Button("Add");
        addBtn.getStyleClass().add("add-btn");
        addBtn.setOnAction(e -> addCustomer());

        // ── 5. Assemble ──────────────────────────────────────
        this.getChildren().addAll(header, characterRow, atRow, btRow, addBtn);
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("customer-card");

        updateTitle();
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
            swapInputFields();
            updateTitle();
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
     * Changes the character's sprite aesthetic (placeholder for future sprites).
     * Called by the left/right arrows flanking the stickman.
     *
     * @param direction -1 for previous sprite, +1 for next sprite
     */
    private void changeSprite(int direction) {
        // TODO: implement sprite cycling when sprites are added
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
     * Swaps the TextFields in each row to show the current customer's fields.
     * Previous fields keep their values but are no longer displayed.
     */
    private void swapInputFields() {
        atRow.getChildren().set(1, atFields[currentIndex]);
        btRow.getChildren().set(1, btFields[currentIndex]);
    }

    /** Updates the header to show "Customer N". */
    private void updateTitle() {
        titleText.setText("Customer " + (currentIndex + 1));
    }

    /** Creates a styled arrow button for sprite navigation. */
    private Button createArrowButton(String symbol) {
        Button btn = new Button(symbol);
        btn.getStyleClass().add("arrow-btn");
        return btn;
    }

    /** Creates an empty text input field with a placeholder. */
    private TextField createInputField() {
        TextField field = new TextField();
        field.getStyleClass().add("input-field");
        field.setPromptText("0");
        return field;
    }

    /** Builds a horizontal row with a label and a text field. */
    private HBox buildInputRow(String label, TextField field) {
        Text labelText = new Text(label);
        labelText.getStyleClass().add("input-label");

        HBox row = new HBox(8, labelText, field);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("input-row");
        return row;
    }

    /** Reads text from a field, returning "0" if empty. */
    private String readField(TextField field) {
        String value = field.getText();
        return (value == null || value.isBlank()) ? "0" : value.trim();
    }
}
