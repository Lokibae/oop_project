package controller;

import view.SurveyResultView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SurveyResultController {
    private SurveyResultView view;

    public SurveyResultController(SurveyResultView view) {
        this.view = view;
        attachEventListeners();
    }

    private void attachEventListeners() {
        view.addGenerateListener(e -> handleGenerateReport());
        view.addExportListener(e -> handleExportCSV());
    }

    private void handleGenerateReport() {
        String selectedSurvey = view.getSelectedSurvey();

        // Simulate loading different data based on survey selection
        Object[][] newData;
        if (selectedSurvey.contains("Customer Satisfaction")) {
            newData = new Object[][]{
                    {"How satisfied are you?", "Avg: 4.2/5"},
                    {"Would you recommend us?", "Yes: 85%"},
                    {"Favorite feature?", "Ease of use: 45%"}
            };
        } else {
            newData = new Object[][]{
                    {"What is your age?", "Avg: 29"},
                    {"Rate our service (1-5)", "Mode: 4"},
                    {"How likely to recommend?", "Avg: 8.2/10"}
            };
        }

        view.updateResultsTable(newData);
        view.showMessage("Report generated for: " + selectedSurvey);
    }

    private void handleExportCSV() {
        // In a real implementation, this would export the table data
        view.showMessage("CSV exported for: " + view.getSelectedSurvey());
    }
}