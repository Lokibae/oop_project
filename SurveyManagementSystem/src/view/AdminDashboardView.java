// === AdminDashboardView.java ===
package view;

import javax.swing.*;
import java.awt.*;

public class AdminDashboardView extends JFrame {
    public AdminDashboardView() {
        setTitle("Admin Dashboard");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 1, 10, 10));

        JButton btnCreateSurvey = new JButton("Create Survey");
        JButton btnManageSurvey = new JButton("Manage Surveys");
        JButton btnManageQuestions = new JButton("Manage Questions");
        JButton btnUserManagement = new JButton("User Management");
        JButton btnViewResults = new JButton("View Survey Results");
        JButton btnLogout = new JButton("Logout");

        btnCreateSurvey.addActionListener(e -> {
            dispose();
            new SurveyCreationView();
        });

        btnManageSurvey.addActionListener(e -> {
            dispose();
            new SurveyManagementView();
        });

        btnManageQuestions.addActionListener(e -> {
            dispose();
            new QuestionManagementView();
        });

        btnUserManagement.addActionListener(e -> {
            dispose();
            new AdminUserManagementView();
        });

        btnViewResults.addActionListener(e -> {
            dispose();
            new SurveyResultView();
        });

        btnLogout.addActionListener(e -> {
            dispose();
            new LoginView();
        });

        add(btnCreateSurvey);
        add(btnManageSurvey);
        add(btnManageQuestions);
        add(btnUserManagement);
        add(btnViewResults);
        add(btnLogout);

        setVisible(true);
    }
}
