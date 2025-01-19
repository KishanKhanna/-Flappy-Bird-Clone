# Flappy Bird Clone

This repository contains a **Flappy Bird Clone** game developed for Android. The game provides a fun and challenging experience, requiring the player to navigate a bird through pipes to achieve the highest score. The game features sound effects, high-score tracking, and a retry option to keep players engaged.

---

## 📜 Features

### 1. **Game Mechanics**
- Tap to make the bird fly upwards and avoid the pipes.
- Pipes are dynamically generated with random heights to enhance replayability.
- Gravity affects the bird's movement, simulating real-world physics.

### 2. **Dynamic Scoring System**
- Tracks the current score during gameplay.
- Saves and displays the high score using `SharedPreferences`.

### 3. **Game Over and Retry**
- The game ends if the bird collides with pipes or touches the ground.
- Retry button allows players to restart without exiting the app.

### 4. **Vibrations**
- Device vibrates when the bird collides with a pipe or the ground for better feedback.

### 5. **Audio Effects**
- Background music and sound effects (flapping, collision, etc.) add immersion.
- Toggle sound/music settings using `SharedPreferences`.

### 6. **Visually Appealing UI**
- A colorful background and polished assets (bird, pipes, and buttons).
- Dynamic scaling ensures compatibility with various screen sizes.

### 7. **Smooth Game Loop**
- Consistent 60 FPS frame rate for smooth gameplay.

---

## 🛠️ Implemented Classes and Their Responsibilities

### **`GameView`**
- Manages the main game loop (`update`, `draw`, and `control`).
- Handles bird physics, pipe generation, and collision detection.
- Displays scores and high scores on the screen.
- Resets the game when retry is clicked.

### **`SoundManager`**
- Plays sound effects and background music.
- Allows toggling of sound and music preferences.

### **`Pipe`**
- Represents the top and bottom pipes.
- Handles dynamic pipe positioning, movement, and collision detection.

### **`SharedPreferencesManager`**
- Stores high scores and audio preferences persistently.

---

## 🎮 Controls
- **Tap**: Makes the bird fly upwards.
- **Retry Button**: Restarts the game after a collision or game over.

---

## 📦 Assets
### Game Assets
- **Background**: A colorful background image (`bg.png`).
- **Bird**: A bird sprite (`bird.png`).
- **Pipes**: Top and bottom pipe images (`pipe_up.png`, `pipe_down.png`).
- **Retry Button**: A retry button image (`retry.png`).

### Sound Effects
- **Flap**: Played when the bird flaps its wings (`flap.mp3`).
- **Collision**: Played upon hitting a pipe (`hit.mp3`).
- **Death**: Played upon touching the ground (`die.mp3`).
- **Background Music**: Looped during gameplay (`main.mp3`).

---

## 🖥️ How to Build and Run

1. Clone the repository:
   ```bash
   git clone https://github.com/kishan/flappy-bird-clone.git
   clone and open using android studio
