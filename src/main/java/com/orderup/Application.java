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
import com.orderup.Factory.WaitingLineUIFactory;
import com.orderup.Handlers.SceneManager;
import com.orderup.Models.CustomerProcess;
import com.orderup.Models.GameClock;
import com.orderup.Models.ProcessQueue;
import com.orderup.Scenes.Interfaces.ManualScene;
import com.orderup.Scenes.Interfaces.WaitingLineScene;

import javafx.scene.Node;

/**
 * Main application class for OrderUp.
 * <br><br>
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

    /** The process queue for the current game session, set by controllers before starting. */
    private static ProcessQueue processQueue;

    /** Current game-clock time in seconds, incremented each tick. */
    private GameClock gameClock = new GameClock();

    /** Customer IDs that have already been spawned, prevents re-spawning on later ticks. */
    private Set<Integer> spawnedIds = new HashSet<>();

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
    }

    /** Returns the process queue for the current game session. */
    public static ProcessQueue getProcessQueue() {
        return processQueue;
    }

    /**
     * Registers the entity factories for the game world.
     * <br><br>
     * Adds {@link WaitingLineUIFactory} and {@link CustomerFactory} so
     * that entities can be spawned during gameplay.
     */
    protected void initFactory() {
        FXGL.getGameWorld().addEntityFactory(new WaitingLineUIFactory());
        FXGL.getGameWorld().addEntityFactory(new CustomerFactory());
    }

    /**
     * Initializes the game world when a new game starts.
     * <br><br>
     * Clears any existing UI nodes, registers entity factories via
     * {@link #initFactory()}, and shows the scene determined by
     * {@link #initialScene}.
     */
    @Override
    protected void initGame() {
        // Remove any existing UI nodes first
        try {
            java.util.List<Node> nodes = new java.util.ArrayList<>(
                FXGL.getGameScene().getUINodes()
            );
            for (Node node : nodes) {
                FXGL.getGameScene().removeUINode(node);
            }
        } catch (Exception e) {
            // Ignore if no nodes exist yet
        }

        spawnedIds.clear();
        gameClock.reset();
        initFactory();

        SceneManager.setGameClock(gameClock);

        switch (initialScene) {
            case MANUAL:
                SceneManager.show(new ManualScene());
                break;
            default:
                SceneManager.show(new WaitingLineScene());
                SceneManager.showClockUI();
                break;
        }
    }

    /**
     * Horizontal position where the waiting line / counter sits.
     * Customers move from off-screen right to this X.
     */
    private static final double TARGET_X = 150;

    /** Horizontal gap between consecutive customers in the line. */
    private static final double LINE_GAP = 90;

    /** Pixels per second that customers move toward their target. */
    private static final double MOVE_SPEED = 200;

    /** x axis where customers spawn from (right edge). */
    private static final double SPAWN_X = 1050;


    /**
     * Spawns a single customer entity off-screen and records its target position.
     * <br><br>
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
        double targetY = (WINDOW_HEIGHT - 70) / 2.0;

        SpawnData data = new SpawnData(SPAWN_X, targetY);
        data.put("customerId", id);
        data.put("targetX", targetX);
        data.put("targetY", targetY);
        data.put("arrivalTime", process.getArrivalTime());
        data.put("burstTime", process.getBurstTime());

        FXGL.spawn("customer", data);
    }

    /**
     * Called every frame by FXGL. Moves all customer entities toward
     * their target X position and stops them when they arrive.
     */
    @Override
    protected void onUpdate(double tpf) {
        // guard this lmao, it bugged
    if (initialScene != SceneType.WAITING_LINE) return;
        // game clock 
        gameClock.update();
        // spawn customers

        //Syncs/converts raw gameclock value to seconds-based ticks for AT
        var currentTick = (gameClock.getTime() - 25200) / 1200;

        var arrived = processQueue.getArrivedProcesses(currentTick);
        for (CustomerProcess process : arrived) {
            if (!spawnedIds.contains(process.getCustomerId())) {
                spawnCustomer(process);
                spawnedIds.add(process.getCustomerId());
            }
        }

        double step = MOVE_SPEED * tpf;

        for (Entity customer : FXGL.getGameWorld().getEntitiesByType(CustomerType.CUSTOMER)) {
            java.util.Optional<Double> targetOptX = customer.getPropertyOptional("targetX");
            java.util.Optional<Double> targetOptY = customer.getPropertyOptional("targetY");
            if (targetOptX.isPresent() && targetOptY.isPresent()) {
                double targetX = targetOptX.get();
                double targetY = targetOptY.get();
                double currentX = customer.getX();
                double currentY = customer.getY();

                if (currentX > targetX) {
                    customer.setX(Math.max(currentX - step, targetX));
                } else if (currentX < targetX) {
                    customer.setX(Math.min(currentX + step, targetX));
                }

                if (currentY > targetY) {
                    customer.setY(Math.max(currentY - step, targetY));
                } else if (currentY < targetY) {
                    customer.setY(Math.min(currentY + step, targetY));
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
