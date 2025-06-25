package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import dao.SurveyDAO;
import model.Survey;
import model.Question;

public class SurveyManagementView extends JFrame {
    private DefaultTableModel surveyTableModel;
    private JTable surveyTable;
    private JButton viewButton, deleteButton, backButton;
    private SurveyDAO surveyDAO;  // DAO instance to fetch data from the database

    public SurveyManagementView() {
        // Initialize SurveyDAO for accessing the database
        surveyDAO = new SurveyDAO();

        setTitle("Survey Management");
        setSize(800, 400);  // Increase the size for the table view
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Close only the current window
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Set the appearance
        UIManager.put("Table.alternateRowColor", new Color(240, 240, 240));  // Alternating row colors
        UIManager.put("Table.font", new Font("Arial", Font.PLAIN, 14));  // Font style for table

        // Column names for the table
        String[] columnNames = {"SurveyID", "Title", "Status", "Start", "End", "Question Type"};
        surveyTableModel = new DefaultTableModel(columnNames, 0);  // Initialize with no data
        surveyTable = new JTable(surveyTableModel);
        surveyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        surveyTable.setDefaultEditor(Object.class, null);  // Disable editing of table cells by default

        // Sort the table rows by default
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(surveyTableModel);
        surveyTable.setRowSorter(sorter);

        // Scroll Pane for the table
        JScrollPane scrollPane = new JScrollPane(surveyTable);

        // Panel to hold buttons
        JPanel buttonPanel = new JPanel();
        viewButton = new JButton("View");
        deleteButton = new JButton("Delete");
        backButton = new JButton("Back");

        // Action listener for the back button
        backButton.addActionListener(e -> {
            this.dispose();  // Close the current window
        });

        // Action listener for the View button
        viewButton.addActionListener(e -> viewSurveyQuestions());

        // Action listener for the Delete button
        deleteButton.addActionListener(e -> deleteSurvey());

        // Add buttons to the panel
        buttonPanel.add(viewButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        // Add components to the frame
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Set the frame visible
        setVisible(true);

        // Load surveys data into the table
        loadSurveys();

        // Make the entire row selectable
        surveyTable.setRowSelectionAllowed(true);
        surveyTable.setColumnSelectionAllowed(false);
    }

    // Public method to update the survey list with a new set of surveys
    public void updateSurveyList(List<Survey> surveys) {
        surveyTableModel.setRowCount(0);  // Clear the existing rows

        // Add each survey to the table, with associated question types (MCQ or Rating Scale)
        for (int i = 0; i < surveys.size(); i++) {
            Survey survey = surveys.get(i);
            List<Question> questions = surveyDAO.getQuestionsBySurveyId(survey.getId());

            // Fetch options for MCQ and Rating Scale questions
            String questionTypes = "None";
            String options = "";
            for (Question question : questions) {
                if ("Multiple Choice".equals(question.getQuestionType())) {
                    questionTypes = "MCQ";
                    options = surveyDAO.getQuestionOptions(question.getQuestionId());  // Get MCQ options
                    break;
                } else if ("Rating Scale".equals(question.getQuestionType())) {
                    questionTypes = "Rating Scale";
                    break;
                }
            }

            Object[] rowData = {
                    survey.getId(),  // SurveyID
                    survey.getTitle(),
                    survey.getStatus(),
                    survey.getStartDate(),
                    survey.getEndDate(),
                    questionTypes,  // Question Type (MCQ, Rating Scale, or None)
                    options  // Display MCQ options if available
            };
            surveyTableModel.addRow(rowData);
        }
    }

    // Method to load surveys from the database and update the table
    private void loadSurveys() {
        List<Survey> surveys = surveyDAO.getAllSurveys();  // Fetch surveys from the database
        updateSurveyList(surveys);  // Update the table with the fetched surveys
    }

    // Action for viewing survey questions
    private void viewSurveyQuestions() {
        int selectedRow = surveyTable.getSelectedRow();
        if (selectedRow != -1) {
            int surveyId = (int) surveyTable.getValueAt(selectedRow, 0);  // Get SurveyID
            List<Question> questions = surveyDAO.getQuestionsBySurveyId(surveyId);

            // Create a dialog to display the questions
            JDialog questionsDialog = new JDialog(this, "Survey Questions", true);
            questionsDialog.setSize(600, 400);
            questionsDialog.setLocationRelativeTo(this);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Add padding to the panel

            // Add each question with a number
            int questionNumber = 1;
            for (Question question : questions) {
                JPanel questionPanel = new JPanel();
                questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.X_AXIS));
                questionPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));  // Add padding between questions

                JTextArea questionTextArea = new JTextArea(question.getQuestionText());
                questionTextArea.setEditable(false);  // Make the text area read-only
                questionPanel.add(new JScrollPane(questionTextArea));

                // Label each question with its number
                JLabel questionLabel = new JLabel("Question " + questionNumber);
                questionPanel.add(questionLabel);

                panel.add(questionPanel);

                questionNumber++;  // Increment question number
            }

            // Add a Back button to close the questions dialog
            JButton backButton = new JButton("Back");
            backButton.addActionListener(e -> {
                questionsDialog.dispose();  // Close the current dialog
            });
            panel.add(backButton);

            JScrollPane scrollPane = new JScrollPane(panel);
            questionsDialog.add(scrollPane);
            questionsDialog.setVisible(true);
        }
    }

    // Action for deleting a survey
    private void deleteSurvey() {
        int selectedRow = surveyTable.getSelectedRow();
        if (selectedRow != -1) {
            int surveyId = (int) surveyTable.getValueAt(selectedRow, 0);  // Get SurveyID
            int response = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this survey and its questions?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                // Delete the survey and its associated questions
                surveyDAO.deleteSurveyAndQuestions(surveyId);
                JOptionPane.showMessageDialog(this, "Survey and its questions deleted successfully!");
                loadSurveys();  // Reload surveys after deletion
            }
        }
    }
}
