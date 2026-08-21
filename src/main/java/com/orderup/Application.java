package com.orderup;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.orderup.Factory.WaitingLineUIFactory;
import com.orderup.Handlers.MainSceneFactory;
import com.orderup.Handlers.SceneManager;
import com.orderup.Scenes.Interfaces.ManualScene;
import com.orderup.Scenes.Interfaces.WaitingLineScene;

import javafx.scene.Node;

/**
 * Main application class for OrderUp.
 * <br><br>
 * Extends FXGL's {@link GameApplication} to leverage the FXGL game engine.
 * Configures game settings including window dimensions, title, and the
 * custom {@link MainSceneFactory} for menu and loading scenes.
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
     * Enum representing the different in-game scenes that can be
     * shown when the game starts.
     */
    public enum SceneType {
        WAITING_LINE,
        ORDER,
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

    /**
     * Sets the initial scene to show when the game starts.
     *
     * @param scene the {@link SceneType} to display
     */
    public static void setInitialScene(SceneType scene) {
        initialScene = scene;
    }

    protected void initFactory() {
        FXGL.getGameWorld().addEntityFactory(new WaitingLineUIFactory());
    }

    /**
     * Initializes the game world when a new game starts.
     * <br><br>
     * Shows the scene determined by {@link #initialScene}.
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
            case ORDER:
                SceneManager.show(new com.orderup.Scenes.Interfaces.OrderScene());
                break;
            case MANUAL:
                SceneManager.show(new ManualScene());
                break;
            case WAITING_LINE:
            default:
                SceneManager.show(new WaitingLineScene());
                break;
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
