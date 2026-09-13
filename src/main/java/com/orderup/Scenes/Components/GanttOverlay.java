package com.orderup.Scenes.Components;

import java.util.List;

import com.almasb.fxgl.dsl.FXGL;
import com.orderup.Application;
import com.orderup.Models.CustomerProcess;
import com.orderup.Models.GanttCell;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
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
 * <p>Styled as a pixel art cafe menu board — layered wooden frame border,
 * chalkboard-like background, pixel art decorations, and chunky blocky elements
 * matching the warm autumnal pixel art aesthetic of the game.</p>
 *
 * <pre>
 *  ╔═══════════════════════════════════════════════════════╗
 *  ║  [☕]              GANTT CHART              [🍁]     ║
 *  ║─────────────────────────────────────────────────────║
 *  ║  0  1  2  3  4  5  6  7  8  9  10                   ║
 *  ║  |--C1--|--C2--|     |--C3--|--C3--|--C4--|          ║
 *  ║                                                     ║
 *  ║  Customer │  AT  │  BT  │  WT  │  TaT               ║
 *  ║     C1    │  0   │  3   │  0   │  3                  ║
 *  ║     C2    │  2   │  2   │  1   │  3                  ║
 *  ║  ─────────────────────────────────────               ║
 *  ║  AWT: 1.50     ATaT: 4.00                            ║
 *  ╚═══════════════════════════════════════════════════════╝
 * </pre>
 */
public class GanttOverlay extends Pane {

    // ── Colors (warm autumnal pixel art cafe palette) ─────────
    private static final Color GIRL_COLOR = Color.web("#CC5522");
    private static final Color GIRL_STROKE = Color.web("#993311");
    private static final Color MAN_COLOR = Color.web("#D9A45B");
    private static final Color MAN_STROKE = Color.web("#B8860B");
    private static final Color IDLE_COLOR = Color.web("#6B5D4F");
    private static final Color IDLE_STROKE = Color.web("#4A3F35");
    private static final Color TIMELINE_COLOR = Color.web("#8B7355");
    private static final Color HEADER_BG = Color.web("#3D2B1F");
    private static final Color ROW_EVEN = Color.web("#2A1C14");
    private static final Color ROW_ODD = Color.web("#352518");
    private static final Color TEXT_COLOR = Color.web("#FFF8E7");
    private static final Color ACCENT_COLOR = Color.web("#E8801A");
    private static final Color AVG_COLOR = Color.web("#FFB347");

    // Pixel art frame colors
    private static final Color FRAME_OUTER = Color.web("#2A1C14");
    private static final Color FRAME_MID = Color.web("#5C3A1E");
    private static final Color FRAME_INNER = Color.web("#D9A45B");
    private static final Color FRAME_HIGHLIGHT = Color.web("#FFF8E7");
    private static final Color CHALKBOARD = Color.web("#1A0F08");

    // ── Layout constants ────────────────────────────────────
    private static final double FRAME_L = 60;
    private static final double FRAME_T = 150;
    private static final double FRAME_R = 1220;
    private static final double FRAME_B = 570;

    // Centered content area within the frame
    private static final double CONTENT_WIDTH = 1100;
    private static final double CONTENT_LEFT = (FRAME_L + FRAME_R) / 2 - CONTENT_WIDTH / 2;
    private static final double CHART_LEFT = CONTENT_LEFT;
    private static final double CHART_WIDTH = CONTENT_WIDTH;
    private static final double ROW_H = 28;
    private static final double TICK_H = 12;
    private static final double BAR_H = 32;
    private static final double PIXEL = 8;
    private static final double OVERLAY_H = 720;
    private static final double TITLE_Y = FRAME_T + 45;
    private static final double SUBTITLE_Y = TITLE_Y + 22;
    private static final double DIVIDER_Y = SUBTITLE_Y + 15;
    private static final double AXIS_Y = DIVIDER_Y + 55;
    private static final double BAR_Y = AXIS_Y + 5;
    private static final double TABLE_TOP_OFFSET = BAR_Y + BAR_H + 35;
    private static final double TABLE_LEFT = CONTENT_LEFT;
    private static final double COL_W = CONTENT_WIDTH / 5;
    private static final double CELL_PAD = 8;
    private static final Font HEADER_FONT = Font.font("Monospace", FontWeight.BOLD, 14);
    private static final Font LABEL_FONT = Font.font("Monospace", FontWeight.NORMAL, 13);
    private static final Font METRIC_FONT = Font.font("Monospace", FontWeight.BOLD, 13);

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

        // Full-screen dark backdrop
        Rectangle backdrop = new Rectangle(1280, 720, Color.web("#000000BB"));

        // Pixel art frame
        Group frame = buildPixelFrame();

        // Title — large orange heading
        Text title = new Text("GANTT CHART");
        title.setFont(Font.font("Monospace", FontWeight.BOLD, 24));
        title.setFill(ACCENT_COLOR);
        title.setX((1280 - title.getBoundsInLocal().getWidth()) / 2);
        title.setY(TITLE_Y);

        // Subtitle
        Text subtitle = new Text("Order Results — FCFS Scheduling");
        subtitle.setFont(Font.font("Monospace", FontWeight.NORMAL, 12));
        subtitle.setFill(Color.web("#B89A6A"));
        subtitle.setX((1280 - subtitle.getBoundsInLocal().getWidth()) / 2);
        subtitle.setY(SUBTITLE_Y);

        // Decorative pixel divider
        Group divider = buildPixelDivider(200, DIVIDER_Y, 880);

        // Pixel art decorations — coffee cup and leaf
        Group coffeeCup = buildCoffeeCup(FRAME_L + 25, FRAME_T - 55);
        Group leaf = buildLeaf(FRAME_R - 68, FRAME_T - 55);

        // Close button — chunky pixel style with drop shadow
        Button closeBtn = new Button("\u2716");
        closeBtn.setFont(Font.font("Monospace", FontWeight.BOLD, 16));
        closeBtn.setTextFill(Color.web("#2A1C14"));
        closeBtn.setStyle(
                "-fx-background-color: #FFF8E7; " +
                "-fx-background-radius: 0; " +
                "-fx-min-width: 32; -fx-min-height: 32; " +
                "-fx-max-width: 32; -fx-max-height: 32; " +
                "-fx-border-color: #D9A45B; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 0; " +
                "-fx-cursor: hand;"
        );
        closeBtn.setOnMouseClicked(e -> onClose.run());
        closeBtn.setLayoutX(FRAME_R - 48);
        closeBtn.setLayoutY(FRAME_T + 12);

        // Drop shadow behind close button
        Rectangle btnShadow = new Rectangle(FRAME_R - 45, FRAME_T + 17, 32, 32);

        // Day-action buttons
        HBox actions = new HBox(30);
        actions.setAlignment(javafx.geometry.Pos.CENTER);

        Button continueBtn = new Button("Continue");
        continueBtn.setFont(Font.font("Monospace", FontWeight.BOLD, 20));
        continueBtn.setTextFill(Color.WHITE);
        continueBtn.setStyle("-fx-background-color: #cc5114; -fx-background-radius: 15; "
                + "-fx-border-color: #D9A45B; -fx-border-width: 2; -fx-border-radius: 15; "
                + "-fx-padding: 10 40 10 40; -fx-cursor: hand;");
        continueBtn.setOnMouseClicked(e -> Application.resetDay());

        Button mainMenuBtn = new Button("Back to Main Menu");
        mainMenuBtn.setFont(Font.font("Monospace", FontWeight.BOLD, 20));
        mainMenuBtn.setTextFill(Color.WHITE);
        mainMenuBtn.setStyle("-fx-background-color: #555555; -fx-background-radius: 15; "
                + "-fx-border-color: #999999; -fx-border-width: 2; -fx-border-radius: 15; "
                + "-fx-padding: 10 40 10 40; -fx-cursor: hand;");
        mainMenuBtn.setOnMouseClicked(e -> FXGL.getGameController().gotoMainMenu());

        actions.getChildren().addAll(continueBtn, mainMenuBtn);
        actions.setPrefWidth(1280);
        actions.setTranslateY(OVERLAY_H - 110);

        // Bar chart
        drawChart(cells);

        // Metrics table
        Group table = buildMetricsTable(cells, processes);

        this.getChildren().addAll(backdrop, frame, btnShadow, closeBtn,
                title, subtitle, divider, coffeeCup, leaf, chartGroup, table, actions);
    }

    // ══════════════════════════════════════════════════════════
    //  PIXEL ART FRAME
    // ══════════════════════════════════════════════════════════

    private Group buildPixelFrame() {
        Group frame = new Group();

        // Outermost border — dark wood
        frame.getChildren().add(createFrameRect(FRAME_L - 12, FRAME_T - 12,
                FRAME_R - FRAME_L + 24, FRAME_B - FRAME_T + 24,
                FRAME_OUTER, 0));

        // Mid border — warm wood
        frame.getChildren().add(createFrameRect(FRAME_L - 6, FRAME_T - 6,
                FRAME_R - FRAME_L + 12, FRAME_B - FRAME_T + 12,
                FRAME_MID, 0));

        // Inner border — amber trim
        frame.getChildren().add(createFrameRect(FRAME_L - 2, FRAME_T - 2,
                FRAME_R - FRAME_L + 4, FRAME_B - FRAME_T + 4,
                FRAME_INNER, 0));

        // Innermost edge highlight — cream
        frame.getChildren().add(createFrameRect(FRAME_L, FRAME_T,
                FRAME_R - FRAME_L, FRAME_B - FRAME_T,
                FRAME_HIGHLIGHT, 0));

        // Chalkboard background
        frame.getChildren().add(createFrameRect(FRAME_L + 4, FRAME_T + 4,
                FRAME_R - FRAME_L - 8, FRAME_B - FRAME_T - 8,
                CHALKBOARD, 0));

        return frame;
    }

    private Rectangle createFrameRect(double x, double y, double w, double h,
                                       Color fill, double arc) {
        Rectangle r = new Rectangle(x, y, w, h);
        r.setFill(fill);
        r.setArcWidth(arc);
        r.setArcHeight(arc);
        return r;
    }

    // ══════════════════════════════════════════════════════════
    //  PIXEL ART DECORATIONS
    // ══════════════════════════════════════════════════════════

    private Group buildCoffeeCup(double ox, double oy) {
        Group cup = new Group();

        // Cup body (filled brown)
        String[] body = {
            "..####..",
            ".######.",
            "########",
            "########",
            "########",
            "########",
            ".######.",
            ".#....#.",
        };

        // Handle (outline)
        String[] handle = {
            "...####.",
            "...#..#.",
            "...####.",
        };

        // Steam (wispy white)
        String[] steam = {
            "..#...#.",
            "...#.#..",
            "........",
        };

        // Steam wisps offset upward
        for (int row = 0; row < steam.length; row++) {
            for (int col = 0; col < steam[row].length(); col++) {
                if (steam[row].charAt(col) == '#') {
                    cup.getChildren().add(createPixel(
                            ox + col * PIXEL, oy - (steam.length - row) * PIXEL,
                            Color.web("#FFF8E788")));
                }
            }
        }

        // Cup body
        for (int row = 0; row < body.length; row++) {
            for (int col = 0; col < body[row].length(); col++) {
                if (body[row].charAt(col) == '#') {
                    cup.getChildren().add(createPixel(
                            ox + col * PIXEL, oy + row * PIXEL,
                            Color.web("#D9A45B")));
                }
            }
        }

        // Handle (offset right of cup)
        for (int row = 0; row < handle.length; row++) {
            for (int col = 0; col < handle[row].length(); col++) {
                if (handle[row].charAt(col) == '#') {
                    cup.getChildren().add(createPixel(
                            ox + (body[0].length() + col) * PIXEL,
                            oy + 1.5 * PIXEL + row * PIXEL,
                            Color.web("#D9A45B")));
                }
            }
        }

        return cup;
    }

    private Group buildLeaf(double ox, double oy) {
        Group leaf = new Group();

        String[] pattern = {
            "......#.",
            ".....##.",
            "....###.",
            "...####.",
            "..#####.",
            ".#####..",
            "####....",
            "#.......",
        };

        // Leaf fill
        for (int row = 0; row < pattern.length; row++) {
            for (int col = 0; col < pattern[row].length(); col++) {
                if (pattern[row].charAt(col) == '#') {
                    leaf.getChildren().add(createPixel(
                            ox + col * PIXEL, oy + row * PIXEL,
                            Color.web("#CC5522")));
                }
            }
        }

        // Lighter accent pixels for detail
        String[] detail = {
            "......#.",
            ".....#..",
            "....#...",
            "...#....",
            "..#.....",
            ".#......",
            "#.......",
            "........",
        };
        for (int row = 0; row < detail.length; row++) {
            for (int col = 0; col < detail[row].length(); col++) {
                if (detail[row].charAt(col) == '#') {
                    leaf.getChildren().add(createPixel(
                            ox + col * PIXEL, oy + row * PIXEL,
                            Color.web("#E8801A")));
                }
            }
        }

        return leaf;
    }

    private Rectangle createPixel(double x, double y, Color fill) {
        Rectangle r = new Rectangle(x, y, PIXEL, PIXEL);
        r.setFill(fill);
        return r;
    }

    private Group buildPixelDivider(double x, double y, double width) {
        Group divider = new Group();
        double dotSize = 4;
        double gap = 16;

        // Center line
        Line line = new Line(x, y + dotSize / 2, x + width, y + dotSize / 2);
        line.setStroke(Color.web("#5C3A1E"));
        line.setStrokeWidth(1);
        divider.getChildren().add(line);

        // Pixel dots along the line
        for (double dx = 0; dx < width; dx += gap) {
            Rectangle dot = new Rectangle(x + dx, y, dotSize, dotSize);
            divider.getChildren().add(dot);
        }

        return divider;
    }

    // ══════════════════════════════════════════════════════════
    //  CHART RENDERING
    // ══════════════════════════════════════════════════════════

    private void drawChart(List<GanttCell> cells) {
        if (cells.isEmpty()) return;

        int totalTime = cells.get(cells.size() - 1).getEndTime();
        if (totalTime <= 0) return;

        // Timeline axis
        Line axis = new Line(CHART_LEFT, BAR_Y, CHART_LEFT + CHART_WIDTH, BAR_Y);
        axis.setStroke(TIMELINE_COLOR);
        axis.setStrokeWidth(2);
        chartGroup.getChildren().add(axis);

        // Collect unique time points (AT/ET of each cell)
        java.util.TreeSet<Integer> timePoints = new java.util.TreeSet<>();
        timePoints.add(0);
        for (GanttCell cell : cells) {
            timePoints.add(cell.getStartTime());
            timePoints.add(cell.getEndTime());
        }

        // Tick marks and labels at AT/ET boundaries only
        for (int t : timePoints) {
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

        // Process and idle blocks — chunky pixel art style
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
                boolean isGirl = p.getCharacterType().isGirl();
                fillColor = isGirl ? GIRL_COLOR : MAN_COLOR;
                strokeColor = isGirl ? GIRL_STROKE : MAN_STROKE;
                label = "C" + p.getCustomerId();
            }

            Rectangle bar = new Rectangle(x, BAR_Y + 2, w, BAR_H);
            bar.setFill(fillColor);
            bar.setStroke(strokeColor);
            bar.setStrokeWidth(2);
            bar.setArcWidth(0);
            bar.setArcHeight(0);
            chartGroup.getChildren().add(bar);

            // Label inside the bar
            if (w > 30) {
                Text barLabel = new Text(label);
                barLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 12));
                barLabel.setFill(Color.web("#FFF8E7"));
                barLabel.setX(x + w / 2 - barLabel.getBoundsInLocal().getWidth() / 2);
                barLabel.setY(BAR_Y + 2 + BAR_H / 2 + 4);
                chartGroup.getChildren().add(barLabel);
            }
        }
    }

    // ══════════════════════════════════════════════════════════
    //  METRICS TABLE
    // ══════════════════════════════════════════════════════════

    private Group buildMetricsTable(List<GanttCell> cells, List<CustomerProcess> processes) {
        Group table = new Group();
        double tableTop = TABLE_TOP_OFFSET;

        // Table header background
        Rectangle headerBg = new Rectangle(TABLE_LEFT, tableTop - 5,
                CONTENT_WIDTH, ROW_H);
        headerBg.setFill(HEADER_BG);
        headerBg.setArcWidth(0);
        headerBg.setArcHeight(0);
        table.getChildren().add(headerBg);

        // Column headers — warm orange
        String[] headers = {"Customer", "AT", "BT", "WT", "TaT"};
        for (int i = 0; i < headers.length; i++) {
            Text h = new Text(headers[i]);
            h.setFont(HEADER_FONT);
            h.setFill(ACCENT_COLOR);
            h.setX(TABLE_LEFT + i * COL_W + CELL_PAD);
            h.setY(tableTop + ROW_H / 2 + 4);
            table.getChildren().add(h);
        }

        // Data rows
        double y = tableTop + ROW_H + 5;
        double totalWT = 0;
        double totalTaT = 0;
        int processCount = 0;

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

            int originalBT = endTime - startTime;
            int wt = p.getWaitingTime(startTime);
            int tat = endTime - p.getArrivalTime();

            totalWT += wt;
            totalTaT += tat;
            processCount++;

            // Row background
            Rectangle rowBg = new Rectangle(TABLE_LEFT, y - 5,
                    CONTENT_WIDTH, ROW_H);
            rowBg.setFill(i % 2 == 0 ? ROW_EVEN : ROW_ODD);
            rowBg.setArcWidth(0);
            rowBg.setArcHeight(0);
            table.getChildren().add(rowBg);

            // Row data
            Object[] row = {"C" + p.getCustomerId(), p.getArrivalTime(), originalBT, wt, tat};
            for (int j = 0; j < row.length; j++) {
                Text cell = new Text(String.valueOf(row[j]));
                cell.setFont(LABEL_FONT);
                cell.setFill(TEXT_COLOR);
                cell.setX(TABLE_LEFT + j * COL_W + CELL_PAD);
                cell.setY(y + ROW_H / 2 + 4);
                table.getChildren().add(cell);
            }

            y += ROW_H;
        }

        // Separator line
        if (processCount > 0) {
            Line sep = new Line(TABLE_LEFT, y, TABLE_LEFT + CONTENT_WIDTH, y);
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
            awtLabel.setY(y + ROW_H / 2 + 4);
            table.getChildren().add(awtLabel);

            Text atatLabel = new Text(String.format("ATaT: %.2f", atat));
            atatLabel.setFont(METRIC_FONT);
            atatLabel.setFill(AVG_COLOR);
            atatLabel.setX(TABLE_LEFT + 4 * COL_W);
            atatLabel.setY(y + ROW_H / 2 + 4);
            table.getChildren().add(atatLabel);
        }

        return table;
    }
}
