# Order Up

A 2D restaurant management game built with FXGL (JavaFX) that visualizes CPU scheduling algorithms through a restaurant. Customers arrive as processes with Arrival Time (AT) and Burst Time (BT), simulating a First Come First Serve (FCFS) scheduling algorithm. The game also includes a rhythm minigame where players click to serve customers at the right time.

## Tech Stack

- Java 21 
- FXGL 21.1 (game engine built on JavaFX)
- Maven
- JavaFX 21.0.6 (media for audio support)

## Project Structure

```
src/main/java/com/orderup/
├── Application.java                  # Entry point. Extends GameApplication. initSettings configures
│                                     #   window size (1280x720), title ("Order Up"), version (1.0),
│                                     #   menus, and MainSceneFactory. initGame shows the initial
│                                     #   in-game scene based on SceneType flag. initFactory() registers
│                                     #   entity factories (WaitingLineUIFactory, CustomerFactory,
│                                     #   RhythmFactory) for the game world.
│                                     #   onUpdate() drives the game loop: updates the game clock,
│                                     #   spawns customer entities based on arrival times, decrements
│                                     #   burst times, animates customer movement, manages rhythm circle
│                                     #   spawning, handles process completion with entity removal and
│                                     #   queue repositioning, and shows Gantt overlay when all customers
│                                     #   are served.
│
├── Handlers/
│   ├── SceneManager.java             # Utility to swap UI nodes on FXGL's GameScene. Call
│   │                                 #   SceneManager.show() to switch in-game scenes. Also manages
│   │                                 #   the clock UI overlay via showClockUI() and setGameClock().
│   ├── ClickHandler.java             # Handles click events on game entities (order button, rhythm
│   │                                 #   circles). Calculates click accuracy, awards score points,
│   │                                 #   triggers rhythm game completion, and plays sound effects.
│   ├── EffectsHandler.java           # Spawns visual effects for rhythm interactions: burst ring
│   │                                 #   (gold, on successful click) and expire fade (red, on missed
│   │                                 #   circle). Uses JavaFX animations (ScaleTransition, FadeTransition).
│   └── AudioManager.java             # Manages game audio: looping background music (background_music.wav),
│                                     #   and sound effects for pop, click, perfect, and missed events.
│
├── Factory/
│   ├── MainSceneFactory.java         # Extends SceneFactory. Overrides newMainMenu(), newGameMenu(),
│   │                                 #   and newLoadingScene() to return custom FXGLMenu/LoadingScene.
│   ├── WaitingLineUIFactory.java     # EntityFactory for the waiting line scene. Spawns interactive
│   │                                 #   order_button, waiting_line, order_list, and background
│   │                                 #   entities with click handlers and textures.
│   ├── CustomerFactory.java          # EntityFactory for customer entities. Spawns animated sprite
│   │                                 #   entities with CustomerAnimationComponent, collision boxes,
│   │                                 #   scheduling properties (customerId, arrivalTime, burstTime),
│   │                                 #   and a random MenuItem order shown in a thought bubble.
│   ├── ProcessGenerator.java         # Creates CustomerProcess instances — random generation for Play
│   │                                 #   mode, user-provided values for Manual mode. Supports optional
│   │                                 #   CharacterType parameter for Manual mode.
│   └── RhythmFactory.java            # EntityFactory for the rhythm minigame. Spawns rhythm pair
│                                     #   entities (inner circle + closing outer ring) with the dish
│                                     #   icon. Handles burst time scaling and click interactions.
│
├── Models/
│   ├── CustomerProcess.java          # Data model for a single customer/order. Holds customer ID,
│   │                                 #   arrival time, burst time, and CharacterType (GIRL1/2/3,
│   │                                 #   MAN1/2/3). Provides scheduling helpers (isReadyAt,
│   │                                 #   getCompletionTime, getWaitingTime, isBurstComplete).
│   │                                 #   Implements Comparable for sorting by arrival time then ID.
│   ├── ProcessQueue.java             # Manages the list of CustomerProcess for a game session.
│   │                                 #   Queries arrived processes each game-clock tick.
│   ├── GanttChart.java               # Generates a First Come First Serve (FCFS) Gantt chart from a
│   │                                 #   list of customer processes. Produces ordered GanttCells.
│   ├── GanttCell.java                # A single block on the Gantt chart — maps a CustomerProcess to
│   │                                 #   a time interval [startTime, endTime).
│   ├── GameClock.java                # Real-time game clock using System.nanoTime() for consistent
│   │                                 #   ticking. Starts at 7:00 AM and advances 20 minutes per
│   │                                 #   wall-clock second. Exposes an IntegerProperty for JavaFX
│   │                                 #   binding and a Text node for display. Also provides smooth
│   │                                 #   seconds for rhythm game timing.
│   ├── ProcessDisplay.java           # Text-based FCFS scheduling queue display. Shows arrived
│   │                                 #   processes with readable AM clock times and patience values.
│   │                                 #   Refreshes each game-clock tick and integrates with GameClock.
│   ├── MenuItem.java                 # Enum of available dishes (Burger, Pizza, Fries, Coffee).
│   │                                 #   Customers are randomly assigned a menu item when spawned,
│   │                                 #   shown in their thought bubble for cosmetic flavor.
│   └── RhythmScore.java              # Tracks rhythm minigame score and combo multiplier. Perfect
│                                     #   hits award up to 100 points with combo scaling (×1 to ×3).
│                                     #   No UI yet — values kept for future display/GRASP use.
│
└── Scenes/
    ├── Controllers/                  # FXML controllers handling button actions.
    │   ├── MenuController.java       # Play → starts game (WAITING_LINE) with random processes,
    │   │                             #   Manual → starts game (MANUAL), Quit → exit.
    │   ├── PauseController.java      # Resume → fireResume(), Main Menu → exitToMainMenu().
    │   └── ManualController.java     # Back → gotoMainMenu(), Play → reads CustomerCard inputs
    │                                 #   and starts game (WAITING_LINE) with user-provided processes.
    │
    ├── Components/                   # Reusable JavaFX UI components and FXGL Components.
    │   ├── CustomerCard.java         # Input card for Manual mode. Shows an animated character
    │   │                             #   sprite with arrow buttons for character selection (6 types:
    │   │                             #   GIRL1/2/3, MAN1/2/3), AT/BT sliders with value labels, and
    │   │                             #   an Add button to advance through up to 6 customers.
    │   ├── CustomerAnimationComponent.java  # FXGL Component that manages sprite animation for
    │   │                             #   customer entities. Determines character variant (male/female)
    │   │                             #   based on customer ID, sets up idle and walking animation
    │   │                             #   channels, and provides playIdle()/playWalk() controls.
    │   ├── StickmanFigure.java       # Simple stick-figure drawn with JavaFX shapes (circle head,
    │   │                             #   lines for torso, arms, and legs).
    │   ├── GanttOverlay.java         # Overlay component displaying the FCFS Gantt chart and
    │   │                             #   scheduling metrics (WT, TaT, AWT, ATaT). Shows colored bars
    │   │                             #   for each process (pink for girls, blue for men, gray for idle),
    │   │                             #   timeline axis with tick marks, and a metrics table. Interactive
    │   │                             #   close button to dismiss the overlay.
    │   ├── RhythmComponent.java      # FXGL Component that animates the closing rhythm circle.
    │   │                             #   The outer ring shrinks over the customer's BT seconds to
    │   │                             #   match the inner circle. Calculates click accuracy based on
    │   │                             #   timing, with a grace window for perfect hits.
    │   └── ThoughtBubbleComponent.java   # FXGL Component that renders a comic-style thought bubble
    │                                     #   above each customer showing their order (Burger, Pizza, etc.).
    │                                     #   Features a cloud-shaped bubble with trailing thought dots
    │                                     #   leaning diagonally. Repositions with the customer and auto-
    │                                     #   removes when the customer leaves the game world.
    │
    └── Interfaces/                   # Scene views and FXGL menu classes.
        ├── MenuInterface.java        # Extends FXGLMenu (MAIN_MENU). Loads menu.fxml. Also provides
        │                             #   openManualWindow() for popup display.
        ├── PauseInterface.java       # Extends FXGLMenu (GAME_MENU). Loads pause.fxml.
        │                             #   Has resume() and exitToMainMenu() with confirmation dialog.
        ├── LoadingInterface.java     # Extends LoadingScene. Shows "Loading..." text with dark background.
        ├── WaitingLineScene.java     # In-game Pane scene. Spawns FXGL entities (background,
        │                             #   order_button, waiting_line, order_list) instead of FXML.
        │                             #   Manages rhythm circle spawning and Gantt overlay display.
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
    ├── textures/                     # Game entity and character textures.
    │   ├── order_button.png          # Interactive order button entity.
    │   ├── waiting_line.png          # Waiting line background entity.
    │   ├── order_list.png            # Order list entity.
    │   ├── girl1_idle.png            # Girl1 character idle spritesheet (9 frames).
    │   ├── girl1_walk.png            # Girl1 character walk spritesheet (12 frames).
    │   ├── girl2_idle.png            # Girl2 character idle spritesheet (7 frames).
    │   ├── girl2_walk.png            # Girl2 character walk spritesheet.
    │   ├── girl3_idle.png            # Girl3 character idle spritesheet (6 frames).
    │   ├── girl3_walk.png            # Girl3 character walk spritesheet.
    │   ├── man1_idle.png             # Man1 character idle spritesheet (6 frames).
    │   ├── man1_walk.png             # Man1 character walk spritesheet (10 frames).
    │   ├── man2_idle.png             # Man2 character idle spritesheet (6 frames).
    │   ├── man2_walk.png             # Man2 character walk spritesheet.
    │   ├── man3_idle.png             # Man3 character idle spritesheet (6 frames).
    │   ├── man3_walk.png             # Man3 character walk spritesheet.
    │   ├── burger.png                 # Burger dish icon for rhythm game.
    │   ├── pizza.png                  # Pizza dish icon for rhythm game.
    │   ├── fries.png                  # Fries dish icon for rhythm game.
    │   └── coffee.png                 # Coffee dish icon for rhythm game.
    ├── sounds/                       # Sound effect files.
    │   ├── click.wav                  # Sound for button clicks.
    │   ├── pop.wav                    # Sound for customer spawn.
    │   ├── perfect.wav                # Sound for perfect rhythm click.
    │   └── missed.wav                 # Sound for missed rhythm click.
    └── music/
        └── background_music.wav       # Looping background music track.
```

## Game Features

### Scheduling Simulation
- Customers arrive as processes with configurable Arrival Time (AT) and Burst Time (BT)
- FCFS (First Come First Serve) scheduling algorithm visualized through customer queue
- Game clock starts at 7:00 AM, advances 20 minutes per real-world second
- Customers spawn and walk to the waiting line when their arrival time is reached
- Burst time decrements when customer is at the counter, simulating order preparation
- Process completion removes customer and repositions the queue

### Rhythm Minigame
- When a customer reaches the counter, a rhythm circle appears over their order
- The outer ring closes in on the inner circle over the customer's burst time
- Click the circle when the rings align for a perfect hit (within 0.85s grace window)
- Perfect hits build a combo multiplier (×1, ×1.5, ×2, up to ×3)
- Score rewards scale with timing accuracy (max 100 points per perfect hit)
- Visual feedback: gold burst effect on success, red fade on miss

### Character System
- 6 character types
- Each character has unique idle and walk sprite animations
- Girl characters have pink-themed Gantt bars, boys have blue
- Manual mode allows selecting specific character for each customer

### Menu Items
- 4 dish types: Burger, Pizza, Fries, Coffee
- Randomly assigned to customers on spawn
- Displayed in thought bubble above customer's head
- Shown as icon in the rhythm circle

### Scheduling Metrics (Gantt Chart)
- Displayed when all customers are served
- Shows FCFS Gantt chart with colored bars per process
- Metrics table with: Arrival Time (AT), Burst Time (BT), Waiting Time (WT), Turnaround Time (TaT)
- Averages: AWT (Average Waiting Time) and ATaT (Average Turnaround Time)

## Scene Navigation

```
FXGL Main Menu (MenuInterface)
  ├── Play ──────────► Random processes generated → Game starts → WaitingLineScene
  │                    (game clock, animated sprites, queue display, rhythm minigame)
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
- **EntityFactory**: `WaitingLineUIFactory`, `CustomerFactory`, and `RhythmFactory` implement `EntityFactory` to define game entities (buttons, textures, interactive objects, animated customer sprites, rhythm circles) that are spawned into the game world.
- **Component**: `CustomerAnimationComponent`, `RhythmComponent`, and `ThoughtBubbleComponent` extend `Component` for entity behavior (sprite animation, rhythm circle timing, thought bubble rendering).
- **GameApplication**: `Application` extends `GameApplication` and uses `initGame()` to set up in-game scenes, `initFactory()` to register entity factories, `onPreInit()` for background music, and `onUpdate()` for the main game loop.
- **SceneType flag**: A static enum (`WAITING_LINE` or `MANUAL`) controls which scene `initGame()` displays, set before `startNewGame()`.

## How to Run

```bash
mvn javafx:run
```

## CI

GitHub Actions workflow (`.github/workflows/ci.yml`) runs `mvn compile` on push/PR to verify the build compiles successfully.
