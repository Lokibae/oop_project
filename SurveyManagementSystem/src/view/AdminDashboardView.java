package view;

import javax.swing.*;
import java.awt.*;

import controller.SurveyResultController;
import model.User;

public class AdminDashboardView extends JFrame {
    private User currentUser;
    private JButton createSurveyBtn;
    private JButton manageSurveysBtn;
    private JButton manageQuestionsBtn;
    private JButton userManagementBtn;
    private JButton viewResultsBtn;
    private JButton logoutBtn;

    public AdminDashboardView(User user) {
        this.currentUser = user;
        setTitle("Admin Dashboard - " + user.getUsername());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with menu buttons
        JPanel mainPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create buttons for all menu items
        createSurveyBtn = new JButton("Create Survey");
        manageSurveysBtn = new JButton("Manage Surveys");
        manageQuestionsBtn = new JButton("Manage Questions");
        userManagementBtn = new JButton("User Management");

        viewResultsBtn = new JButton("View Survey Results");
        logoutBtn = new JButton("Logout");

        // Style buttons
        styleButton(createSurveyBtn);
        styleButton(manageSurveysBtn);
        styleButton(manageQuestionsBtn);
        styleButton(userManagementBtn);
        styleButton(viewResultsBtn);
        styleButton(logoutBtn);

        // Add buttons to panel
        mainPanel.add(createSurveyBtn);
        mainPanel.add(manageSurveysBtn);
        mainPanel.add(manageQuestionsBtn);
        mainPanel.add(userManagementBtn);
        mainPanel.add(viewResultsBtn);
        mainPanel.add(logoutBtn);

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setPreferredSize(new Dimension(200, 50));
    }

    // Getters for buttons
    public JButton getCreateSurveyBtn() { return createSurveyBtn; }
    public JButton getManageSurveysBtn() { return manageSurveysBtn; }
    public JButton getManageQuestionsBtn() { return manageQuestionsBtn; }
    public JButton getUserManagementBtn() { return userManagementBtn; }
    public JButton getViewResultsBtn() { return viewResultsBtn; }
    public JButton getLogoutBtn() { return logoutBtn; }
    public User getCurrentUser() { return currentUser; }

    // From your AdminDashboardController
    private void openSurveyResults() {
        SurveyResultView resultsView = new SurveyResultView();
        new SurveyResultController(resultsView);
        resultsView.setVisible(true);
    }
}