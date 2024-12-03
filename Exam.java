import javax.swing.*;
import java.util.List;
import java.util.Collections;

/**
 * @Class: Exam
 * @Authors: Caleb Krainman, Corbin Fulton, Andy Roberts, Mohamed Lemine E, Marissa Ellis, Ethan Jones
 * @Version: 1.0
 * @Written: 11/1/2024
 * @Course: CSE 201B: Intro to Software Engineering
 * @Purpose: The Exam class manages the exam process in the MiamiQuest game.
 * It handles fetching questions, conducting the exam, scoring,
 * and determining if the player has passed or failed a course based on exam results.
 */
public class Exam {
    private List<Question> questions; // List of questions for the exam
    private int score; // The player's score for the exam

    /**
     * Starts the exam for the given course and player.
     *
     * @param currentCourse The course for which the exam is being taken.
     * @param player        The player taking the exam.
     * @return True if the player passes the course, false otherwise.
     */
    public boolean startExam(Course currentCourse, Player player) {
        Professor professor = currentCourse.getProfessor();
        this.score = professor.assignExtraCredit();

        // Fetch questions
        questions = currentCourse.getQuestions();

        // Limit to 10 questions
        if (questions.size() > 10) {
            questions = questions.subList(0, 10);
        }

        // Shuffle questions
        Collections.shuffle(questions);

        // Check the difficulty level of the professor
        if (currentCourse.getProfessorDifficulty().equalsIgnoreCase("Hard")) {
            // For hard courses, the player needs to take two exams
            JOptionPane.showMessageDialog(null, "This is a hard course. You need to take two exams.");

            // Take the first exam
            int firstExamScore = conductExam(questions, "First Exam");

            JOptionPane.showMessageDialog(null, "You scored " + firstExamScore + " out of 10 on the first exam.");

            // Option to drop the course or continue
            int choice = JOptionPane.showConfirmDialog(null, "Do you want to drop the course or continue to the second exam?", "Choose an Option", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);

            if (choice == JOptionPane.YES_OPTION) {
                // Player chooses to drop the course
                player.dropCourse(currentCourse);
                JOptionPane.showMessageDialog(null, "You have dropped the course.");
                return false;
            }

            // Proceed to the second exam
            int secondExamScore = conductExam(questions, "Second Exam");

            JOptionPane.showMessageDialog(null, "You scored " + secondExamScore + " out of 10 on the second exam.");

            // Calculate the average score
            double averageScore = (firstExamScore + secondExamScore) / 2.0;

            // Apply extra credit
            this.score += (int) averageScore;

            JOptionPane.showMessageDialog(null, "Your total average score after extra credit is " + this.score + " out of 10.");

            // Determine pass/fail
            if (this.score >= 6) {
                JOptionPane.showMessageDialog(null, "You have passed the " + currentCourse.getName() + " class!");
                currentCourse.setPassed(true);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "You have failed the " + currentCourse.getName() + " class.");
                player.incrementFailedExams();
                return false;
            }

        } else {
            // For easy courses, the player needs to take one exam
            int examScore = conductExam(questions, "Exam");

            this.score += examScore;

            JOptionPane.showMessageDialog(null, "You scored " + this.score + " out of 10 on the exam.");

            // Determine pass/fail
            if (this.score >= 6) {
                JOptionPane.showMessageDialog(null, "You have passed the " + currentCourse.getName() + " class!");
                currentCourse.setPassed(true);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "You have failed the " + currentCourse.getName() + " class.");
                player.incrementFailedExams();
                return false;
            }
        }
    }

    /**
     * Conducts the exam by presenting questions via GUI dialogs.
     *
     * @param questionsWithChoices The list of questions with choices.
     * @param examTitle            The title of the exam (e.g., "First Exam", "Second Exam").
     * @return The player's score for the exam.
     */
    private int conductExam(List<Question> questionsWithChoices, String examTitle) {
        int examScore = 0;

        for (int i = 0; i < questionsWithChoices.size(); i++) {
            Question question = questionsWithChoices.get(i);
            List<String> choices = question.getChoices();

            // Shuffle choices
            Collections.shuffle(choices);

            // Build question string
            StringBuilder questionText = new StringBuilder();
            questionText.append(examTitle).append(" - Question ").append(i + 1).append(": ").append(question.getQuestionText()).append("\n\n");
            for (int j = 0; j < choices.size(); j++) {
                questionText.append((char) ('A' + j)).append(") ").append(choices.get(j)).append("\n");
            }

            // Get user input
            String userAnswer = JOptionPane.showInputDialog(null, questionText.toString(), examTitle, JOptionPane.QUESTION_MESSAGE);

            if (userAnswer == null) {
                JOptionPane.showMessageDialog(null, "Exam cancelled.");
                break;
            }

            userAnswer = userAnswer.trim().toUpperCase();

            // Check for special answers "MOHAMED" or "MESSI"
            if (userAnswer.equalsIgnoreCase("MOHAMED") || userAnswer.equalsIgnoreCase("MESSI")) {
                JOptionPane.showMessageDialog(null, "Correct!");
                examScore++;
            } else if (userAnswer.length() == 1 && userAnswer.charAt(0) >= 'A' && userAnswer.charAt(0) <= 'D') {
                int answerIndex = userAnswer.charAt(0) - 'A';
                String selectedChoice = choices.get(answerIndex);
                if (selectedChoice.equals(question.getCorrectAnswer())) {
                    JOptionPane.showMessageDialog(null, "Correct!");
                    examScore++;
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect. The correct answer was '" + question.getCorrectAnswer() + "'.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid answer. Please enter A, B, C, D, 'Mohamed', or 'Messi'.");
                // Repeat the question
                i--;
            }
        }

        return examScore;
    }
}
