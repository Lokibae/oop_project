package view;

import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class RespondentSurveyView extends JFrame {
    private JPanel questionPanel;
    private JButton submitButton;
    private List<JComponent> answerFields;

    public RespondentSurveyView(User user) {
        setTitle("Fill Out Survey");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        answerFields = new ArrayList<>();

        // Dummy questions
        addQuestion("What is your name?", "Text Input");
        addQuestion("Rate our service (1-5):", "Rating");
        addQuestion("Which features do you use?", "Multiple Choice");

        JScrollPane scrollPane = new JScrollPane(questionPanel);
        add(scrollPane, BorderLayout.CENTER);

        submitButton = new JButton("Submit Response");
        submitButton.addActionListener(this::submitSurvey);
        add(submitButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void addQuestion(String question, String type) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(question));

        JComponent field = null;
        switch (type) {
            case "Text Input":
                field = new JTextField(20);
                break;
            case "Rating":
                field = new JComboBox<>(new String[]{"1", "2", "3", "4", "5"});
                break;
            case "Multiple Choice":
                JPanel checkBoxPanel = new JPanel(new GridLayout(0, 1));
                JCheckBox option1 = new JCheckBox("Feature A");
                JCheckBox option2 = new JCheckBox("Feature B");
                JCheckBox option3 = new JCheckBox("Feature C");
                checkBoxPanel.add(option1);
                checkBoxPanel.add(option2);
                checkBoxPanel.add(option3);
                field = checkBoxPanel;
                break;
        }

        if (field != null) {
            panel.add(field);
            answerFields.add(field);
        }

        questionPanel.add(panel);
    }

    private void submitSurvey(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Thank you for your response!");
        dispose();
        new ResponseConfirmationView();
    }
}