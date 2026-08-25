package com.orderup;

import java.util.HashSet;
import java.util.Set;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.orderup.Factory.MainSceneFactory;
import com.orderup.Factory.OrderStationUI;
import com.orderup.Factory.WaitingLineUIFactory;
import com.orderup.Handlers.SceneManager;
import com.orderup.Models.CustomerProcess;
import com.orderup.Models.ProcessQueue;
import com.orderup.Scenes.Interfaces.ManualScene;
import com.orderup.Scenes.Interfaces.WaitingLineScene;

import javafx.scene.Node;
import javafx.util.Duration;

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
    private static int gameClock = 0;

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
     * Adds {@link WaitingLineUIFactory} and {@link OrderStationUI} so
     * that entities can be spawned during gameplay.
     */
    protected void initFactory() {
        FXGL.getGameWorld().addEntityFactory(new WaitingLineUIFactory());
        FXGL.getGameWorld().addEntityFactory(new OrderStationUI());
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

        initFactory();

        switch (initialScene) {
            case MANUAL:
                SceneManager.show(new ManualScene());
                break;
            default:
                SceneManager.show(new WaitingLineScene());
                startGameClock();
                break;
        }
    }

    /**
     * Starts the game clock, ticking once per second.
     *
     * <p>Each tick increments {@link #gameClock}, queries the {@link ProcessQueue}
     * for processes that have arrived, and spawns an FXGL entity for each new
     * customer. The {@link #spawnedIds} set ensures each customer is spawned only once.</p>
     */
    private void startGameClock() {
        FXGL.getGameTimer().runAtInterval(() -> {
            gameClock++;
            var arrived = processQueue.getArrivedProcesses(gameClock);

            for (CustomerProcess process : arrived) {
                if (!spawnedIds.contains(process.getCustomerId())) {
                    // TODO: spawn FXGL entity for this customer
                    spawnedIds.add(process.getCustomerId());
                }
            }
        }, Duration.seconds(1));
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
