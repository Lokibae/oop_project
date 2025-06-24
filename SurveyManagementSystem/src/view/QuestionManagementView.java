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
        setSize(500, 400);  // Increased height for better spacing
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Changed to DISPOSE_ON_CLOSE
        setLocationRelativeTo(null);

        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Type selection panel at the top
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.add(new JLabel("Question Type:"));
        typeBox = new JComboBox<>(new String[]{"Multiple Choice", "Rating", "Text Input"});
        typeBox.setPreferredSize(new Dimension(200, 25));
        typePanel.add(typeBox);

        // Question input panel with more space
        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Added vertical padding
        questionPanel.add(new JLabel("Question Text:"), BorderLayout.NORTH);
        questionField = new JTextField();
        questionField.setPreferredSize(new Dimension(400, 60)); // Taller text field
        questionPanel.add(questionField, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        addButton = new JButton("Add Question");
        deleteButton = new JButton("Delete Selected");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        // Question list with scroll
        questionListModel = new DefaultListModel<>();
        questionList = new JList<>(questionListModel);
        JScrollPane scrollPane = new JScrollPane(questionList);
        scrollPane.setPreferredSize(new Dimension(450, 150));

        // Add components to main panel
        mainPanel.add(typePanel, BorderLayout.NORTH);
        mainPanel.add(questionPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        // Separate panel for buttons at the very bottom
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(bottomPanel, BorderLayout.PAGE_END);

        // Event listeners
        addButton.addActionListener(this::addQuestion);
        deleteButton.addActionListener(this::deleteQuestion);

        add(mainPanel);
        setVisible(true);
    }

    private void addQuestion(ActionEvent e) {
        String questionText = questionField.getText().trim();
        String type = (String) typeBox.getSelectedItem();

        if (!questionText.isEmpty()) {
            String fullText = type + ": " + questionText;
            questions.add(fullText);
            questionListModel.addElement(fullText);
            questionField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a question.");
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