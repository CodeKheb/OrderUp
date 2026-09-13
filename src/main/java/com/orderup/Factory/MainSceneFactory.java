package com.orderup.Factory;

import java.io.IOException;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.IntroScene;
import com.almasb.fxgl.app.scene.LoadingScene;
import com.almasb.fxgl.app.scene.StartupScene;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.orderup.Scenes.Interfaces.IntroInterface;
import com.orderup.Scenes.Interfaces.LoadingInterface;
import com.orderup.Scenes.Interfaces.StartupInterface;
import com.orderup.Scenes.Interfaces.MenuInterface;
import com.orderup.Scenes.Interfaces.PauseInterface;

/**
 * Custom SceneFactory that overrides FXGL's default menu scenes.
 * <br><br>
 * This factory provides custom implementations for the main menu,
 * game (pause) menu, and loading scene, allowing full control over
 * the look and feel of each scene using FXML layouts.
 */
public class MainSceneFactory extends SceneFactory {

    /**
     * Creates the main menu scene.
     *
     * @return a new {@link MenuInterface} instance
     */
    @Override
    public FXGLMenu newMainMenu() {
        try {
            return new MenuInterface();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates the game (pause) menu scene.
     *
     * @return a new {@link PauseInterface} instance
     */
    @Override
    public FXGLMenu newGameMenu() {
        try {
            return new PauseInterface();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates the loading scene.
     *
     * @return a new {@link LoadingInterface} instance
     */
    @Override
    public LoadingScene newLoadingScene() {
        return new LoadingInterface();
    }

    /**
     * Creates the intro scene shown once at launch, replacing FXGL's
     * default starfield logo animation with the OrderUp loading art.
     *
     * @return a new {@link IntroInterface} instance
     */
    @Override
    public IntroScene newIntro() {
        return new IntroInterface();
    }

    /**
     * Creates the startup scene shown while the engine boots, replacing
     * FXGL's default blue splash with the OrderUp loading art.
     *
     * @return a new {@link StartupInterface} instance
     */
    @Override
    public StartupScene newStartup(int w, int h) {
        return new StartupInterface(w, h);
    }
}
