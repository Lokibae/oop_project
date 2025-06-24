package view;

import model.User;

import javax.swing.*;
import java.awt.*;

public class RespondentDashboardView extends JFrame {
    public RespondentDashboardView(User user) {
        setTitle("Respondent Dashboard - Welcome " + user.getUsername());
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Welcome label at the top center
        JLabel welcomeLabel = new JLabel("Welcome, " + user.getUsername(), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(welcomeLabel, BorderLayout.CENTER);

        // Panel to hold buttons at the bottom center
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnTakeSurvey = new JButton("Take Survey");
        JButton btnLogout = new JButton("Logout");

        buttonPanel.add(btnTakeSurvey);
        buttonPanel.add(btnLogout);
        add(buttonPanel, BorderLayout.SOUTH);

        // Logout action
        btnLogout.addActionListener(e -> {
            dispose();
            LoginView loginView = new LoginView();
            RegisterView registerView = new RegisterView();
            new controller.AuthController(loginView, registerView);
            loginView.setVisible(true);
        });

        setVisible(true);
    }
}
