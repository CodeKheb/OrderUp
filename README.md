# Order Up

A 2D scheduling algorithm restaurant game built with FXGL (JavaFX). Customers are processes with Arrival Time and Burst Time, simulating a First Come First Serve scheduling algorithm.

## Tech Stack

- Java 17
- FXGL 21.1
- Maven
- Jackson 2.18.6 (JSON processing)

## Project Structure

```
src/main/java/com/orderup/
├── Application.java                  # Entry point. Extends GameApplication. initSettings configures
│                                     #   window size, title, menus, and MainSceneFactory. initGame
│                                     #   shows the initial in-game scene based on SceneType flag.
│                                     #   initFactory() registers entity factories for the game world.
│                                     #   onUpdate() drives the game loop: updates the game clock,
│                                     #   spawns customer entities based on arrival times, decrements
│                                     #   burst times, animates customer movement, and handles process
│                                     #   completion with entity removal and queue repositioning.
│
├── Handlers/
│   ├── SceneManager.java             # Utility to swap UI nodes on FXGL's GameScene. Call
│   │                                 #   SceneManager.show() to switch in-game scenes. Also manages
│   │                                 #   the clock UI overlay via showClockUI() and setGameClock().
│   └── ClickHandler.java             # Handles click events on game entities (e.g. order button).
│
├── Factory/
│   ├── MainSceneFactory.java         # Extends SceneFactory. Overrides newMainMenu(), newGameMenu(),
│   │                                 #   and newLoadingScene() to return custom FXGLMenu/LoadingScene.
│   ├── WaitingLineUIFactory.java     # EntityFactory for the waiting line scene. Spawns interactive
│   │                                 #   order_button, waiting_line, order_list, and background
│   │                                 #   entities with click handlers and textures.
│   ├── CustomerFactory.java          # EntityFactory for customer entities. Spawns animated sprite
│   │                                 #   entities with CustomerAnimationComponent, collision boxes,
│   │                                 #   and scheduling properties (customerId, arrivalTime, burstTime).
│   └── ProcessGenerator.java         # Creates CustomerProcess instances — random generation for Play
│                                     #   mode, user-provided values for Manual mode. Supports optional
│                                     #   CharacterType parameter for Manual mode.
│
├── Models/
│   ├── CustomerProcess.java          # Data model for a single customer/order. Holds customer ID,
│   │                                 #   arrival time, burst time, and CharacterType (GIRL/MAN).
│   │                                 #   Provides scheduling helpers (isReadyAt, getCompletionTime,
│   │                                 #   getWaitingTime, isBurstComplete). Implements Comparable.
│   ├── ProcessQueue.java             # Manages the list of CustomerProcess for a game session.
│   │                                 #   Queries arrived processes each game-clock tick.
│   ├── GanttChart.java               # Generates a First Come First Serve (FCFS) Gantt chart from a
│   │                                 #   list of customer processes. Produces ordered GanttCells.
│   ├── GanttCell.java                # A single block on the Gantt chart — maps a CustomerProcess to
│   │                                 #   a time interval [startTime, endTime).
│   ├── GameClock.java                # Real-time game clock using System.nanoTime() for consistent
│   │                                 #   ticking. Starts at 7:00 AM and advances 20 minutes per
│   │                                 #   wall-clock second. Exposes an IntegerProperty for JavaFX
│   │                                 #   binding and a Text node for display.
│   └── ProcessDisplay.java           # Text-based FCFS scheduling queue display. Shows arrived
│                                     #   processes with readable AM clock times and patience values.
│                                     #   Refreshes each game-clock tick and integrates with GameClock.
│
└── Scenes/
    ├── Controllers/                  # FXML controllers handling button actions.
    │   ├── MenuController.java       # Play → starts game (WAITING_LINE) with random processes,
    │   │                             #   Manual → starts game (MANUAL), Quit → exit.
    │   ├── PauseController.java      # Resume → fireResume(), Main Menu → exitToMainMenu().
    │   └── ManualController.java     # Back → gotoMainMenu(), Play → reads CustomerCard inputs
    │                                 #   and starts game (WAITING_LINE) with user-provided processes.
    │
    ├── Components/                   # Reusable JavaFX UI components.
    │   ├── CustomerCard.java         # Input card for Manual mode. Shows an animated character sprite
    │   │                             #   with arrow buttons for character selection, AT/BT sliders
    │   │                             #   with value labels, and an Add button to advance through
    │   │                             #   up to 6 customers.
    │   ├── CustomerAnimationComponent.java  # FXGL Component that manages sprite animation for
    │   │                             #   customer entities. Determines character variant (male/female)
    │   │                             #   based on customer ID, sets up idle and walking animation
    │   │                             #   channels, and provides playIdle()/playWalk() controls.
    │   └── StickmanFigure.java       # Simple stick-figure drawn with JavaFX shapes (circle head,
    │                                 #   lines for torso, arms, and legs).
    │
    └── Interfaces/                   # Scene views and FXGL menu classes.
        ├── MenuInterface.java        # Extends FXGLMenu (MAIN_MENU). Loads menu.fxml. Also provides
        │                             #   openManualWindow() for popup display.
        ├── PauseInterface.java       # Extends FXGLMenu (GAME_MENU). Loads pause.fxml.
        │                             #   Has resume() and exitToMainMenu() with confirmation dialog.
        ├── LoadingInterface.java     # Extends LoadingScene. Shows "Loading..." text with dark background.
        ├── WaitingLineScene.java     # In-game Pane scene. Spawns FXGL entities (background,
        │                             #   order_button, waiting_line, order_list) instead of FXML.
        └── ManualScene.java          # In-game VBox scene. Loads manual.fxml with CustomerCard input.

src/main/resources/
├── scenes/
│   ├── menu.fxml                     # Main menu: Play, Manual, Quit buttons.
│   ├── pause.fxml                    # Pause menu: Resume, Main Menu buttons.
│   └── manual.fxml                   # Manual scene: Back to Menu, CustomerCard, Play buttons.
├── stylesheets/
│   ├── stylesheet.css                # Global styles for text, buttons, and layout.
│   └── manual.css                    # Styles specific to the Manual mode input screen.
└── assets/
    └── textures/                     # Game entity and character textures.
        ├── order_button.png          # Interactive order button entity.
        ├── waiting_line.png          # Waiting line background entity.
        ├── order_list.png            # Order list entity.
        ├── girl1_idle.png            # Girl character idle spritesheet (9 frames).
        ├── girl1_walk.png            # Girl character walk spritesheet (12 frames).
        ├── man1_idle.png             # Man character idle spritesheet (6 frames).
        └── man1_walk.png             # Man character walk spritesheet (10 frames).
```

## Scene Navigation

```
FXGL Main Menu (MenuInterface)
  ├── Play ──────────► Random processes generated → Game starts → WaitingLineScene
  │                    (game clock, animated sprites, queue display)
  ├── Manual ────────► Game starts → ManualScene (CustomerCard input)
  │                    └── Play ──► User-provided processes → WaitingLineScene
  └── Quit ──────────► Exit game

FXGL Pause Menu (PauseInterface) [press ESC during game]
  ├── Resume ────────► Return to game
  └── Main Menu ─────► gotoMainMenu() with confirmation dialog
```

## FXGL Integration

- **SceneFactory**: `MainSceneFactory` overrides FXGL's default scenes to provide custom menu, pause, and loading screens.
- **FXGLMenu**: `MenuInterface` and `PauseInterface` extend `FXGLMenu` to integrate with FXGL's built-in menu system.
- **LoadingScene**: `LoadingInterface` extends `LoadingScene` for a custom loading screen.
- **EntityFactory**: `WaitingLineUIFactory` and `CustomerFactory` implement `EntityFactory` to define game entities (buttons, textures, interactive objects, animated customer sprites) that are spawned into the game world.
- **Component**: `CustomerAnimationComponent` extends `Component` to manage sprite animation (idle/walk channels) for customer entities, with gender-based character variants.
- **GameApplication**: `Application` extends `GameApplication` and uses `initGame()` to set up in-game scenes, `initFactory()` to register entity factories, and `onUpdate()` for the main game loop (clock updates, customer spawning/movement, burst time processing, and process completion).
- **ClickHandler**: Static utility that handles entity click events.
- **SceneType flag**: A static enum controls which scene `initGame()` displays, set before `startNewGame()`.

## How to Run

```bash
mvn javafx:run
```

## CI

GitHub Actions workflow runs `mvn compile` on push/PR to verify the build compiles successfully.
