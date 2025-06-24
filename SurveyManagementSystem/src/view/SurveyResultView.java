package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class SurveyResultView extends JFrame {
    private JComboBox<String> surveyComboBox;
    private JTable resultsTable;
    private JButton generateButton;
    private JButton exportButton;

    public SurveyResultView() {
        setTitle("Survey Management Application");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Create top panel with survey selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.add(new JLabel("Select Survey:"));

        surveyComboBox = new JComboBox<>(new String[]{
                "Customer Satisfaction Survey",
                "Product Feedback",
                "Employee Engagement"
        });
        surveyComboBox.setPreferredSize(new Dimension(300, 25));
        topPanel.add(surveyComboBox);

        generateButton = new JButton("Go");
        topPanel.add(generateButton);

        add(topPanel, BorderLayout.NORTH);

        // Create results table
        String[] columnNames = {"Question", "Summary"};
        Object[][] data = {
                {"What is your age?", "Avg: 29"},
                {"Rate our service (1-5)", "Mode: 4"},
                {"How likely to recommend?", "Avg: 8.2/10"},
                {"Favorite product feature?", "Most: Ease of use"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        resultsTable = new JTable(model);
        resultsTable.setRowHeight(30);
        JScrollPane tableScrollPane = new JScrollPane(resultsTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        exportButton = new JButton("Export CSV");
        buttonPanel.add(exportButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Set default window position
        setLocationRelativeTo(null);
    }

    // Methods for controller to add listeners
    public void addGenerateListener(ActionListener listener) {
        generateButton.addActionListener(listener);
    }

    public void addExportListener(ActionListener listener) {
        exportButton.addActionListener(listener);
    }

    public String getSelectedSurvey() {
        return (String) surveyComboBox.getSelectedItem();
    }

    public void updateResultsTable(Object[][] newData) {
        DefaultTableModel model = (DefaultTableModel) resultsTable.getModel();
        model.setRowCount(0); // Clear existing data
        for (Object[] row : newData) {
            model.addRow(row);
        }
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}