package controller;

import dao.UserDAO;
import model.User;
import view.*;

import java.sql.SQLException;

public class AuthController {
    private final LoginView loginView;
    private final RegisterView registerView;
    private final UserDAO userDAO;

    public AuthController(LoginView loginView, RegisterView registerView) {
        this.loginView = loginView;
        this.registerView = registerView;
        this.userDAO = new UserDAO();
        initControllers();
    }

    private void initControllers() {
        // Set up login view actions
        loginView.setLoginAction(e -> handleLogin());
        loginView.setRegisterAction(e -> showRegisterView());

        // Set up register view actions
        registerView.setRegisterAction(e -> handleRegistration());
        registerView.setBackAction(e -> showLoginView()); // Add back button to RegisterView
    }

    private void showRegisterView() {
        loginView.setVisible(false);    // Hide login view
        registerView.setVisible(true);  // Show register view
        registerView.clearFields();     // Clear any previous entries
    }

    private void showLoginView() {
        registerView.setVisible(false); // Hide register view
        loginView.setVisible(true);    // Show login view
        loginView.clearFields();       // Clear any previous entries
    }

    private void handleLogin() {
        try {
            User user = userDAO.authenticate(loginView.getUsername(), loginView.getPassword());
            if (user != null) {
                loginView.showMessage("Login successful!");
                loginView.setVisible(false);

                if (user.getRole().equalsIgnoreCase("admin")) {
                    AdminDashboardView adminDashboard = new AdminDashboardView(user);
                    new AdminDashboardController(adminDashboard, user); // Attach controller
                } else {
                    new RespondentDashboardView(user); // Respondent dashboard remains
                }
            } else {
                loginView.showMessage("Invalid credentials");
            }
        } catch (SQLException ex) {
            loginView.showMessage("Database error: " + ex.getMessage());
        }
    }


    private void handleRegistration() {
        try {
            String username = registerView.getUsername();
            String password = registerView.getPassword();
            String role = registerView.getRole();

            if (username.isEmpty() || password.isEmpty()) {
                registerView.showMessage("Please fill all fields");
                return;
            }

            if (userDAO.register(new User(0, username, password, role))) {
                registerView.showMessage("Registration successful!");
                showLoginView(); // Return to login after registration
            }
        } catch (SQLException ex) {
            registerView.showMessage(ex.getMessage().contains("Duplicate") ?
                    "Username already exists" : "Registration failed");
        }
    }
}