package controller;

import model.User;
import view.*;

public class AdminDashboardController {
    private final AdminDashboardView view;
    private final User currentUser;

    public AdminDashboardController(AdminDashboardView view, User user) {
        this.view = view;
        this.currentUser = user;
        initController();
    }

    private void initController() {
        view.getCreateSurveyBtn().addActionListener(e -> openSurveyCreation());
        view.getManageSurveysBtn().addActionListener(e -> openSurveyManagement());
        view.getUserManagementBtn().addActionListener(e -> openUserManagement());
        view.getViewResultsBtn().addActionListener(e -> openSurveyResults());
        view.getLogoutBtn().addActionListener(e -> logout());
    }

    private void openSurveyCreation() {
        SurveyCreationView creationView = new SurveyCreationView(currentUser);
        creationView.setVisible(true);
    }

    private void openSurveyManagement() {
        SurveyManagementView managementView = new SurveyManagementView();
        managementView.setVisible(true);
    }


    private void openUserManagement() {
        AdminUserManagementView userView = new AdminUserManagementView();
        userView.setLoggedInUser(currentUser.getUsername(), true);
        userView.setVisible(true);
    }

    private void openSurveyResults() {
        SurveyResultView resultView = new SurveyResultView();
        resultView.setVisible(true); // Make sure to set visible
        new SurveyResultController(resultView); // Initialize controller
    }

    private void logout() {
        view.dispose();
        LoginView loginView = new LoginView();
        RegisterView registerView = new RegisterView();
        new AuthController(loginView, registerView);
        loginView.setVisible(true);
    }
}
