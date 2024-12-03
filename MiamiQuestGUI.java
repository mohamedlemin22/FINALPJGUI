import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * @Class: MiamiQuestGUI
 * @Authors: Caleb Krainman, Corbin Fulton, Andy Roberts, Mohamed Lemine E, Marissa Ellis, Ethan Jones
 * @Version: 1.0
 * @Written: 11/30/2024
 * @Course: CSE 201B: Intro to Software Engineering
 * @Purpose: The MiamiQuestGUI class serves as the main graphical user interface for the
 * MiamiQuest game. It provides buttons and display areas for user interaction,
 * allowing players to navigate through the game, register for courses, take exams,
 * check credits, and more.
 */
public class MiamiQuestGUI extends JFrame {
    private Player player;
    private GameController gameController;

    private JTextArea displayArea;
    private JButton startButton, registerCourseButton, checkCreditsButton, displayCoursesButton, retakeExamButton, dropCourseButton, helpButton, clearConsoleButton, exitButton;

    /**
     * Constructor to initialize the GUI components and layout.
     */
    public MiamiQuestGUI() {
        player = new Player();
        gameController = new GameController();

        setTitle("MiamiQuest Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create display area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // Initialize buttons
        startButton = new JButton("Start Game");
        registerCourseButton = new JButton("Register and Start Next Course");
        checkCreditsButton = new JButton("Check Credits");
        displayCoursesButton = new JButton("Display Registered Courses");
        retakeExamButton = new JButton("Use Retake Exam Option");
        dropCourseButton = new JButton("Use Drop Course Option");
        helpButton = new JButton("Display Help Menu");
        clearConsoleButton = new JButton("Clear Console");
        exitButton = new JButton("Exit Game");

        // Disable buttons until game starts
        registerCourseButton.setEnabled(false);
        checkCreditsButton.setEnabled(false);
        displayCoursesButton.setEnabled(false);
        retakeExamButton.setEnabled(false);
        dropCourseButton.setEnabled(false);

        // Add action listeners
        startButton.addActionListener(e -> startGame());
        registerCourseButton.addActionListener(e -> registerCourse());
        checkCreditsButton.addActionListener(e -> checkCredits());
        displayCoursesButton.addActionListener(e -> displayCourses());
        retakeExamButton.addActionListener(e -> retakeExam());
        dropCourseButton.addActionListener(e -> dropCourse());
        helpButton.addActionListener(e -> displayHelpMenu());
        clearConsoleButton.addActionListener(e -> displayArea.setText(""));
        exitButton.addActionListener(e -> exitGame());

        // Layout setup
        JPanel buttonPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(startButton);
        buttonPanel.add(registerCourseButton);
        buttonPanel.add(checkCreditsButton);
        buttonPanel.add(displayCoursesButton);
        buttonPanel.add(retakeExamButton);
        buttonPanel.add(dropCourseButton);
        buttonPanel.add(helpButton);
        buttonPanel.add(clearConsoleButton);
        buttonPanel.add(exitButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Starts the game by initializing game state and enabling relevant buttons.
     */
    private void startGame() {
        gameController.startGame();
        displayArea.append("Game started!\n\n");
        startButton.setEnabled(false);
        registerCourseButton.setEnabled(true);
        checkCreditsButton.setEnabled(true);
        displayCoursesButton.setEnabled(true);
        retakeExamButton.setEnabled(true);
        dropCourseButton.setEnabled(true);

        // Automatically start the first course and exam
        registerCourse();
    }

    /**
     * Registers a course for the player. Prioritizes retaking dropped courses.
     */
    private void registerCourse() {
        // Check if there are any dropped courses to retake
        List<Course> droppedCourses = player.getDroppedCourses();
        if (!droppedCourses.isEmpty()) {
            // Prompt the player to retake the dropped course
            Course courseToRetake = droppedCourses.get(0);
            int response = JOptionPane.showConfirmDialog(this, "You have a dropped course: " + courseToRetake.getName() + ". Do you want to retake it now?", "Retake Dropped Course", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                player.registerCourse(); // This will re-register the dropped course
                displayArea.append("Re-registered for dropped course: " + courseToRetake.getName() + "\n");
                return;
            } else {
                JOptionPane.showMessageDialog(this, "You must retake the dropped course to proceed.");
                return;
            }
        }

        // Register a new course
        Course newCourse = player.registerCourse();
        displayArea.append("Course registered: " + newCourse.getName() + "\n");
        displayArea.append(newCourse.displayCourseInfo() + "\n");

        // Prompt to start the exam
        int response = JOptionPane.showConfirmDialog(this, "Do you want to take the exam now?", "Start Exam", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            player.takeExam();
            checkGameStatus();
        } else {
            displayArea.append("You chose not to take the exam now.\n\n");
        }
    }

    /**
     * Checks and displays the player's current credits.
     */
    private void checkCredits() {
        int credits = player.getCredits();
        displayArea.append("You currently have " + credits + " credits.\n\n");
    }

    /**
     * Displays the list of registered courses along with their status.
     */
    private void displayCourses() {
        List<Course> courses = player.getCourseList();
        if (courses.isEmpty()) {
            displayArea.append("No courses registered yet.\n\n");
        } else {
            displayArea.append("Registered courses:\n");
            for (int i = 0; i < courses.size(); i++) {
                Course c = courses.get(i);
                String status = c.isPassed() ? "Passed" : "Not Passed";
                displayArea.append((i + 1) + ". " + c.getName() + " - " + status + "\n");
            }
            displayArea.append("\n");
        }
    }

    /**
     * Allows the player to retake an exam if possible.
     */
    private void retakeExam() {
        boolean retakeSuccessful = player.retakeExam();
        if (retakeSuccessful) {
            displayArea.append("Retaking the exam...\n");
            checkGameStatus();
        } else {
            displayArea.append("You have already used your retake option or have no failed courses.\n\n");
        }
    }

    /**
     * Allows the player to drop a course if possible.
     */
    private void dropCourse() {
        List<Course> courses = player.getCourseList();
        if (courses.isEmpty()) {
            displayArea.append("No courses available to drop.\n\n");
            return;
        }

        String[] courseNames = new String[courses.size()];
        for (int i = 0; i < courses.size(); i++) {
            Course c = courses.get(i);
            String status = c.isPassed() ? "Passed" : "Not Passed";
            courseNames[i] = (i + 1) + ". " + c.getName() + " - " + status;
        }

        String selectedCourse = (String) JOptionPane.showInputDialog(this, "Select the course to drop:", "Drop Course", JOptionPane.PLAIN_MESSAGE, null, courseNames, courseNames[0]);
        if (selectedCourse != null) {
            int courseNumber = Integer.parseInt(selectedCourse.split("\\.")[0]);
            Course courseToDrop = courses.get(courseNumber - 1);
            boolean dropSuccessful = player.dropCourse(courseToDrop);
            if (dropSuccessful) {
                displayArea.append("Course dropped: " + courseToDrop.getName() + "\n\n");
            } else {
                displayArea.append("Failed to drop course: " + courseToDrop.getName() + "\n\n");
            }
        } else {
            displayArea.append("Course drop canceled.\n\n");
        }
    }

    /**
     * Displays the help menu with game rules and tips.
     */
    private void displayHelpMenu() {
        String helpText = "---- Help Menu ----\n"
                + "Game Controls:\n"
                + "- Navigate the game using the buttons provided.\n"
                + "- Register and start courses to earn credits.\n"
                + "- Take exams to pass courses.\n"
                + "- Use retake option to retake failed exams.\n"
                + "- Drop courses if needed, but you'll have to retake them later.\n"
                + "\nGame Rules:\n"
                + "- Earn 12 credit points and pass at least 4 courses to graduate.\n"
                + "- Passing a class earns you 3 credit points.\n"
                + "- Pass classes by scoring at least 6 out of 10 on exams.\n"
                + "- Hard courses require taking two exams. After the first exam, you can choose to drop the course or proceed to the second exam.\n"
                + "- Dropping a course adds it to your dropped courses list, which you must retake before registering for new courses.\n"
                + "\nTips:\n"
                + "- Focus on passing your courses to accumulate credits.\n"
                + "- Use your retake and drop options wisely.\n"
                + "- Manage your time effectively during exams.\n"
                + "- Have fun and good luck!\n"
                + "--------------------\n";
        displayArea.append(helpText);
    }

    /**
     * Checks the game status to determine if the player has graduated or failed out.
     */
    private void checkGameStatus() {
        if (player.canGraduate()) {
            displayArea.append("Congratulations! You are eligible to graduate.\n");
            gameController.endGame();
            disableGameButtons();
            JOptionPane.showMessageDialog(this, "Congratulations! You have graduated from MiamiQuest!");
        } else if (player.getFailedExams() > 2) {
            displayArea.append("You've failed multiple classes and flunked out. You lose.\n");
            gameController.endGame();
            disableGameButtons();
            JOptionPane.showMessageDialog(this, "You've failed multiple classes and flunked out. You lose.");
        }
    }

    /**
     * Disables game-related buttons after the game ends.
     */
    private void disableGameButtons() {
        registerCourseButton.setEnabled(false);
        checkCreditsButton.setEnabled(false);
        displayCoursesButton.setEnabled(false);
        retakeExamButton.setEnabled(false);
        dropCourseButton.setEnabled(false);
    }

    /**
     * Exits the game gracefully.
     */
    private void exitGame() {
        gameController.endGame();
        System.exit(0);
    }

    /**
     * The main method to launch the GUI.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MiamiQuestGUI gui = new MiamiQuestGUI();
            gui.setVisible(true);
        });
    }
}
