package com.orderup;

import java.util.HashSet;
import java.util.Set;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.orderup.Factory.CustomerFactory;
import com.orderup.Factory.CustomerFactory.CustomerType;
import com.orderup.Factory.MainSceneFactory;
import com.orderup.Factory.RhythmFactory;
import com.orderup.Factory.WaitingLineUIFactory;
import com.orderup.Handlers.AudioManager;
import com.orderup.Handlers.SceneManager;
import com.orderup.Models.CustomerProcess;
import com.orderup.Models.GameClock;
import com.orderup.Models.MenuItem;
import com.orderup.Models.ProcessDisplay;
import com.orderup.Models.ProcessQueue;
import com.orderup.Models.RhythmScore;
import com.orderup.Scenes.Components.CustomerAnimationComponent;
import com.orderup.Scenes.Interfaces.IntroInterface;
import com.orderup.Scenes.Interfaces.ManualScene;
import com.orderup.Scenes.Interfaces.WaitingLineScene;
import com.orderup.Uitility.LoadFont;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 * Main application class for OrderUp.
 * <br>
 * <br>
 * Extends FXGL's {@link GameApplication} to leverage the FXGL game engine.
 * Configures game settings including window dimensions, title, and the
 * custom {@link MainSceneFactory} for menu and loading scenes. Registers
 * entity factories via {@link #initFactory()} and selects the initial
 * in-game scene based on the {@link SceneType} flag.
 */
public class Application extends GameApplication {

    /** Game window width in pixels */
    private static final int WINDOW_WIDTH = 1280;

    /** Game window height in pixels */
    private static final int WINDOW_HEIGHT = 720;

    /** Application title */
    private static final String APP_TITLE = "Order Up";

    /** Application version */
    private static final String APP_VERSION = "1.0";

    /** The scene to show when the game starts */
    private static SceneType initialScene = SceneType.WAITING_LINE;

    /**
     * The process queue for the current game session, set by controllers before
     * starting.
     */
    private static ProcessQueue processQueue;

    /**
     * Original process list, never modified during gameplay. Used by the Gantt
     * overlay.
     */
    private static java.util.List<CustomerProcess> originalProcesses;

    /** Reference to the waiting line scene. */
    private static WaitingLineScene waitingLineScene;

    /**
     * The process display for the current game session, set in {@link #initGame()}
     * before starting.
     */
    private ProcessDisplay processDisplay;

    /** Current game-clock time in seconds, incremented each tick. */
    private GameClock gameClock = new GameClock();

    /**
     * Customer IDs that have already been spawned, prevents re-spawning on later
     * ticks.
     */
    private Set<Integer> spawnedIds = new HashSet<>();

    private int lastTick = -1;

    /** Customer ID the rhythm circle was spawned for (-1 = none active). */
    private int rhythmCustomerId = -1;

    /** Set when the pause menu requests the Gantt chart. */
    private static boolean ganttChartRequested = false;

    /** Requests the Gantt chart overlay to be shown over the game scene. */
    public static void requestGanttChart() {
        ganttChartRequested = true;
    }

    /**
     * Enum representing the different in-game scenes that can be
     * shown when the game starts.
     */
    public enum SceneType {
        WAITING_LINE,
        MANUAL
    }

    /**
     * Initializes the game settings including window size, title,
     * menu configuration, and the custom scene factory.
     *
     * @param settings the {@link GameSettings} to configure
     */
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(WINDOW_WIDTH);
        settings.setHeight(WINDOW_HEIGHT);
        settings.setTitle(APP_TITLE);
        settings.setVersion(APP_VERSION);
        settings.setIntroEnabled(true);
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
        settings.setSceneFactory(new MainSceneFactory());
    }

    /** Sets the initial scene to show when the game starts. */
    public static void setInitialScene(SceneType scene) {
        initialScene = scene;
    }

    /** Sets the process queue for the current game session. */
    public static void setProcessQueue(ProcessQueue queue) {
        processQueue = queue;
        originalProcesses = new java.util.ArrayList<>();
        for (CustomerProcess p : queue.getProcessList()) {
            originalProcesses.add(new CustomerProcess(p));
        }
    }

    /** Returns the original, unmodified process list. */
    public static java.util.List<CustomerProcess> getOriginalProcesses() {
        return originalProcesses;
    }

    /**
     * Restores the process queue to its original state (full burst times,
     * original list) and restarts the game, replaying the same day from 7:00 AM.
     */
    public static void resetDay() {
        if (processQueue == null) {
            return;
        }

        java.util.List<CustomerProcess> restored = new java.util.ArrayList<>();
        for (CustomerProcess p : originalProcesses) {
            restored.add(new CustomerProcess(p));
        }
        processQueue.setProcessList(restored);

        FXGL.getGameController().startNewGame();
    }

    /** Returns the process queue for the current game session. */
    public static ProcessQueue getProcessQueue() {
        return processQueue;
    }

    /** Returns the waiting line scene instance. */
    public static WaitingLineScene getWaitingLineScene() {
        return waitingLineScene;
    }

    /**
     * Registers the entity factories for the game world.
     * <br>
     * <br>
     * Adds {@link WaitingLineUIFactory} and {@link CustomerFactory} so
     * that entities can be spawned during gameplay.
     */
    protected void initFactory() {
        FXGL.getGameWorld().addEntityFactory(new WaitingLineUIFactory());
        FXGL.getGameWorld().addEntityFactory(new CustomerFactory());
        FXGL.getGameWorld().addEntityFactory(new RhythmFactory());
    }

    @Override
    protected void onPreInit() {
        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished(e -> AudioManager.playBackgroundMusic());
        delay.play();
    }

    /**
     * Initializes the game world when a new game starts.
     * <br>
     * <br>
     * Clears any existing UI nodes, registers entity factories via
     * {@link #initFactory()}, and shows the scene determined by
     * {@link #initialScene}.
     */
    @Override
    protected void initGame() {

        // Remove any existing UI nodes first
        try {
            java.util.List<Node> nodes = new java.util.ArrayList<>(
                    FXGL.getGameScene().getUINodes());
            for (Node node : nodes) {
                FXGL.getGameScene().removeUINode(node);
            }
        } catch (Exception e) {
            // Ignore if no nodes exist yet
        }

        spawnedIds.clear();
        gameClock.reset();
        RhythmScore.reset();
        initFactory();
        lastTick = -1;
        rhythmCustomerId = -1;
        ganttChartRequested = false;

        processDisplay = new ProcessDisplay(gameClock, 30);

        SceneManager.setGameClock(gameClock);

        switch (initialScene) {
            case MANUAL:
                SceneManager.show(new ManualScene());
                break;
            default:
                waitingLineScene = new WaitingLineScene();
                SceneManager.show(waitingLineScene);
                SceneManager.showClockUI();
                SceneManager.showScoreUI();
                Group queueDisplay = processDisplay.getDisplayGroup();
                queueDisplay.setTranslateX(50);
                queueDisplay.setTranslateY(510);
                FXGL.getGameScene().addUINode(queueDisplay);
                break;
        }
    }

    /**
     * Horizontal position where the waiting line / counter sits.
     * Customers move from off-screen right to this X.
     */
    private static final double TARGET_X = 600;

    /** Horizontal gap between consecutive customers in the line. */
    private static final double LINE_GAP = 150;

    /** Pixels per second that customers move toward their target. */
    private static final double MOVE_SPEED = 200;

    /** x axis where customers spawn from (right edge). */
    private static final double SPAWN_X = 1320;

    /**
     * Raw game-clock offset for the start of the simulation timeline.
     * The queue begins at 7:00 AM, which is 25,200 seconds after midnight.
     */
    private static final int SIMULATION_START_TIME_SECONDS = 7 * 60 * 60;

    /**
     * Number of raw game-clock seconds represented by one simulation tick.
     * This keeps the queue timeline aligned with the game's 20-minute tick
     * granularity.
     */
    private static final int TICK_DURATION_SECONDS = 20 * 60;

    /**
     * Spawns a single customer entity off-screen and records its target position.
     * <br>
     * <br>
     * The customer is placed at {@link #SPAWN_X} (right edge) and assigned a
     * target X in the waiting line. Each subsequent customer shifts right by
     * {@link #LINE_GAP} so they form a visible queue.
     *
     * @param process the customer process whose data drives this entity
     */
    private void spawnCustomer(CustomerProcess process) {
        int id = process.getCustomerId();
        int index = spawnedIds.size();
        double targetX = TARGET_X + (index * LINE_GAP);
        double targetY = (WINDOW_HEIGHT - 150) / 2.0;

        SpawnData data = new SpawnData(SPAWN_X, targetY);
        data.put("customerId", id);
        data.put("targetX", targetX);
        data.put("targetY", targetY);
        data.put("arrivalTime", process.getArrivalTime());
        data.put("burstTime", process.getBurstTime());
        data.put("characterType", process.getCharacterType());

        FXGL.spawn("customer", data);
        AudioManager.pop();
    }

    /** Finds the game-world entity matching the given customer ID, or null. */
    private Entity findCustomerEntity(int customerId) {
        for (Entity entity : FXGL.getGameWorld().getEntitiesByType(CustomerType.CUSTOMER)) {
            var entityId = entity.<Integer>getPropertyOptional("customerId");
            if (entityId.isPresent() && entityId.get() == customerId) {
                return entity;
            }
        }

        return null;
    }

    /**
     * Handles completion of a process whose burst time has reached zero.
     *
     * <p>
     * Removes the process from the queue, display, and game world,
     * then repositions remaining customers to fill the gap.
     * </p>
     */
    private void completeProcess(CustomerProcess process) {
        int customerId = process.getCustomerId();

        processQueue.getProcessList().remove(process);
        processDisplay.removeProcess(customerId);
        spawnedIds.remove(customerId);

        Entity entity = findCustomerEntity(customerId);
        if (entity != null) {
            entity.removeFromWorld();
        }

        // Destroy the rhythm circle together with the customer it belongs to.
        // A still-active circle means it expired unclicked — that breaks the combo.
        if (waitingLineScene != null) {
            if (waitingLineScene.removeRhythmCircle()) {
                RhythmScore.breakCombo();
            }
        }
        rhythmCustomerId = -1;

        repositionCustomers();

        // Day is done: fade in a "no more customers" message, then swap
        // to an earnings summary banner. The banner fades out on its own
        // and only afterwards is the Gantt chart revealed.
        //
        // Full sequence budget (chart lands at exactly 2.5s):
        // 0.00 banner 1 fades in (0.25)
        // 0.25 hold (0.70)
        // 0.95 banner 1 fades out (0.25)
        // 1.20 earnings fades in (0.25)
        // 1.45 hold (0.80)
        // 2.25 earnings fades out (0.25)
        // 2.50 Gantt chart shows
        if (processQueue.getProcessList().isEmpty() && waitingLineScene != null) {
            StackPane dayEndMessage = buildDayEndMessage(
                    "It looks like there will be no more customers coming...",
                    (WINDOW_HEIGHT - 150) / 2.0);
            FXGL.getGameScene().addUINode(dayEndMessage);

            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.25), dayEndMessage);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();

            FXGL.getGameTimer().runOnceAfter(() -> {
                gameClock.pause();

                // First banner out, earnings banner in, then the chart.
                fadeOutUiNode(dayEndMessage, () -> {
                    StackPane earningsMessage = buildDayEndMessage(
                            "I earned a total of "
                                    + RhythmScore.formatMoney(RhythmScore.getScore())
                                    + " today...",
                            (WINDOW_HEIGHT - 150) / 2.0);
                    FXGL.getGameScene().addUINode(earningsMessage);

                    FadeTransition earningsIn = new FadeTransition(
                            Duration.seconds(0.25), earningsMessage);
                    earningsIn.setFromValue(0.0);
                    earningsIn.setToValue(1.0);
                    // Hold the banner on its own so the total can be read,
                    // fade it out, and reveal the Gantt chart only once it
                    // is fully gone (completes the 2.5s budget above).
                    earningsIn.setOnFinished(e -> FXGL.getGameTimer().runOnceAfter(
                            () -> fadeOutUiNode(earningsMessage,
                                    () -> waitingLineScene
                                            .showGanttOverlay(originalProcesses, true)),
                            Duration.seconds(0.8)));
                    earningsIn.play();
                });
            }, Duration.seconds(0.95));
        }
    }

    /**
     * Fades a UI node out over 0.25s, removes it from the game scene,
     * then runs the given callback (if any).
     *
     * @param node  the node to fade out and remove
     * @param after callback to run once the node is gone, or null
     */
    private void fadeOutUiNode(StackPane node, Runnable after) {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.25), node);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            FXGL.getGameScene().removeUINode(node);
            if (after != null) {
                after.run();
            }
        });
        fadeOut.play();
    }

    /**
     * Builds a centered end-of-day banner with the given message.
     * Full-screen-width panel but mouse-transparent so it never blocks
     * input to the scene or the Gantt overlay underneath.
     *
     * @param message    the text to show
     * @param translateY vertical position of the panel's top edge
     */
    private StackPane buildDayEndMessage(String message, double translateY) {
        Text text = new Text(message);
        text.setFont(LoadFont.loadPixelFont(24));
        text.setFill(Color.WHITE);
        text.setLineSpacing(14);
        text.setWrappingWidth(840);
        text.setTextAlignment(TextAlignment.CENTER);

        Rectangle bg = new Rectangle(920, 150);
        bg.setArcWidth(20);
        bg.setArcHeight(20);
        bg.setFill(Color.rgb(0, 0, 0, 0.7));

        StackPane pane = new StackPane(bg, text);
        pane.setAlignment(Pos.CENTER);
        pane.setMouseTransparent(true);
        pane.setTranslateX((WINDOW_WIDTH - 920) / 2.0);
        pane.setTranslateY(translateY);
        // Start hidden BEFORE the node is added to the scene. A
        // FadeTransition's fromValue is not applied until the next
        // animation pulse, so without this the banner renders one frame
        // at full opacity first — visible as a "spawns twice" flash.
        pane.setOpacity(0.0);
        return pane;
    }

    /** Recalculates target X positions for all remaining customer entities. */
    private void repositionCustomers() {
        int index = 0;
        for (Entity entity : FXGL.getGameWorld().getEntitiesByType(CustomerType.CUSTOMER)) {
            entity.setProperty("targetX", TARGET_X + (index * LINE_GAP));
            index++;
        }
    }

    /**
     * Called every frame by FXGL. Moves all customer entities toward
     * their target X position and stops them when they arrive.
     */
    @Override
    protected void onUpdate(double tpf) {
        // guard this lmao, it bugged
        if (initialScene != SceneType.WAITING_LINE)
            return;

        // Deferred Gantt chart request from the pause menu — runs here so
        // the overlay is added after the game scene is visible again.
        if (ganttChartRequested) {
            ganttChartRequested = false;
            if (waitingLineScene != null && !waitingLineScene.isGanttOverlayVisible()) {
                waitingLineScene.showGanttOverlay(originalProcesses, false);
            }
        }

        // game clock
        gameClock.update();
        // spawn customers

        // Converts the raw game clock into simulation ticks using the documented
        // constants.
        var currentTick = (gameClock.getTime() - SIMULATION_START_TIME_SECONDS) / TICK_DURATION_SECONDS;

        var arrived = processQueue.getArrivedProcesses(currentTick);
        for (CustomerProcess process : arrived) {
            if (!spawnedIds.contains(process.getCustomerId())) {
                spawnCustomer(process);
                spawnedIds.add(process.getCustomerId());
            }
            if (!processDisplay.containsProcess(process.getCustomerId())) {
                processDisplay.addProcess(process);
            }
        }

        // The clock keeps running past 5:00 PM
        // so every tick is now delivered by the game clock at the same
        // 1-tick-per-second rate.
        boolean tickChanged = currentTick != lastTick;
        if (tickChanged) {
            lastTick = currentTick;

            arrived = processQueue.getArrivedProcesses(currentTick);
            if (!arrived.isEmpty()) {
                CustomerProcess front = arrived.get(0);
                Entity frontEntity = findCustomerEntity(front.getCustomerId());
                boolean atCounter = frontEntity != null
                        && frontEntity.<Boolean>getPropertyOptional("arrived").orElse(false);
                if (atCounter && !front.isBurstComplete()) {
                    front.setBurstTime(front.getBurstTime() - 1);
                    if (front.isBurstComplete()) {
                        completeProcess(front);
                    }
                }
            }
            processDisplay.update(currentTick);
        }

        // Rhythm Circle spawns if customer is on the counter only
        if (waitingLineScene != null && waitingLineScene.isRhythmDone()) {
            var arrivedFront = processQueue.getArrivedProcesses(currentTick);
            if (!arrivedFront.isEmpty()) {
                CustomerProcess front = arrivedFront.get(0);
                if (front.getCustomerId() != rhythmCustomerId) {
                    Entity frontEntity = findCustomerEntity(front.getCustomerId());
                    boolean atCounter = frontEntity != null
                            && frontEntity.<Boolean>getPropertyOptional("arrived").orElse(false);
                    if (atCounter) {
                        // The circle shows the dish this customer ordered.
                        MenuItem order = frontEntity != null
                                ? frontEntity.<MenuItem>getPropertyOptional("order").orElse(null)
                                : null;
                        AudioManager.playIntro(front.getCharacterType());
                        waitingLineScene.spawnRhythmCircle(front.getBurstTime(), order,
                                front.getCharacterType());
                        rhythmCustomerId = front.getCustomerId();
                    }
                }
            }
        }

        // Cap tpf to prevent large jumps during initialization lag
        double cappedTpf = Math.min(tpf, 1.0 / 60.0);
        double step = MOVE_SPEED * cappedTpf;

        for (Entity customer : FXGL.getGameWorld().getEntitiesByType(CustomerType.CUSTOMER)) {
            java.util.Optional<Double> targetOptX = customer.getPropertyOptional("targetX");
            java.util.Optional<Double> targetOptY = customer.getPropertyOptional("targetY");
            if (targetOptX.isPresent() && targetOptY.isPresent()) {
                double targetX = targetOptX.get();
                double targetY = targetOptY.get();
                double currentX = customer.getX();
                double currentY = customer.getY();

                boolean moving = false;

                if (currentX > targetX) {
                    customer.setX(Math.max(currentX - step, targetX));
                    moving = true;
                } else if (currentX < targetX) {
                    customer.setX(Math.min(currentX + step, targetX));
                    moving = true;
                }

                if (currentY > targetY) {
                    customer.setY(Math.max(currentY - step, targetY));
                    moving = true;
                } else if (currentY < targetY) {
                    customer.setY(Math.min(currentY + step, targetY));
                    moving = true;
                }

                if (!moving) {
                    customer.setProperty("arrived", true);
                }

                var anim = customer.getComponentOptional(CustomerAnimationComponent.class);
                if (anim.isPresent()) {
                    if (moving) {
                        anim.get().playWalk();
                    } else {
                        anim.get().playIdle();
                    }
                }
            }
        }
    }

    /**
     * Entry point for the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
