package view;

import model.User;
import model.Question;
import dao.SurveyDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.UtilDateModel;
import java.util.Properties;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SurveyCreationView extends JFrame {
    private int currentSurveyId = -1;
    private List<Question> questions = new ArrayList<>();
    private JTextField titleField, descriptionField;
    private JTextField startDateField, endDateField;
    private JComboBox<String> statusBox;
    private JTextField questionTextField;
    private JComboBox<String> questionTypeBox;
    private JTextField minRatingField, maxRatingField;
    private JTextField numOptionsField;
    private JPanel optionsPanel;
    private JScrollPane optionsScrollPane;
    private JButton submitButton, backButton, addQuestionButton;
    private User currentUser;
    private JTextArea questionsListArea;

    public SurveyCreationView(User currentUser) {
        this.currentUser = currentUser;
        initializeUI();
        setupEventListeners();
    }

    private void initializeUI() {
        setTitle("Survey Creation - Logged in as: " + currentUser.getUsername());
        setSize(700, 700); // Increased size to accommodate questions list
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Create a scrollable form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        // Survey fields
        addFormField(formPanel, "Title:", titleField = new JTextField(20));
        addFormField(formPanel, "Description:", descriptionField = new JTextField(20));
        formPanel.add(createDateField("Start Date:"));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(createDateField("End Date:"));
        formPanel.add(Box.createVerticalStrut(10));
        addFormField(formPanel, "Status:", statusBox = new JComboBox<>(new String[]{"Active", "Closed"}));

        // Question section
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(new JLabel("Add Question"));
        formPanel.add(Box.createVerticalStrut(10));
        addFormField(formPanel, "Question Text:", questionTextField = new JTextField(20));

        formPanel.add(new JLabel("Question Type:"));
        questionTypeBox = new JComboBox<>(new String[]{"Multiple Choice", "Text Input", "Rating Scale"});
        formPanel.add(questionTypeBox);
        formPanel.add(Box.createVerticalStrut(10));

        addFormField(formPanel, "Number of Options (1-99):", numOptionsField = new JTextField(3));

        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsScrollPane = new JScrollPane(optionsPanel);
        optionsScrollPane.setPreferredSize(new Dimension(400, 150));
        optionsScrollPane.setBorder(BorderFactory.createTitledBorder("Options"));
        optionsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        formPanel.add(optionsScrollPane);

        addFormField(formPanel, "Minimum Rating:", minRatingField = new JTextField(3));
        addFormField(formPanel, "Maximum Rating:", maxRatingField = new JTextField(3));

        // Initially hide optional fields
        minRatingField.setVisible(false);
        maxRatingField.setVisible(false);
        numOptionsField.setVisible(false);
        optionsScrollPane.setVisible(false);

        // Questions list area
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(new JLabel("Current Questions:"));
        questionsListArea = new JTextArea(5, 40);
        questionsListArea.setEditable(false);
        questionsListArea.setBorder(BorderFactory.createEtchedBorder());
        JScrollPane questionsScrollPane = new JScrollPane(questionsListArea);
        formPanel.add(questionsScrollPane);

        // Wrap the form panel in a scroll pane
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(120, 30));
        buttonPanel.add(backButton);

        submitButton = new JButton("Submit Survey");
        submitButton.setPreferredSize(new Dimension(120, 30));
        buttonPanel.add(submitButton);

        addQuestionButton = new JButton("Add Question");
        addQuestionButton.setPreferredSize(new Dimension(120, 30));
        buttonPanel.add(addQuestionButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
        pack();
        setVisible(true);
    }

    private void setupEventListeners() {
        submitButton.addActionListener(e -> saveSurveyToDatabase());
        addQuestionButton.addActionListener(e -> addQuestionToSurvey());
        backButton.addActionListener(e -> dispose());

        questionTypeBox.addActionListener(e -> {
            String selectedType = (String) questionTypeBox.getSelectedItem();
            boolean isMultipleChoice = "Multiple Choice".equals(selectedType);
            boolean isRatingScale = "Rating Scale".equals(selectedType);

            numOptionsField.setVisible(isMultipleChoice);
            optionsScrollPane.setVisible(isMultipleChoice);
            minRatingField.setVisible(isRatingScale);
            maxRatingField.setVisible(isRatingScale);
        });

        numOptionsField.addActionListener(e -> updateOptionsFields());
    }

    private void addFormField(JPanel parent, String label, JComponent field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel.add(new JLabel(label));
        panel.add(field);
        parent.add(panel);
        parent.add(Box.createVerticalStrut(5));
    }

    private JPanel createDateField(String label) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel.add(new JLabel(label));

        JTextField dateField = new JTextField(10);
        dateField.setEditable(false);
        dateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        JButton pickerButton = new JButton("...");
        pickerButton.setMargin(new Insets(0, 5, 0, 5));
        pickerButton.addActionListener(e -> showCalendarPopup(dateField));

        panel.add(dateField);
        panel.add(pickerButton);

        if (label.equals("Start Date:")) {
            startDateField = dateField;
        } else {
            endDateField = dateField;
        }

        return panel;
    }

    private void showCalendarPopup(JTextField dateField) {
        JDialog popup = new JDialog(this, "Select Date", true);
        popup.setUndecorated(true);
        popup.setSize(300, 250);
        popup.setLocationRelativeTo(this);
        popup.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);

        try {
            String dateText = dateField.getText();
            if (dateText != null && !dateText.isEmpty()) {
                Date currentDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateText);
                model.setValue(currentDate);
            } else {
                model.setValue(new Date());
            }
        } catch (Exception e) {
            model.setValue(new Date());
        }

        datePanel.addActionListener(e -> {
            Date selectedDate = model.getValue();
            if (selectedDate != null) {
                dateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(selectedDate));
                popup.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No date selected!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            popup.setVisible(false);
            popup.dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        popup.add(buttonPanel, BorderLayout.SOUTH);
        popup.add(datePanel);
        popup.setVisible(true);
    }

    private int saveSurveyToDatabase() {
        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        String startDate = startDateField.getText();
        String endDate = endDateField.getText();
        String status = (String) statusBox.getSelectedItem();

        if (title.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }

        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add at least one question!", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }

        String url = "jdbc:mysql://localhost:3306/survey_db";
        String dbUsername = "root";
        String password = "Daniel_10";

        try (Connection connection = DriverManager.getConnection(url, dbUsername, password)) {
            connection.setAutoCommit(false); // Start transaction

            String surveyQuery = "INSERT INTO surveys (username, title, description, start_date, end_date, status) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement surveyStmt = connection.prepareStatement(surveyQuery, Statement.RETURN_GENERATED_KEYS)) {
                surveyStmt.setString(1, currentUser.getUsername());
                surveyStmt.setString(2, title);
                surveyStmt.setString(3, description);
                surveyStmt.setString(4, startDate);
                surveyStmt.setString(5, endDate);
                surveyStmt.setString(6, status);

                int rowsAffected = surveyStmt.executeUpdate();

                if (rowsAffected > 0) {
                    try (ResultSet generatedKeys = surveyStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            currentSurveyId = generatedKeys.getInt(1);

                            // Save all questions in transaction
                            String questionQuery = "INSERT INTO questions (survey_id, question_text, question_type, options, min_rating, max_rating) VALUES (?, ?, ?, ?, ?, ?)";
                            try (PreparedStatement questionStmt = connection.prepareStatement(questionQuery)) {
                                for (Question question : questions) {
                                    questionStmt.setInt(1, currentSurveyId);
                                    questionStmt.setString(2, question.getQuestionText());
                                    questionStmt.setString(3, question.getQuestionType());
                                    questionStmt.setString(4, question.getOptions());
                                    questionStmt.setInt(5, question.getMinRating());
                                    questionStmt.setInt(6, question.getMaxRating());
                                    questionStmt.addBatch();
                                }
                                questionStmt.executeBatch();
                            }

                            connection.commit();
                            JOptionPane.showMessageDialog(this,
                                    "Survey and questions saved successfully! ID: " + currentSurveyId,
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                            resetForm();
                            return currentSurveyId;
                        }
                    }
                }
                connection.rollback();
                return -1;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return -1;
        }
    }

    private void addQuestionToSurvey() {
        String questionText = questionTextField.getText().trim();
        String questionType = (String) questionTypeBox.getSelectedItem();

        if (questionText.isEmpty()) {
            showError("Question Text cannot be empty.");
            return;
        }

        try {
            Question question = createQuestion(questionText, questionType);
            questions.add(question);

            // Update the questions list display
            updateQuestionsList();

            showSuccess("Question added to survey (not saved to database yet)");
            clearQuestionFields(questionType);
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void updateQuestionsList() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            sb.append((i + 1)).append(". ").append(q.getQuestionText());

            if ("Multiple Choice".equals(q.getQuestionType())) {
                sb.append(" [Multiple Choice]");
            } else if ("Rating Scale".equals(q.getQuestionType())) {
                sb.append(" [Rating Scale: ").append(q.getMinRating()).append("-").append(q.getMaxRating()).append("]");
            } else {
                sb.append(" [Text Input]");
            }
            sb.append("\n");
        }
        questionsListArea.setText(sb.toString());
    }

    private Question createQuestion(String questionText, String questionType) throws IllegalArgumentException {
        String optionsData = null;
        Integer minRating = null;
        Integer maxRating = null;

        if ("Multiple Choice".equals(questionType)) {
            optionsData = collectOptions();
            if (optionsData == null) {
                throw new IllegalArgumentException("Please fill in all option fields.");
            }
        } else if ("Rating Scale".equals(questionType)) {
            try {
                minRating = Integer.parseInt(minRatingField.getText());
                maxRating = Integer.parseInt(maxRatingField.getText());
                if (minRating >= maxRating) {
                    throw new IllegalArgumentException("Maximum rating must be greater than minimum rating.");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Please enter valid numbers for rating scale.");
            }
        }

        return new Question(
                0, // survey_id will be set when survey is saved
                questionText,
                questionType,
                optionsData,
                minRating != null ? minRating : 0,
                maxRating != null ? maxRating : 0
        );
    }

    private String collectOptions() {
        StringBuilder optionsBuilder = new StringBuilder();
        for (Component comp : optionsPanel.getComponents()) {
            if (comp instanceof JTextField) {
                String optionText = ((JTextField) comp).getText().trim();
                if (optionText.isEmpty()) {
                    return null;
                }
                if (optionsBuilder.length() > 0) {
                    optionsBuilder.append("|");
                }
                optionsBuilder.append(optionText);
            }
        }
        return optionsBuilder.toString();
    }

    private void updateOptionsFields() {
        try {
            int numOptions = Integer.parseInt(numOptionsField.getText());
            if (numOptions < 1 || numOptions > 99) {
                showError("Please enter a number between 1 and 99.");
                return;
            }

            optionsPanel.removeAll();
            optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));

            for (int i = 0; i < numOptions; i++) {
                JPanel optionRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JLabel optionLabel = new JLabel(String.valueOf((char) ('a' + i)) + ":");
                JTextField optionField = new JTextField(30);
                optionRow.add(optionLabel);
                optionRow.add(optionField);
                optionsPanel.add(optionRow);
            }

            optionsPanel.revalidate();
            optionsPanel.repaint();
        } catch (NumberFormatException e) {
            showError("Please enter a valid number for options.");
        }
    }

    private void clearQuestionFields(String questionType) {
        questionTextField.setText("");
        if ("Multiple Choice".equals(questionType)) {
            optionsPanel.removeAll();
            optionsPanel.revalidate();
            optionsPanel.repaint();
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void resetForm() {
        titleField.setText("");
        descriptionField.setText("");
        startDateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        endDateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        statusBox.setSelectedIndex(0);
        questionTextField.setText("");
        optionsPanel.removeAll();
        optionsPanel.revalidate();
        optionsPanel.repaint();
        questions.clear();
        questionsListArea.setText("");
        currentSurveyId = -1;
    }

    // Getters
    public int getCurrentSurveyId() { return currentSurveyId; }
    public List<Question> getQuestions() { return questions; }
    public String getSurveyTitle() { return titleField.getText(); }
    public String getSurveyDescription() { return descriptionField.getText(); }
    public String getSurveyStartDate() { return startDateField.getText(); }
    public String getSurveyEndDate() { return endDateField.getText(); }
    public String getSurveyStatus() { return (String) statusBox.getSelectedItem(); }

    // Action listener setters
    public void addSubmitSurveyListener(ActionListener listener) {
        submitButton.addActionListener(listener);
    }

    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
}
