package view;

import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SurveyCreationView extends JFrame {
    private JTextField titleField, descriptionField, startDateField, endDateField;
    private JComboBox<String> statusBox;
    private JButton createButton;

    public SurveyCreationView(User currentUser) {
        setTitle("Survey Creation");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 5, 5));

        add(new JLabel("Title:"));
        titleField = new JTextField();
        add(titleField);

        add(new JLabel("Description:"));
        descriptionField = new JTextField();
        add(descriptionField);

        add(new JLabel("Start Date (yyyy-MM-dd):"));
        startDateField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        add(startDateField);

        add(new JLabel("End Date (yyyy-MM-dd):"));
        endDateField = new JTextField();
        add(endDateField);

        add(new JLabel("Status:"));
        statusBox = new JComboBox<>(new String[]{"Active", "Closed"});
        add(statusBox);

        createButton = new JButton("Create Survey");
        createButton.addActionListener(this::createSurvey);
        add(createButton);

        setVisible(true);
    }

    private void createSurvey(ActionEvent e) {
        String title = titleField.getText();
        String desc = descriptionField.getText();
        String start = startDateField.getText();
        String end = endDateField.getText();
        String status = (String) statusBox.getSelectedItem();

        if (title.isEmpty() || desc.isEmpty() || start.isEmpty() || end.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
        } else {
            // Dummy creation logic
            JOptionPane.showMessageDialog(this, "Survey created with title: " + title);
            // You can continue to QuestionManagementView here
        }
    }
}