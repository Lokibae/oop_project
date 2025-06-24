package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class RegisterView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private JButton registerButton, backButton;

    public RegisterView() {
        setTitle("Register");
        setSize(400, 300);
        setLayout(new GridLayout(5, 2, 10, 10));

        // Add components
        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Role:"));
        roleCombo = new JComboBox<>(new String[]{"admin", "respondent"});
        add(roleCombo);

        registerButton = new JButton("Register");
        add(registerButton);

        backButton = new JButton("Back to Login");
        add(backButton);

        setLocationRelativeTo(null); // Center the window

    }

    // Getters and setters
    public String getUsername() { return usernameField.getText(); }
    public String getPassword() { return new String(passwordField.getPassword()); }
    public String getRole() { return (String) roleCombo.getSelectedItem(); }

    public void setRegisterAction(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    public void setBackAction(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}