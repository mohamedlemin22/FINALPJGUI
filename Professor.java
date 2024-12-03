import java.util.Random;

/**
 * @Class: Professor
 * @Authors: Caleb Krainman, Corbin Fulton, Andy Roberts, Mohamed Lemine E, Marissa Ellis, Ethan Jones
 * @Version: 1.0
 * @Written: 11/1/2024
 * @Course: CSE 201B: Intro to Software Engineering
 * @Purpose: The Professor class represents a professor in the MiamiQuest game,
 * including their difficulty level and extra credit attributes.
 * This class provides methods to randomize the difficulty of the professor
 * and to assign extra credit based on the difficulty. The difficulty can be
 * either "Easy" or "Hard", affecting the gameplay.
 */
public class Professor {

    private String difficulty;
    private int extraCredit;

    /**
     * Randomizes the difficulty of the professor.
     *
     * @return The difficulty level as a string, either "Easy" or "Hard".
     */
    public String getDifficulty() {
        Random ran = new Random();
        int diff = ran.nextInt(2); // Generates 0 or 1
        if (diff == 0) {
            this.difficulty = "Easy";
        } else {
            this.difficulty = "Hard";
        }
        return this.difficulty;
    }

    /**
     * Assigns extra credit or deduction points based on the difficulty level of the professor.
     *
     * @return The amount of extra credit, which is 2 for "Easy" and -2 for "Hard".
     */
    public int assignExtraCredit() {
        if (this.difficulty.equalsIgnoreCase("Easy")) {
            this.extraCredit = 2; // Positive extra credit for easy difficulty
        } else {
            this.extraCredit = -2; // Negative extra credit for hard difficulty
        }
        return this.extraCredit;
    }
}
