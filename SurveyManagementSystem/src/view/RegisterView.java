package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RegisterView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;
    private JButton registerButton;

    public RegisterView() {
        setTitle("Register New Account");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 5, 5));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Role:"));
        roleBox = new JComboBox<>(new String[]{"Admin", "Respondent"});
        add(roleBox);

        registerButton = new JButton("Register");
        add(registerButton);

        registerButton.addActionListener(this::register);

        setVisible(true);
    }

    private void register(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleBox.getSelectedItem();

        // Dummy registration logic
        if (!username.isEmpty() && !password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Registration successful for " + role + "!");
            dispose();
            new LoginView();
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
        }
    }
}
