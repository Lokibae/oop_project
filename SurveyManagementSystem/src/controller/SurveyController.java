package controller;

import java.util.List;
import dao.SurveyDAO;
import model.Survey;
import model.User;
import view.SurveyCreationView;
import view.SurveyManagementView;
import view.AdminDashboardView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SurveyController {
    private SurveyCreationView view;
    private SurveyDAO surveyDAO;
    private User currentUser;

    public SurveyController(SurveyCreationView view, User currentUser) {
        this.view = view;
        this.currentUser = currentUser;
        this.surveyDAO = new SurveyDAO(); // Initialize DAO
        initializeController();
    }

    private void initializeController() {
        // Adding listeners for submit and back button
        view.addSubmitSurveyListener(new SubmitSurveyListener());
        view.addBackButtonListener(new BackButtonListener());
    }

    private class SubmitSurveyListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (validateAndCreateSurvey()) {
                    view.resetForm();  // Reset the form after successful submission
                    showSuccessMessage("Survey created successfully!");

                    // Fetch the list of surveys after creation
                    List<Survey> surveys = surveyDAO.getAllSurveys();  // Now calling the getAllSurveys method

                    // Navigate to the SurveyManagementView and update the list
                    SurveyManagementView surveyManagementView = new SurveyManagementView();
                    surveyManagementView.updateSurveyList(surveys); // Pass the full Survey objects to update the view
                    surveyManagementView.setVisible(true);
                    view.dispose();  // Close the SurveyCreationView
                }
            } catch (IllegalArgumentException ex) {
                showErrorMessage(ex.getMessage());  // Show validation error messages
            } catch (ParseException ex) {
                showErrorMessage("Invalid date format. Please use yyyy-MM-dd");  // Date format error
            } catch (SQLException ex) {
                showErrorMessage("Database error: " + ex.getMessage());  // Database errors
                ex.printStackTrace();
            }
        }
    }

    // Retrieve all surveys from the database
    private List<Survey> getAllSurveys() {
        return surveyDAO.getAllSurveys();  // Fetch all surveys from the database
    }

    private boolean validateAndCreateSurvey() throws IllegalArgumentException, ParseException, SQLException {
        // Get the input form data from SurveyCreationView
        String title = view.getSurveyTitle().trim();
        String description = view.getSurveyDescription().trim();
        String startDateStr = view.getSurveyStartDate();
        String endDateStr = view.getSurveyEndDate();
        String status = view.getSurveyStatus();

        // Validate that no field is empty
        if (title.isEmpty()) throw new IllegalArgumentException("Survey title is required.");
        if (description.isEmpty()) throw new IllegalArgumentException("Survey description is required.");
        if (startDateStr.isEmpty()) throw new IllegalArgumentException("Start date is required.");
        if (endDateStr.isEmpty()) throw new IllegalArgumentException("End date is required.");

        // Parse dates
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(startDateStr);
        Date endDate = sdf.parse(endDateStr);

        // Ensure the end date is after the start date
        if (endDate.before(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }

        // Create a new Survey object with validated data
        Survey survey = new Survey(
                currentUser.getUsername(),
                title,
                description,
                startDateStr,
                endDateStr,
                status
        );

        // Save the survey to the database using SurveyDAO
        return surveyDAO.saveSurvey(survey);  // Pass Survey object
    }

    private class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            navigateToDashboard();
        }

        private void navigateToDashboard() {
            // Navigate to the AdminDashboardView
            AdminDashboardView dashboardView = new AdminDashboardView(currentUser);
            dashboardView.setVisible(true);
            view.dispose();  // Close the SurveyCreationView
        }
    }

    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(view, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(view, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Edit Survey functionality
    public static class EditSurveyListener implements ActionListener {
        private final SurveyDAO surveyDAO;
        private final int selectedRow;

        public EditSurveyListener(SurveyDAO surveyDAO, int selectedRow) {
            this.surveyDAO = surveyDAO;
            this.selectedRow = selectedRow;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedRow != -1) {
                // Retrieve the selected survey title and status
                String surveyTitle = surveyDAO.getSurveyTitle(selectedRow);
                String surveyStatus = surveyDAO.getSurveyStatus(selectedRow);

                // Allow user to edit the survey title and status
                String newTitle = JOptionPane.showInputDialog("Enter new title:", surveyTitle);
                String newStatus = (String) JOptionPane.showInputDialog(
                        null,
                        "Select Status",
                        "Edit Status",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[] {"Active", "Closed"},
                        surveyStatus);

                if (newTitle != null && newStatus != null) {
                    // Update the survey in the database
                    surveyDAO.updateSurveyTitle(selectedRow, newTitle);
                    surveyDAO.updateSurveyStatus(selectedRow, newStatus);
                    JOptionPane.showMessageDialog(null, "Survey updated successfully!");
                }
            }
        }
    }

    // Delete Survey functionality
    public static class DeleteSurveyListener implements ActionListener {
        private final SurveyDAO surveyDAO;
        private final int selectedRow;

        public DeleteSurveyListener(SurveyDAO surveyDAO, int selectedRow) {
            this.surveyDAO = surveyDAO;
            this.selectedRow = selectedRow;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedRow != -1) {
                // Delete the survey from the database
                surveyDAO.deleteSurvey(selectedRow);
                JOptionPane.showMessageDialog(null, "Survey deleted successfully!");
            }
        }
    }
}
