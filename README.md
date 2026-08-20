# Order Up

A 2D scheduling algorithm restaurant game built with FXGL (JavaFX). Customers are processes with Arrival Time and Burst Time, simulating a First Come First Serve scheduling algorithm.

## Tech Stack

- Java 21
- FXGL 21.1
- Maven

## Project Structure

```
src/main/java/com/orderup/
├── Application.java                  # Entry point. Extends GameApplication. initSettings configures
│                                     #   window size, title, menus, and MainSceneFactory. initGame
│                                     #   shows the initial in-game scene based on SceneType flag.
│
├── Handlers/
│   ├── MainSceneFactory.java         # Extends SceneFactory. Overrides newMainMenu(), newGameMenu(),
│   │                                 #   and newLoadingScene() to return custom FXGLMenu/LoadingScene.
│   └── SceneManager.java             # Utility to swap UI nodes on FXGL's GameScene. Call
│                                     #   SceneManager.show() to switch in-game scenes.
│
└── Scenes/
    ├── Controllers/                  # FXML controllers handling button actions.
    │   ├── MenuController.java       # Play → starts game (WAITING_LINE), Manual → starts game
    │   │                             #   (MANUAL), Quit → exit. Uses setMenu() pattern.
    │   ├── PauseController.java      # Resume → fireResume(), Main Menu → exitToMainMenu().
    │   ├── ManualController.java     # Back → gotoMainMenu().
    │   ├── WaitingLineController.java# Take Order → OrderScene via SceneManager.
    │   └── OrderController.java      # Back → WaitingLineScene via SceneManager.
    │
    └── Interfaces/                   # Scene views and FXGL menu classes.
        ├── MenuInterface.java        # Extends FXGLMenu (MAIN_MENU). Loads menu.fxml.
        ├── PauseInterface.java       # Extends FXGLMenu (GAME_MENU). Loads pause.fxml.
        │                             #   Has resume() and exitToMainMenu() with confirmation dialog.
        ├── LoadingInterface.java     # Extends LoadingScene. Shows "Loading..." text.
        ├── WaitingLineScene.java     # In-game VBox scene. Loads waitingline.fxml.
        ├── OrderScene.java           # In-game VBox scene. Loads order.fxml.
        └── ManualScene.java          # In-game VBox scene. Loads manual.fxml.

src/main/resources/
├── scenes/
│   ├── menu.fxml                     # Main menu: Play, Manual, Quit buttons.
│   ├── pause.fxml                    # Pause menu: Resume, Main Menu buttons.
│   ├── waitingline.fxml              # Waiting line: Take Order button.
│   ├── order.fxml                    # Order scene: Back to Waiting Line button.
│   └── manual.fxml                   # Manual scene: Back to Menu button.
├── stylesheets/
│   └── stylesheet.css                # Global styles for text, buttons, and layout.
└── assets/
    └── .gitkeep                      # Placeholder for future game assets.
```

## Scene Navigation

```
FXGL Main Menu (MenuInterface)
  ├── Play ──────────► Game starts → WaitingLineScene ──► OrderScene
  ├── Manual ────────► Game starts → ManualScene              │
  └── Quit ──────────► Exit game                  (back to WaitingLine)

FXGL Pause Menu (PauseInterface) [press ESC during game]
  ├── Resume ────────► Return to game
  └── Main Menu ─────► gotoMainMenu()
```

## FXGL Integration

- **SceneFactory**: `MainSceneFactory` overrides FXGL's default scenes to provide custom menu, pause, and loading screens.
- **FXGLMenu**: `MenuInterface` and `PauseInterface` extend `FXGLMenu` to integrate with FXGL's built-in menu system.
- **LoadingScene**: `LoadingInterface` extends `LoadingScene` for a custom loading screen.
- **GameApplication**: `Application` extends `GameApplication` and uses `initGame()` to set up in-game scenes.
- **SceneType flag**: A static enum controls which scene `initGame()` displays, set before `startNewGame()`.

## How to Run

```bash
mvn javafx:run
```

## CI

GitHub Actions workflow runs `mvn compile` on push/PR to verify the build compiles successfully.
