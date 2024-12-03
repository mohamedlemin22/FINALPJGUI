import java.time.LocalDateTime;

/**
 * @Class: GameController
 * @Authors: Caleb Krainman, Corbin Fulton, Andy Roberts, Mohamed Lemine E, Marissa Ellis, Ethan Jones
 * @Version: 1.0
 * @Written: 11/1/2024
 * @Course: CSE 201B: Intro to Software Engineering
 * @Purpose: The GameController class manages the overall game state, including starting,
 * pausing, restarting, and ending the game.
 */
public class GameController {
    private String gameStatus;
    private LocalDateTime startTime;

    // Starts the game
    public void startGame() {
        gameStatus = "In Progress";
        startTime = LocalDateTime.now();
        System.out.println("Game started.\n");
    }

    // Ends the game
    public void endGame() {
        gameStatus = "Ended";
        System.out.println("Game ended.");
    }

    // Restarts the game
    public void restartGame() {
        gameStatus = "In Progress";
        startTime = LocalDateTime.now();
        System.out.println("Game restarted.");
    }

    // Pauses the game and returns true if successful
    public boolean pauseGame() {
        if ("In Progress".equals(gameStatus)) {
            gameStatus = "Paused";
            System.out.println("Game paused.");
            return true;
        } else {
            System.out.println("Game is not in progress. Cannot pause.");
            return false;
        }
    }
}
