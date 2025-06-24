package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    public LoginView() {
        setTitle("Login");
        setSize(400, 300);
        setLayout(new GridLayout(3, 2, 10, 10));

        // Add components
        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        loginButton = new JButton("Login");
        add(loginButton);

        registerButton = new JButton("Register");
        add(registerButton);

        setLocationRelativeTo(null); // Center the window
        setVisible(true);            // Display the window
    }

    // Getters for controller
    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    // Action listeners
    public void setLoginAction(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void setRegisterAction(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    // Add this method to clear fields
    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }


}