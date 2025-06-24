package controller;

import dao.UserDAO;
import model.User;
import view.AdminUserManagementView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AdminUserManagementController {
    private AdminUserManagementView view;
    private UserDAO userDAO;

    public AdminUserManagementController(AdminUserManagementView view) {
        this.view = view;
        this.userDAO = new UserDAO();
        initializeController();
        loadUsers();
    }

    private void initializeController() {
        view.addAddUserListener(e -> handleAddUser());
        view.addSearchListener(e -> handleSearch());
    }

    private void loadUsers() {
        try {
            view.clearTable();
            for (User user : userDAO.getAllUsers()) {
                JPanel actionPanel = view.addUserToTable(user.getUsername(), user.getRole());
                attachButtonListeners(actionPanel, user.getUsername());
            }
        } catch (SQLException ex) {
            showDatabaseError("loading users", ex);
        }
    }

    private void handleAddUser() {
        String username = JOptionPane.showInputDialog(view, "Enter username:");
        if (username == null || username.trim().isEmpty()) {
            return;
        }

        String role = (String) JOptionPane.showInputDialog(view,
                "Select role:", "Add User",
                JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Super Admin", "Survey Creator", "Respondent"},
                "Survey Creator");

        if (role != null) {
            try {
                if (userDAO.userExists(username)) {
                    view.showErrorDialog("Username already exists!");
                    return;
                }

                User newUser = new User(0, username, "defaultPassword", role);
                if (userDAO.register(newUser)) {
                    JPanel actionPanel = view.addUserToTable(username, role);
                    attachButtonListeners(actionPanel, username);
                    view.showSuccessDialog("User added successfully!");
                }
            } catch (SQLException ex) {
                showDatabaseError("adding user", ex);
            }
        }
    }

    private void handleEditUser(String username) {
        String currentRole = getRoleForUser(username);

        String newRole = (String) JOptionPane.showInputDialog(view,
                "Edit role for " + username + ":", "Edit User",
                JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Super Admin", "Survey Creator", "Respondent"},
                currentRole);

        if (newRole != null && !newRole.equals(currentRole)) {
            try {
                if (userDAO.updateUserRole(username, newRole)) {
                    updateUserInTable(username, newRole);
                    view.showSuccessDialog("User updated successfully!");
                }
            } catch (SQLException ex) {
                showDatabaseError("updating user", ex);
            }
        }
    }

    private void handleDeleteUser(String username) {
        int confirm = JOptionPane.showConfirmDialog(view,
                "Are you sure you want to delete user " + username + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (userDAO.deleteUser(username)) {
                    removeUserFromTable(username);
                    view.showSuccessDialog("User deleted successfully!");
                }
            } catch (SQLException ex) {
                showDatabaseError("deleting user", ex);
            }
        }
    }

    private void handleSearch() {
        String searchText = view.getSearchText().toLowerCase();
        try {
            view.clearTable();
            for (User user : userDAO.searchUsers(searchText)) {
                JPanel actionPanel = view.addUserToTable(user.getUsername(), user.getRole());
                attachButtonListeners(actionPanel, user.getUsername());
            }
        } catch (SQLException ex) {
            showDatabaseError("searching users", ex);
        }
    }

    private void attachButtonListeners(JPanel actionPanel, String username) {
        for (Component comp : actionPanel.getComponents()) {
            if (comp instanceof JButton button) {
                // Remove existing listeners
                for (ActionListener al : button.getActionListeners()) {
                    button.removeActionListener(al);
                }

                // Add new listener based on button text
                if ("Edit".equals(button.getText())) {
                    button.addActionListener(e -> handleEditUser(username));
                } else if ("Delete".equals(button.getText())) {
                    button.addActionListener(e -> handleDeleteUser(username));
                }
            }
        }
    }

    private String getRoleForUser(String username) {
        for (int i = 0; i < view.getTableModel().getRowCount(); i++) {
            if (username.equals(view.getTableModel().getValueAt(i, 0))) {
                return (String) view.getTableModel().getValueAt(i, 1);
            }
        }
        return "";
    }

    private void updateUserInTable(String username, String newRole) {
        for (int i = 0; i < view.getTableModel().getRowCount(); i++) {
            if (username.equals(view.getTableModel().getValueAt(i, 0))) {
                view.getTableModel().setValueAt(newRole, i, 1);
                break;
            }
        }
    }

    private void removeUserFromTable(String username) {
        for (int i = 0; i < view.getTableModel().getRowCount(); i++) {
            if (username.equals(view.getTableModel().getValueAt(i, 0))) {
                view.getTableModel().removeRow(i);
                break;
            }
        }
    }

    private void showDatabaseError(String action, SQLException ex) {
        view.showErrorDialog("Error " + action + ": " + ex.getMessage());
    }
}