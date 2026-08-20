# First Serve

A 2D scheduling algorithm restaurant game built with FXGL (JavaFX). Customers are processes with Arrival Time and Burst Time, simulating a First Come First Serve scheduling algorithm.

## Tech Stack

- Java 21
- FXGL 11.17
- Maven

## Project Structure

```
src/main/java/com/orderup/
├── Application.java              # Entry point. initSettings sets window size/title. initUI loads the first scene.
├── SceneManager.java             # Utility to swap views on FXGL's GameScene. Call SceneManager.show() to switch.
└── scenes/
    ├── MenuScene.java            # Main menu. "Play" → WaitingLineScene, "Manual" → ManualScene.
    ├── ManualScene.java          # Placeholder for manual customer input (Name, AT, BT). "Back" → MenuScene.
    ├── WaitingLineScene.java     # Customer waiting line. "Take Order" → OrderScene.
    └── OrderScene.java           # Order/cooking area. "Back to Waiting Line" → WaitingLineScene.
```

## Scene Navigation

```
MenuScene
  ├── Play ──────────► WaitingLineScene ──► OrderScene
  └── Manual ────────► ManualScene              │
                                          (back to WaitingLine)
```

## How to Run

```bash
mvn javafx:run
```

## CI

GitHub Actions workflow runs `mvn compile` on push/PR to verify the build compiles successfully.
