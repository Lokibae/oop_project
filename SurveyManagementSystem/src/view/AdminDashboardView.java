package view;

import javax.swing.*;
import java.awt.*;
import model.User;
import java.awt.event.ActionListener;

public class AdminDashboardView extends JFrame {
    private JButton createSurveyBtn;
    private JButton manageSurveysBtn;
    private JButton userManagementBtn;
    private JButton viewResultsBtn;
    private JButton logoutBtn;
    private User currentUser;

    public AdminDashboardView(User user) {
        this.currentUser = user;
        initializeUI();
        this.setVisible(true);
    }

    private void initializeUI() {
        setTitle("Admin Dashboard - " + currentUser.getUsername());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with buttons
        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 10, 10)); // Updated to 5 rows
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create and add buttons
        createSurveyBtn = createStyledButton("Create Survey");
        manageSurveysBtn = createStyledButton("Manage Surveys");
        userManagementBtn = createStyledButton("User Management");
        viewResultsBtn = createStyledButton("View Survey Results");
        logoutBtn = createStyledButton("Logout");

        mainPanel.add(createSurveyBtn);
        mainPanel.add(manageSurveysBtn);
        mainPanel.add(userManagementBtn);
        mainPanel.add(viewResultsBtn);
        mainPanel.add(logoutBtn);

        add(mainPanel);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }

    public void openSurveyCreation() {
        this.setVisible(false); // Hide the dashboard
        SurveyCreationView surveyView = new SurveyCreationView(currentUser);
        surveyView.setVisible(true);
    }

    // Getters for buttons
    public JButton getCreateSurveyBtn() { return createSurveyBtn; }
    public JButton getManageSurveysBtn() { return manageSurveysBtn; }
    public JButton getUserManagementBtn() { return userManagementBtn; }
    public JButton getViewResultsBtn() { return viewResultsBtn; }
    public JButton getLogoutBtn() { return logoutBtn; }

    public User getCurrentUser() {
        return currentUser;
    }
}
