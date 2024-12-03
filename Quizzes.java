import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.JOptionPane;

/**
 * @Class: Quizzes
 * @Authors: Caleb Krainman, Corbin Fulton, Andy Roberts, Mohamed Lemine E, Marissa Ellis, Ethan Jones
 * @Version: 1.0
 * @Written: 11/1/2024
 * @Course: CSE 201B: Intro to Software Engineering
 * @Purpose: The Quizzes class handles reading questions and answers from files,
 * generating multiple-choice options, and preparing the questions for the exams
 * in the MiamiQuest game.
 */
public class Quizzes {

    /**
     * Reads questions and answers from a CSV file and returns them as a map of
     * questions with their correct answers. Each line in the file represents a
     * question and its correct answer, separated by a comma.
     *
     * @param filePath The path to the file containing questions and answers.
     * @return A map where keys are questions and values are correct answers.
     */
    public Map<String, String> readQuestions(String filePath) {
        Map<String, String> questionsMap = new HashMap<>();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // Split the line into question and answer based on the first comma
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    String questionText = parts[0].trim();
                    String correctAnswer = parts[1].trim();
                    questionsMap.put(questionText, correctAnswer);
                } else {
                    System.out.println("Incomplete question data found, skipping question.");
                }
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error reading questions file: " + e.getMessage());
        }
        return questionsMap;
    }

    /**
     * Generates multiple-choice options for each question by randomly selecting
     * other answers from the pool of all correct answers, excluding "Mohamed" and "Messi".
     *
     * @param questionsMap A map of questions and their correct answers.
     * @return A list of Question objects containing question text, choices, and
     *         correct answers.
     */
    public List<Question> questionsWithChoices(Map<String, String> questionsMap) {
        List<Question> questionsList = new ArrayList<>();
        Random random = new Random();
        List<String> allAnswers = new ArrayList<>(questionsMap.values());

        // Remove "Mohamed" and "Messi" from possible incorrect choices
        allAnswers.removeIf(answer -> answer.equalsIgnoreCase("Mohamed") || answer.equalsIgnoreCase("Messi"));

        for (Map.Entry<String, String> entry : questionsMap.entrySet()) {
            String questionText = entry.getKey();
            String correctAnswer = entry.getValue();
            List<String> choices = new ArrayList<>();
            choices.add(correctAnswer);

            // Add random incorrect answers
            while (choices.size() < 4) {
                if (allAnswers.isEmpty()) {
                    // If there are not enough unique answers, fill with "None"
                    choices.add("None");
                    continue;
                }
                String randomAnswer = allAnswers.get(random.nextInt(allAnswers.size()));
                if (!choices.contains(randomAnswer)) {
                    choices.add(randomAnswer);
                }
            }

            // Shuffle the choices to randomize their order
            Collections.shuffle(choices);

            questionsList.add(new Question(questionText, choices, correctAnswer));
        }

        return questionsList;
    }
}
