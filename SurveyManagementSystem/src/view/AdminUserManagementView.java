package view;

import dao.UserDAO;
import model.User;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class AdminUserManagementView extends JFrame {
    private JTextField searchField;
    private JButton searchButton, addUserButton;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JLabel currentUserLabel, adminStatusLabel;
    private UserDAO userDAO;
    private Map<String, JPanel> userActionPanels = new HashMap<>();

    public AdminUserManagementView() {
        initializeDAO();
        initializeUI();
        loadUsersFromDatabase();
    }

    private void initializeDAO() {
        this.userDAO = new UserDAO();
    }

    private void initializeUI() {
        configureWindow();
        createStatusBar();
        createSearchPanel();
        createButtonPanel();
        createUserTable();
        assembleMainPanel();
    }

    private void configureWindow() {
        setTitle("Survey Management Application - Admin User Management");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    private void createStatusBar() {
        JPanel statusPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        currentUserLabel = new JLabel(" Current User: Not logged in");
        adminStatusLabel = new JLabel("Admin Status: None");

        Font statusFont = new Font("Segoe UI", Font.PLAIN, 12);
        currentUserLabel.setFont(statusFont);
        adminStatusLabel.setFont(statusFont);

        statusPanel.add(currentUserLabel);
        statusPanel.add(adminStatusLabel);
        add(statusPanel, BorderLayout.SOUTH);
    }

    private void createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchField = new JTextField(30);
        searchButton = new JButton("Search");

        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchButton.setBackground(new Color(70, 130, 180));
        searchButton.setForeground(Color.WHITE);

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);
    }

    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        addUserButton = new JButton("+ Add New Admin User");

        addUserButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addUserButton.setBackground(new Color(70, 130, 180));
        addUserButton.setForeground(Color.WHITE);

        buttonPanel.add(addUserButton);
        add(buttonPanel, BorderLayout.PAGE_END);
    }

    private void createUserTable() {
        String[] columnNames = {"Username", "Role", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return column == 2 ? JPanel.class : String.class;
            }
        };

        userTable = new JTable(tableModel);
        userTable.setRowHeight(40);
        userTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 2; i++) {
            userTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        TableColumn actionsColumn = userTable.getColumnModel().getColumn(2);
        actionsColumn.setCellRenderer(new ButtonRenderer());
        actionsColumn.setCellEditor(new ButtonEditor());
        actionsColumn.setPreferredWidth(200);
    }

    private void assembleMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(new JScrollPane(userTable), BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void loadUsersFromDatabase() {
        try {
            tableModel.setRowCount(0);
            userActionPanels.clear();

            List<User> users = userDAO.getAllUsers();
            for (User user : users) {
                tableModel.addRow(new Object[]{
                        user.getUsername(),
                        user.getRole(),
                        getOrCreateActionPanel(user.getUsername())
                });
            }
        } catch (SQLException ex) {
            showErrorDialog("Error loading users: " + ex.getMessage());
        }
    }

    public JPanel addUserToTable(String username, String role) {
        // Create a panel for action buttons
        JPanel actionPanel = getOrCreateActionPanel(username);

        // Add the user data to the table
        tableModel.addRow(new Object[]{
                username,
                role,
                actionPanel
        });

        return actionPanel;
    }

    private JPanel getOrCreateActionPanel(String username) {
        if (userActionPanels.containsKey(username)) {
            return userActionPanels.get(username);
        }

        // Create the panel with action buttons
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setOpaque(true);

        JButton editButton = createActionButton("Edit", new Color(70, 130, 180),
                e -> handleEditUser(username));
        JButton deleteButton = createActionButton("Delete", new Color(220, 53, 69),
                e -> handleDeleteUser(username));

        panel.add(editButton);
        panel.add(deleteButton);

        userActionPanels.put(username, panel);  // Cache the panel
        return panel;
    }

    private JButton createActionButton(String text, Color bgColor, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.addActionListener(listener);  // Attach listener to button
        return button;
    }

    private void handleEditUser(String displayedUsername) {
        try {
            String currentUsername = userDAO.getExactUsername(displayedUsername);
            if (currentUsername == null) {
                showErrorDialog("User '" + displayedUsername + "' not found in database!");
                loadUsersFromDatabase();
                return;
            }

            String newUsername = JOptionPane.showInputDialog(
                    this,
                    "Enter new username:",
                    "Edit User: " + currentUsername,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (newUsername == null || newUsername.trim().isEmpty()) {
                return;
            }
            newUsername = newUsername.trim();

            boolean success = userDAO.updateUsername(currentUsername, newUsername);

            if (success) {
                loadUsersFromDatabase();
                showSuccessDialog("Username updated successfully!");
            } else {
                showErrorDialog("Failed to update username. It may already exist.");
                loadUsersFromDatabase();
            }
        } catch (SQLException ex) {
            showErrorDialog("Database error: " + ex.getMessage());
            loadUsersFromDatabase();
        }
    }

    private void handleDeleteUser(String username) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete user '" + username + "'?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = userDAO.deleteUser(username);
                if (success) {
                    loadUsersFromDatabase();
                    showSuccessDialog("User deleted successfully!");
                } else {
                    showErrorDialog("Failed to delete user. User might not exist.");
                }
            } catch (SQLException ex) {
                showErrorDialog("Error deleting user: " + ex.getMessage());
            }
        }
    }

    public String getSearchText() {
        return searchField.getText().trim();
    }

    public void clearTable() {
        tableModel.setRowCount(0);
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JTable getUserTable() {
        return userTable;
    }

    public void addAddUserListener(ActionListener listener) {
        addUserButton.addActionListener(listener);
    }

    public void addSearchListener(ActionListener listener) {
        searchButton.addActionListener(e -> {
            String searchText = getSearchText();  // Get the search input
            clearTable();  // Clear the table before showing results

            if (!searchText.isEmpty()) {
                try {
                    List<User> users = userDAO.searchUsers(searchText);  // Use the search method
                    if (users.isEmpty()) {
                        showErrorDialog("No users found for search term: " + searchText);
                    } else {
                        for (User user : users) {
                            // Instead of calling addUserToTable, directly add rows to the table
                            tableModel.addRow(new Object[]{
                                    user.getUsername(),
                                    user.getRole(),
                                    getOrCreateActionPanel(user.getUsername())  // Use existing panel for actions
                            });
                        }
                    }
                } catch (SQLException ex) {
                    showErrorDialog("Error during search: " + ex.getMessage());
                }
            } else {
                showErrorDialog("Please enter a search term.");
            }

            searchField.setText("");  // Clear the search field after search attempt
        });
    }

    public void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccessDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setLoggedInUser(String username, boolean isAdmin) {
        currentUserLabel.setText(" Current User: " + username);
        adminStatusLabel.setText(" Admin Status: " + (isAdmin ? "Admin" : "User"));
    }
}
