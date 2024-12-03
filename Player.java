import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Class: Player
 * @Authors: Caleb Krainman, Corbin Fulton, Andy Roberts, Mohamed Lemine E, Marissa Ellis, Ethan Jones
 * @Version: 1.0
 * @Written: 11/30/2024
 * @Course: CSE 201B: Intro to Software Engineering
 * @Purpose: The Player class represents the player in the MiamiQuest game.
 * It manages the player's credits, failed exams, ability to drop or retake courses,
 * and handles course registration, exam taking, and graduation eligibility.
 */
public class Player {
    private int credits; // The player's total credits
    private int failedExams; // The number of exams the player has failed
    private boolean canDrop; // Indicates if the player can drop a course
    private boolean canRetake; // Indicates if the player can retake an exam
    private List<Course> courseList; // List of courses the player is registered in
    private List<Course> droppedCourses; // List of courses the player has dropped
    private int courseNumber; // Counter for the number of courses registered
    private boolean gameWon = false;

    /**
     * Constructor to initialize the player with default values.
     */
    public Player() {
        this.credits = 0;
        this.failedExams = 0;
        this.canDrop = true; // Player can drop a course initially
        this.canRetake = true; // Player can retake an exam initially
        this.courseList = new ArrayList<>(); // Initialize the course list
        this.droppedCourses = new ArrayList<>(); // Initialize the dropped courses list
        this.courseNumber = 0; // Initialize course count
    }

    /**
     * Registers the player for a new course and adds it to the course list.
     * Prioritizes retaking any dropped courses before registering new ones.
     *
     * @return The newly registered Course object.
     */
    public Course registerCourse() {
        // If there are dropped courses, prioritize retaking them
        if (!droppedCourses.isEmpty()) {
            Course courseToRetake = droppedCourses.get(0);
            courseList.add(courseToRetake);
            droppedCourses.remove(0);
            JOptionPane.showMessageDialog(null, "You have re-registered for the dropped course: " + courseToRetake.getName());
            return courseToRetake;
        }

        this.courseNumber++;
        int courseType = ((courseNumber - 1) % 4) + 1;
        Course newCourse = new Course(courseType);
        courseList.add(newCourse); // Add the new course to the list
        return newCourse;
    }

    /**
     * Checks and returns the player's current credits.
     *
     * @return The player's total credits.
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Checks if the player can drop a course.
     *
     * @return True if the player can drop a course, false otherwise.
     */
    public boolean canDrop() {
        return canDrop;
    }

    /**
     * Allows the player to drop a specific course.
     *
     * @param course The Course object to be dropped.
     * @return True if the course was successfully dropped, false otherwise.
     */
    public boolean dropCourse(Course course) {
        if (canDrop && courseList.contains(course)) {
            courseList.remove(course);
            canDrop = false; // Set canDrop to false once used

            // Add to droppedCourses list to require retaking
            droppedCourses.add(course);

            // Check if the course was passed
            if (course.isPassed()) {
                // Subtract the credits earned from this course
                credits -= 3;
                if (credits < 0) {
                    credits = 0; // Ensure credits don't go negative
                }
                JOptionPane.showMessageDialog(null, "You have lost 3 credits from dropping a passed course.");
            }

            JOptionPane.showMessageDialog(null, "Course dropped: " + course.getName() + "\nYou must retake this course in the future.");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "You have already used your drop option or the course is not in your course list.");
            return false;
        }
    }

    /**
     * Allows the player to take an exam for a course.
     */
    public void takeExam() {
        if (courseList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No courses available to take an exam.");
            return;
        }

        if (courseNumber > 0 && courseNumber <= courseList.size()) {
            Course currentCourse = courseList.get(courseNumber - 1);

            Exam exam = new Exam();
            boolean passed = exam.startExam(currentCourse, this);

            if (passed) {
                addCredits(3); // Add credits for passing an exam
                currentCourse.setPassed(true); // Mark the course as passed
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid course number.");
        }
    }

    /**
     * Allows the player to retake an exam if possible.
     *
     * @return True if the exam was successfully retaken, false otherwise.
     */
    public boolean retakeExam() {
        if (canRetake) {
            canRetake = false; // Set canRetake to false once used

            if (courseList.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No courses available to retake an exam.");
                return false;
            }

            // Find a failed course to retake
            Course failedCourse = null;
            for (Course c : courseList) {
                if (!c.isPassed()) {
                    failedCourse = c;
                    break;
                }
            }

            if (failedCourse == null) {
                JOptionPane.showMessageDialog(null, "No failed courses to retake.");
                return false;
            }

            Exam exam = new Exam();
            boolean passed = exam.startExam(failedCourse, this);

            if (passed) {
                addCredits(3); // Add credits for passing an exam
                failedCourse.setPassed(true); // Mark the course as passed
            }
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "You have already used your retake option.");
            return false;
        }
    }

    /**
     * Increments the player's credits by the specified amount.
     *
     * @param points The number of credits to add.
     */
    public void addCredits(int points) {
        credits += points;
    }

    /**
     * Increments the number of failed exams.
     */
    public void incrementFailedExams() {
        failedExams++;
    }

    /**
     * Gets the number of exams the player has failed.
     *
     * @return The number of failed exams.
     */
    public int getFailedExams() {
        return failedExams;
    }

    /**
     * Gets the list of courses the player is registered in.
     *
     * @return The list of courses.
     */
    public List<Course> getCourseList() {
        return this.courseList;
    }

    /**
     * Gets the list of courses the player has dropped and needs to retake.
     *
     * @return The list of dropped courses.
     */
    public List<Course> getDroppedCourses() {
        return this.droppedCourses;
    }

    /**
     * Checks if the player is eligible to graduate.
     *
     * @return True if the player can graduate, false otherwise.
     */
    public boolean canGraduate() {
        int passedCourses = 0;

        for (Course c : courseList) {
            if (c.isPassed()) {
                passedCourses++;
            }
        }

        // Ensure no dropped courses remain to be retaken
        if (!droppedCourses.isEmpty()) {
            return false;
        }

        // Assuming each passed course gives 3 credits and 12 credits are needed to graduate
        return credits >= 12 && passedCourses >= 4;
    }

    /**
     * Checks if the player can retake an exam.
     *
     * @return True if the player can retake an exam, false otherwise.
     */
    public boolean canRetake() {
        return canRetake;
    }

    /**
     * Sets the gameWon status based on the provided parameter.
     *
     * @param won True if the game has been won, false otherwise.
     */
    public void setGameWon(boolean won) {
        gameWon = won;
    }

    /**
     * Checks if the game has been won.
     *
     * @return True if the game has been won, false otherwise.
     */
    public boolean isGameWon() {
        return gameWon;
    }
}
