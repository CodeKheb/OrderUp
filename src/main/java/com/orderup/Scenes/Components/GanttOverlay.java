package com.orderup.Scenes.Components;

import java.util.List;

import com.orderup.Models.CustomerProcess;
import com.orderup.Models.CustomerProcess.CharacterType;
import com.orderup.Models.GanttCell;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Overlay component that displays the FCFS Gantt chart and scheduling metrics.
 *
 * <p>Renders a visual bar chart with colored blocks for each process (gray for idle),
 * a timeline axis with tick marks, and a metrics table showing Waiting Time (WT),
 * Turnaround Time (TaT), and their averages (AWT, ATaT).</p>
 *
 * <pre>
 * ┌──────────────────────────────────────────────────────┐
 * │                 GANTT CHART                          │
 * │  0  1  2  3  4  5  6  7  8  9  10                   │
 * │  |--C1--|--C2--|     |--C3--|--C3--|--C4--|          │
 * │                                                      │
 * │  Customer │  AT  │  BT  │  WT  │  TaT               │
 * │     C1    │  0   │  3   │  0   │  3                  │
 * │     C2    │  2   │  2   │  1   │  3                  │
 * │     C3    │  4   │  4   │  1   │  5                  │
 * │     C4    │  5   │  1   │  4   │  5                  │
 * │  ─────────────────────────────────────               │
 * │  AWT: 1.50     ATaT: 4.00                            │
 * └──────────────────────────────────────────────────────┘
 * </pre>
 */
public class GanttOverlay extends Pane {

    // ── Colors ──────────────────────────────────────────────
    private static final Color GIRL_COLOR = Color.web("#E88DA0");
    private static final Color GIRL_STROKE = Color.web("#C06070");
    private static final Color MAN_COLOR = Color.web("#6CB4C8");
    private static final Color MAN_STROKE = Color.web("#4A8A9E");
    private static final Color IDLE_COLOR = Color.web("#CCCCCC");
    private static final Color IDLE_STROKE = Color.web("#999999");
    private static final Color TIMELINE_COLOR = Color.web("#555555");
    private static final Color HEADER_BG = Color.web("#2A2A2A");
    private static final Color ROW_EVEN = Color.web("#1E1E1E");
    private static final Color ROW_ODD = Color.web("#252525");
    private static final Color TEXT_COLOR = Color.web("#DDDDDD");
    private static final Color ACCENT_COLOR = Color.web("#E88DA0");
    private static final Color AVG_COLOR = Color.web("#FFD700");

    // ── Layout constants ────────────────────────────────────
    private static final double CHART_LEFT = 40;
    private static final double CHART_RIGHT = 40;
    private static final double CHART_WIDTH = 1280 - CHART_LEFT - CHART_RIGHT;
    private static final double ROW_H = 28;
    private static final double TICK_H = 12;
    private static final double BAR_H = 30;
    private static final double OVERLAY_H = 720;
    private static final double CONTENT_HEIGHT = 330; // approx height of chart + table
    private static final double VERTICAL_OFFSET = (OVERLAY_H - CONTENT_HEIGHT) / 2 - 30;
    private static final double BAR_Y = VERTICAL_OFFSET + 35;
    private static final double TABLE_TOP_OFFSET = VERTICAL_OFFSET + 105;
    private static final double COL_W = CHART_WIDTH / 5;
    private static final double TABLE_LEFT = CHART_LEFT;
    private static final Font HEADER_FONT = Font.font("Monospace", FontWeight.BOLD, 15);
    private static final Font LABEL_FONT = Font.font("Monospace", FontWeight.NORMAL, 13);
    private static final Font METRIC_FONT = Font.font("Monospace", FontWeight.BOLD, 14);

    private final Group chartGroup = new Group();

    /** Callback invoked when the close button is clicked. */
    private final Runnable onClose;

    /**
     * Creates the Gantt overlay with the given cells and original process list.
     *
     * @param cells     the Gantt chart cells (processes + idle gaps)
     * @param processes the original process list (for AT/BT values)
     * @param onClose   callback to close the overlay
     */
    public GanttOverlay(List<GanttCell> cells, List<CustomerProcess> processes, Runnable onClose) {
        this.onClose = onClose;
        this.getStyleClass().add("gantt-overlay");

        // Semi-transparent dark background
        Rectangle bg = new Rectangle(1280, 720, Color.web("#000000CC"));
        bg.setStroke(Color.web("#555555"));
        bg.setStrokeWidth(1);
        bg.setArcWidth(12);
        bg.setArcHeight(12);

        // Title
        Text title = new Text("GANTT CHART");
        title.setFont(Font.font("Monospace", FontWeight.BOLD, 22));
        title.setFill(ACCENT_COLOR);
        title.setX(40);
        title.setY(VERTICAL_OFFSET);

        // Close button
        Button closeBtn = new Button("X");
        closeBtn.setFont(Font.font("Monospace", FontWeight.BOLD, 16));
        closeBtn.setTextFill(Color.WHITE);
        closeBtn.setStyle("-fx-background-color: #555555; -fx-background-radius: 20; "
                + "-fx-min-width: 36; -fx-min-height: 36; -fx-max-width: 36; -fx-max-height: 36; "
                + "-fx-cursor: hand;");
        closeBtn.setOnMouseClicked(e -> onClose.run());
        closeBtn.setLayoutX(1280 - 70);
        closeBtn.setLayoutY(VERTICAL_OFFSET - 20);

        // Bar chart
        drawChart(cells, processes);

        // Metrics table
        Group table = buildMetricsTable(cells, processes);

        this.getChildren().addAll(bg, title, closeBtn, chartGroup, table);
    }

    // ── Chart rendering ─────────────────────────────────────

    private void drawChart(List<GanttCell> cells, List<CustomerProcess> processes) {
        if (cells.isEmpty()) return;

        int totalTime = cells.get(cells.size() - 1).getEndTime();
        if (totalTime <= 0) return;

        // Timeline axis
        Line axis = new Line(CHART_LEFT, BAR_Y, CHART_LEFT + CHART_WIDTH, BAR_Y);
        axis.setStroke(TIMELINE_COLOR);
        axis.setStrokeWidth(2);
        chartGroup.getChildren().add(axis);

        // Tick marks and labels (labels above the axis)
        for (int t = 0; t <= totalTime; t++) {
            double x = CHART_LEFT + ((double) t / totalTime) * CHART_WIDTH;

            Line tick = new Line(x, BAR_Y - TICK_H / 2, x, BAR_Y + TICK_H / 2);
            tick.setStroke(TIMELINE_COLOR);
            tick.setStrokeWidth(1);
            chartGroup.getChildren().add(tick);

            Text label = new Text(String.valueOf(t));
            label.setFont(LABEL_FONT);
            label.setFill(TEXT_COLOR);
            label.setX(x - 4);
            label.setY(BAR_Y - TICK_H / 2 - 4);
            chartGroup.getChildren().add(label);
        }

        // Process and idle blocks
        for (GanttCell cell : cells) {
            double x = CHART_LEFT + ((double) cell.getStartTime() / totalTime) * CHART_WIDTH;
            double w = ((double) (cell.getEndTime() - cell.getStartTime()) / totalTime) * CHART_WIDTH;

            Color fillColor;
            Color strokeColor;
            String label;

            if (cell.getProcessId() == null) {
                fillColor = IDLE_COLOR;
                strokeColor = IDLE_STROKE;
                label = "IDLE";
            } else {
                CustomerProcess p = cell.getProcessId();
                boolean isGirl = p.getCharacterType() == CharacterType.GIRL;
                fillColor = isGirl ? GIRL_COLOR : MAN_COLOR;
                strokeColor = isGirl ? GIRL_STROKE : MAN_STROKE;
                label = "C" + p.getCustomerId();
            }

            Rectangle bar = new Rectangle(x, BAR_Y + 2, w, BAR_H);
            bar.setFill(fillColor);
            bar.setStroke(strokeColor);
            bar.setStrokeWidth(1.5);
            bar.setArcWidth(4);
            bar.setArcHeight(4);
            chartGroup.getChildren().add(bar);

            // Label inside the bar (only if wide enough)
            if (w > 30) {
                Text barLabel = new Text(label);
                barLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 12));
                barLabel.setFill(Color.WHITE);
                barLabel.setX(x + w / 2 - barLabel.getBoundsInLocal().getWidth() / 2);
                barLabel.setY(BAR_Y + 2 + BAR_H / 2 + 4);
                chartGroup.getChildren().add(barLabel);
            }
        }
    }

    // ── Metrics table ───────────────────────────────────────

    private Group buildMetricsTable(List<GanttCell> cells, List<CustomerProcess> processes) {
        Group table = new Group();
        double tableTop = TABLE_TOP_OFFSET;

        // Table header background
        Rectangle headerBg = new Rectangle(TABLE_LEFT - 10, tableTop - 5, CHART_WIDTH + 20, ROW_H);
        headerBg.setFill(HEADER_BG);
        headerBg.setArcWidth(4);
        headerBg.setArcHeight(4);
        table.getChildren().add(headerBg);

        // Column headers
        String[] headers = {"Customer", "AT", "BT", "WT", "TaT"};
        for (int i = 0; i < headers.length; i++) {
            Text h = new Text(headers[i]);
            h.setFont(HEADER_FONT);
            h.setFill(ACCENT_COLOR);
            h.setX(TABLE_LEFT + i * COL_W);
            h.setY(tableTop + ROW_H - 8);
            table.getChildren().add(h);
        }

        // Data rows
        double y = tableTop + ROW_H + 5;
        double totalWT = 0;
        double totalTaT = 0;
        int processCount = 0;

        // Build a lookup: customerId → (startTime, endTime) from the GanttCells
        // We need the ORIGINAL burst time, not the decremented one
        java.util.Map<Integer, int[]> schedule = new java.util.HashMap<>();
        for (GanttCell cell : cells) {
            if (cell.getProcessId() != null) {
                int id = cell.getProcessId().getCustomerId();
                schedule.put(id, new int[]{cell.getStartTime(), cell.getEndTime()});
            }
        }

        for (int i = 0; i < processes.size(); i++) {
            CustomerProcess p = processes.get(i);
            int[] times = schedule.get(p.getCustomerId());
            if (times == null) continue;

            int startTime = times[0];
            int endTime = times[1];

            // Use original BT from the process list (not the decremented value)
            int originalBT = endTime - startTime;
            int wt = p.getWaitingTime(startTime);
            int tat = endTime - p.getArrivalTime();

            totalWT += wt;
            totalTaT += tat;
            processCount++;

            // Row background
            Rectangle rowBg = new Rectangle(TABLE_LEFT - 10, y - 5, CHART_WIDTH + 20, ROW_H);
            rowBg.setFill(i % 2 == 0 ? ROW_EVEN : ROW_ODD);
            rowBg.setArcWidth(3);
            rowBg.setArcHeight(3);
            table.getChildren().add(rowBg);

            // Row data
            Object[] row = {"C" + p.getCustomerId(), p.getArrivalTime(), originalBT, wt, tat};
            for (int j = 0; j < row.length; j++) {
                Text cell = new Text(String.valueOf(row[j]));
                cell.setFont(LABEL_FONT);
                cell.setFill(TEXT_COLOR);
                cell.setX(TABLE_LEFT + j * COL_W);
                cell.setY(y + ROW_H - 8);
                table.getChildren().add(cell);
            }

            y += ROW_H;
        }

        // Separator line
        if (processCount > 0) {
            Line sep = new Line(TABLE_LEFT - 10, y, TABLE_LEFT + CHART_WIDTH + 10, y);
            sep.setStroke(TIMELINE_COLOR);
            sep.setStrokeWidth(1);
            table.getChildren().add(sep);
        }

        // Averages
        y += 5;
        if (processCount > 0) {
            double awt = totalWT / processCount;
            double atat = totalTaT / processCount;

            Text awtLabel = new Text(String.format("AWT: %.2f", awt));
            awtLabel.setFont(METRIC_FONT);
            awtLabel.setFill(AVG_COLOR);
            awtLabel.setX(TABLE_LEFT + 3 * COL_W);
            awtLabel.setY(y + ROW_H - 5);
            table.getChildren().add(awtLabel);

            Text atatLabel = new Text(String.format("ATaT: %.2f", atat));
            atatLabel.setFont(METRIC_FONT);
            atatLabel.setFill(AVG_COLOR);
            atatLabel.setX(TABLE_LEFT + 4 * COL_W);
            atatLabel.setY(y + ROW_H - 5);
            table.getChildren().add(atatLabel);
        }

        return table;
    }
}
