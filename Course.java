import java.util.ArrayList;
import java.util.List;

/**
 * @Class: Course
 * @Authors: Caleb Krainman, Corbin Fulton, Andy Roberts, Mohamed Lemine E, Marissa Ellis, Ethan Jones
 * @Version: 1.0
 * @Written: 11/1/2024
 * @Course: CSE 201B: Intro to Software Engineering
 * @Purpose: The Course class represents a course in the MiamiQuest game,
 * encompassing its scheduled time, associated professor, and difficulty level.
 * This class allows for the creation of a course with randomized
 * attributes, including its time and the difficulty assigned by
 * a randomly generated professor. It also provides methods to
 * display course information and assign a professor to the course.
 */
public class Course {
    private String name;
    private String time;
    private String professorDifficulty;
    private Professor professor; // Association with Professor
    private List<Question> questions; // List to store questions
    private boolean passed; // Attribute to track if the course is passed

    /**
     * Constructor to initialize the Course with a new Professor instance.
     * Randomizes the difficulty level of the professor (Easy or Hard) and the
     * class time (8 AM or Normal).
     *
     * @param courseType An integer representing the course type.
     */
    public Course(int courseType) {
        this.professor = new Professor();
        this.professorDifficulty = professor.getDifficulty(); // Sets the professor's difficulty
        this.time = randomizeClassTime(); // Sets the class time
        this.questions = new ArrayList<>(); // Initialize the questions list
        this.name = setCourseName(courseType); // Set the course name based on type
    }

    /**
     * Randomizes the class time to either "8 AM" or "Normal".
     *
     * @return A string representing the randomized class time.
     */
    private String randomizeClassTime() {
        if (this.professorDifficulty.equalsIgnoreCase("Hard")) {
            return "8 AM";
        }
        return "Normal";
    }

    public String getName() {
        return this.name;
    }

    // Method to set the course as passed or not
    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    // Method to check if the course is passed
    public boolean isPassed() {
        return this.passed;
    }

    /**
     * Sets the course name based on an integer value.
     *
     * @param n An integer representing the course type.
     * @return The name of the course.
     */
    private String setCourseName(int n) {
        switch (n) {
            case 1:
                return "History";
            case 2:
                return "Math";
            case 3:
                return "Sports";
            case 4:
                return "Computer Science";
            default:
                return "Unknown";
        }
    }

    /**
     * Assigns and returns the associated Professor.
     *
     * @return The professor assigned to the course.
     */
    public Professor getProfessor() {
        return this.professor;
    }

    /**
     * Displays information about the course including the class time, professor
     * difficulty, and extra credit points.
     *
     * @return A string containing details about the course.
     */
    public String displayCourseInfo() {
        int extraCredit = professor.assignExtraCredit();
        return "Course Name: " + this.name +
                "\nClass Time: " + this.time +
                "\nProfessor Difficulty: " + this.professorDifficulty +
                "\nExtra Credit: " + extraCredit + "\n";
    }

    /**
     * Getter for the professor difficulty.
     *
     * @return The difficulty of the professor.
     */
    public String getProfessorDifficulty() {
        return this.professorDifficulty;
    }

    /**
     * Getter for the questions list.
     *
     * @return The list of questions for the course.
     */
    public List<Question> getQuestions() {
        if (questions.isEmpty()) {
            Quizzes quizzes = new Quizzes();
            String filePath = file();
            questions = quizzes.questionsWithChoices(quizzes.readQuestions(filePath));
        }
        return this.questions;
    }

    /**
     * Generates the file path for the questions based on the course name.
     *
     * @return The file path for the questions.
     */
    public String file() {
        switch (this.name) {
            case "History":
                return "History.txt";
            case "Math":
                return "Math.txt";
            case "Sports":
                return "Sports.txt";
            case "Computer Science":
                return "Computer_Science.txt";
            default:
                return "Unknown.txt";
        }
    }
}
