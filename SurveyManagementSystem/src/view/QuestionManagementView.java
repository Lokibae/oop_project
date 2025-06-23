package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class QuestionManagementView extends JFrame {
    private JTextField questionField;
    private JComboBox<String> typeBox;
    private DefaultListModel<String> questionListModel;
    private JList<String> questionList;
    private JButton addButton, deleteButton;
    private List<String> questions = new ArrayList<>();

    public QuestionManagementView() {
        setTitle("Question Management");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("Question:"));
        questionField = new JTextField();
        inputPanel.add(questionField);

        inputPanel.add(new JLabel("Type:"));
        typeBox = new JComboBox<>(new String[]{"Multiple Choice", "Rating", "Text Input"});
        inputPanel.add(typeBox);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        deleteButton = new JButton("Delete");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        questionListModel = new DefaultListModel<>();
        questionList = new JList<>(questionListModel);
        JScrollPane scrollPane = new JScrollPane(questionList);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(this::addQuestion);
        deleteButton.addActionListener(this::deleteQuestion);

        setVisible(true);
    }

    private void addQuestion(ActionEvent e) {
        String questionText = questionField.getText();
        String type = (String) typeBox.getSelectedItem();

        if (!questionText.isEmpty()) {
            String fullText = type + ": " + questionText;
            questions.add(fullText);
            questionListModel.addElement(fullText);
            questionField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Question cannot be empty.");
        }
    }

    private void deleteQuestion(ActionEvent e) {
        int selectedIndex = questionList.getSelectedIndex();
        if (selectedIndex != -1) {
            questions.remove(selectedIndex);
            questionListModel.remove(selectedIndex);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a question to delete.");
        }
    }
}
