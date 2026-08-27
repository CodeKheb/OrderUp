# Order Up

A 2D scheduling algorithm restaurant game built with FXGL (JavaFX). Customers are processes with Arrival Time and Burst Time, simulating a First Come First Serve scheduling algorithm.

## Tech Stack

- Java 25
- FXGL 21.1
- Maven

## Project Structure

```
src/main/java/com/orderup/
├── Application.java                  # Entry point. Extends GameApplication. initSettings configures
│                                     #   window size, title, menus, and MainSceneFactory. initGame
│                                     #   shows the initial in-game scene based on SceneType flag.
│                                     #   initFactory() registers entity factories for the game world.
│                                     #   onUpdate() moves customer entities toward their target position.
│
├── Handlers/
│   ├── SceneManager.java             # Utility to swap UI nodes on FXGL's GameScene. Call
│   │                                 #   SceneManager.show() to switch in-game scenes.
│   └── ClickHandler.java             # Handles click events on game entities (e.g. order button).
│
├── Factory/
│   ├── MainSceneFactory.java         # Extends SceneFactory. Overrides newMainMenu(), newGameMenu(),
│   │                                 #   and newLoadingScene() to return custom FXGLMenu/LoadingScene.
│   ├── WaitingLineUIFactory.java     # EntityFactory for the waiting line scene. Spawns interactive
│   │                                 #   order_button, waiting_line, and order_list entities with
│   │                                 #   click handlers and textures.
│   ├── CustomerFactory.java          # EntityFactory for customer entities. Spawns colored placeholder
│   │                                 #   rectangles with properties (customerId, arrivalTime, burstTime).
│   └── ProcessGenerator.java         # Creates CustomerProcess instances — random generation for Play
│                                     #   mode, user-provided values for Manual mode.
│
├── Models/
│   ├── CustomerProcess.java          # Data model for a single customer/order. Holds customer ID,
│   │                                 #   arrival time, and burst time. Provides scheduling helpers
│   │                                 #   (isReadyAt, getCompletionTime, getWaitingTime).
│   ├── ProcessQueue.java             # Manages the list of CustomerProcess for a game session.
│   │                                 #   Queries arrived processes each game-clock tick.
│   ├── GanttChart.java               # Generates a First Come First Serve (FCFS) Gantt chart from a
│   │                                 #   list of customer processes. Produces ordered GanttCells.
│   └── GanttCell.java                # A single block on the Gantt chart — maps a CustomerProcess to
│                                     #   a time interval [startTime, endTime).
│
└── Scenes/
    ├── Controllers/                  # FXML controllers handling button actions.
    │   ├── MenuController.java       # Play → starts game (WAITING_LINE), Manual → starts game
    │   │                             #   (MANUAL), Quit → exit. Uses setMenu() pattern.
    │   ├── PauseController.java      # Resume → fireResume(), Main Menu → exitToMainMenu().
    │   └── ManualController.java     # Back → gotoMainMenu().
    │
    ├── Components/                   # Reusable JavaFX UI components.
    │   ├── CustomerCard.java         # Input card for Manual mode. Shows a stickman with arrows,
    │   │                             #   AT/BT input fields, and an Add button to advance through
    │   │                             #   up to 6 customers.
    │   └── StickmanFigure.java       # Simple stick-figure drawn with JavaFX shapes (circle head,
    │                                 #   lines for torso, arms, and legs).
    │
    └── Interfaces/                   # Scene views and FXGL menu classes.
        ├── MenuInterface.java        # Extends FXGLMenu (MAIN_MENU). Loads menu.fxml.
        ├── PauseInterface.java       # Extends FXGLMenu (GAME_MENU). Loads pause.fxml.
        │                             #   Has resume() and exitToMainMenu() with confirmation dialog.
        ├── LoadingInterface.java     # Extends LoadingScene. Shows "Loading..." text.
        ├── WaitingLineScene.java     # In-game Pane scene. Spawns FXGL entities (order_button,
        │                             #   waiting_line, order_list) instead of loading FXML.
        └── ManualScene.java          # In-game VBox scene. Loads manual.fxml.

src/main/resources/
├── scenes/
│   ├── menu.fxml                     # Main menu: Play, Manual, Quit buttons.
│   ├── pause.fxml                    # Pause menu: Resume, Main Menu buttons.
│   └── manual.fxml                   # Manual scene: Back to Menu button.
├── stylesheets/
│   ├── stylesheet.css                # Global styles for text, buttons, and layout.
│   └── manual.css                    # Styles specific to the Manual mode input screen.
└── assets/
    └── textures/                     # Game entity textures.
        ├── order_button.png
        ├── waiting_line.png
        ├── order_list.png
        ├── counter.png
        └── stove.png
```

## Scene Navigation

```
FXGL Main Menu (MenuInterface)
  ├── Play ──────────► Game starts → WaitingLineScene (FXGL entities)
  ├── Manual ────────► Game starts → ManualScene (CustomerCard input)
  └── Quit ──────────► Exit game

FXGL Pause Menu (PauseInterface) [press ESC during game]
  ├── Resume ────────► Return to game
  └── Main Menu ─────► gotoMainMenu()
```

## FXGL Integration

- **SceneFactory**: `MainSceneFactory` overrides FXGL's default scenes to provide custom menu, pause, and loading screens.
- **FXGLMenu**: `MenuInterface` and `PauseInterface` extend `FXGLMenu` to integrate with FXGL's built-in menu system.
- **LoadingScene**: `LoadingInterface` extends `LoadingScene` for a custom loading screen.
- **EntityFactory**: `WaitingLineUIFactory` and `CustomerFactory` implement `EntityFactory` to define game entities (buttons, textures, interactive objects, customer placeholders) that are spawned into the game world.
- **GameApplication**: `Application` extends `GameApplication` and uses `initGame()` to set up in-game scenes, `initFactory()` to register entity factories, and `onUpdate()` to animate customer movement.
- **ClickHandler**: Static utility that handles entity click events.
- **SceneType flag**: A static enum controls which scene `initGame()` displays, set before `startNewGame()`.

## How to Run

```bash
mvn javafx:run
```

## CI

GitHub Actions workflow runs `mvn compile` on push/PR to verify the build compiles successfully.
