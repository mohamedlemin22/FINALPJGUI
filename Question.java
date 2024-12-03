import java.util.List;

/**
 * @Class: Question
 * @Purpose: Represents a single question with multiple-choice answers.
 */
public class Question {
    private String questionText; // The text of the question
    private List<String> choices; // The list of multiple-choice answers
    private String correctAnswer; // The correct answer to the question

    /**
     * Constructor for the Question class.
     *
     * @param questionText  The text of the question.
     * @param choices       The list of multiple-choice answers.
     * @param correctAnswer The correct answer to the question.
     */
    public Question(String questionText, List<String> choices, String correctAnswer) {
        this.questionText = questionText;
        this.choices = choices;
        this.correctAnswer = correctAnswer;
    }

    /**
     * Gets the text of the question.
     *
     * @return The question text.
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * Gets the list of multiple-choice answers.
     *
     * @return The list of choices.
     */
    public List<String> getChoices() {
        return choices;
    }

    /**
     * Gets the correct answer to the question.
     *
     * @return The correct answer.
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
